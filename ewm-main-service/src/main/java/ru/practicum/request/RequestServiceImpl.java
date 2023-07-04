package ru.practicum.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.enums.EventState;
import ru.practicum.enums.RequestState;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.user.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public RequestServiceImpl(RequestRepository requestRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    //API Пользователя для запросов
    @Override
    public ParticipationRequest createRequest(int userId, int eventId) {
        log.info("Получен запрос в сервис на создание запроса");
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);
        if (requestRepository.findByRequesterAndEvent(userId, eventId).isPresent()) {
            log.warn("Попытка отправки повторного запроса");
            throw new ConflictException("Попытка отправки повторного запроса");
        }
        if (event.getInitiator() == userId) {
            log.warn("Инициатор события не может добавить запрос на участие в своём событии");
            throw new ConflictException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (event.getState().compareTo(EventState.PUBLISHED) != 0) {
            log.warn("Нельзя участвовать в неопубликованном событии");
            throw new ConflictException("Нельзя участвовать в неопубликованном событии");
        }
        if (Objects.equals(event.getParticipantLimit(), event.getConfirmedRequests()) &&
            event.getParticipantLimit() != 0) {
            log.warn("У события достигнут лимит запросов на участие");
            throw new ConflictException("У события достигнут лимит запросов на участие");
        }
        ParticipationRequest request = new ParticipationRequest();
        request.setEvent(eventId);
        request.setRequester(userId);
        if (event.getRequestModeration() && event.getParticipantLimit() > 0) {
            request.setStatus(RequestState.PENDING);
        }  else {
            request.setStatus(RequestState.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
        request.setCreated(LocalDateTime.now());
        return requestRepository.save(request);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByUser(int userId) {
        log.info("Получен запрос в сервис на получение запросов пользователя");
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        List<ParticipationRequestDto> dtoList = new ArrayList<>();
        List<ParticipationRequest> requestList = requestRepository.findByRequester(userId);
        for (ParticipationRequest request : requestList) {
            dtoList.add(MappingRequest.mapToDto(request));
        }
        return dtoList;
    }

    @Override
    public ParticipationRequest cancelRequest(int userId, int requestId) {
        log.info("Получен запрос в сервис на отмену запроса");
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        ParticipationRequest request = requestRepository.findById(requestId).orElseThrow(NotFoundException::new);
        request.setStatus(RequestState.CANCELED);
        return requestRepository.save(request);
    }

    //API Приватный для запросов
    @Override
    public List<ParticipationRequestDto> getRequest(int userId, int eventId) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        List<ParticipationRequestDto> dtoList = new ArrayList<>();
        List<ParticipationRequest> requestList = requestRepository.findByEvent(eventId);
        for (ParticipationRequest request : requestList) {
            dtoList.add(MappingRequest.mapToDto(request));
        }
        return dtoList;
    }

    @Override
    public EventRequestStatusUpdateResult updateRequests(int userId,
                                                         int eventId,
                                                         EventRequestStatusUpdateRequest requests) {
        boolean needConf = true;
        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);
        //нужна ли модерация
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            needConf = false;
        }
        //на входе проверить, не превышен ли лимит
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        if (requests.getStatus().equals(RequestState.CONFIRMED) &&
                event.getConfirmedRequests() == event.getParticipantLimit() &&
                needConf) {
            throw new ConflictException("Достигнут лимит по заявкам на данное событие");
        }
        EventRequestStatusUpdateResult resultRequests = new EventRequestStatusUpdateResult();
        List<ParticipationRequest> requestList = requestRepository.findAllById(requests.getRequestIds());
        List<ParticipationRequestDto> dtoListConfirm = new ArrayList<>();
        List<ParticipationRequestDto> dtoListReject = new ArrayList<>();
        //индикатор достижения лимита
        boolean limitCheck = false;
        for (ParticipationRequest request : requestList) {
            if (requests.getStatus().equals(RequestState.CONFIRMED) && !limitCheck) {
                request.setStatus(RequestState.CONFIRMED);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                if (Objects.equals(event.getConfirmedRequests(), event.getParticipantLimit())) {
                    limitCheck = true;
                }
            } else {
                if (requests.getStatus().equals(RequestState.REJECTED) &&
                request.getStatus().equals(RequestState.CONFIRMED)) {
                    throw new ConflictException("Можно отклонить заявки в статусе ожидания");
                }
                request.setStatus(RequestState.REJECTED);
            }
            ParticipationRequest savedRequest = requestRepository.save(request);
            //распределяем запрос по спискам
            if (savedRequest.getStatus().equals(RequestState.CONFIRMED)) {
                dtoListConfirm.add(MappingRequest.mapToDto(savedRequest));
            } else {
                dtoListReject.add(MappingRequest.mapToDto(savedRequest));
            }
        }
        eventRepository.save(event);
        resultRequests.setRejectedRequests(dtoListReject);
        resultRequests.setConfirmedRequests(dtoListConfirm);
        return resultRequests;
    }
}
