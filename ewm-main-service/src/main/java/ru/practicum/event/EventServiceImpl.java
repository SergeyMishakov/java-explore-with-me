package ru.practicum.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.StatClient;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryRepository;
import ru.practicum.enums.ActionState;
import ru.practicum.enums.EventState;
import ru.practicum.enums.SortState;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.modelDto.HitDto;
import ru.practicum.modelDto.StatDto;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatClient statClient;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, CategoryRepository categoryRepository, UserRepository userRepository, StatClient statClient) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.statClient = statClient;
    }

    @Override
    public EventFullDto createEvent(int userId, Event event) {
        log.info("Получен запрос в сервис создания события");
        Category category = categoryRepository.findById(event.getCategory()).orElseThrow(NotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        //проверить что событие наступит не ранее чем через 2 часа
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException();
        }
        event.setInitiator(userId);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        event.setConfirmedRequests(0);
        event.setViews(0L);
        Event savedEvent = eventRepository.save(event);
        return MappingEvent.mapToEventFullDto(savedEvent, category, user);
    }

    @Override
    public List<EventShortDto> getEvents(int userId, Integer from, Integer size) {
        List<EventShortDto> eventSDList = new ArrayList<>();
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        List<Event> eventList = eventRepository.getAllEventsByUser(userId, size, from);
        for (Event event : eventList) {
            Category category = categoryRepository.findById(event.getCategory()).orElseThrow(NotFoundException::new);
            eventSDList.add(MappingEvent.mapToEventShortDto(event, category, user));
        }
        return eventSDList;
    }

    @Override
    public EventFullDto getEventById(int userId, int eventId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);
        Category category = categoryRepository.findById(event.getCategory()).orElseThrow(NotFoundException::new);
        return MappingEvent.mapToEventFullDto(event, category, user);
    }

    @Override
    public EventFullDto changeEventByUser(int userId, int eventId, ActionState actionState, Event event) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Event updatedEvent = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);
        //проверить что событие еще не опубликовано
        if (updatedEvent.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Изменить можно только отмененные события или события в состоянии ожидания модерации");
        }
        if (event.getAnnotation() != null) {
            updatedEvent.setAnnotation(event.getAnnotation());
        }
        if (event.getCategory() != null) {
            updatedEvent.setCategory(event.getCategory());
        }
        if (event.getDescription() != null) {
            updatedEvent.setDescription(event.getDescription());
        }
        if (event.getEventDate() != null) {
            //проверить что новая дата не в прошлом
            if (event.getEventDate().isBefore(LocalDateTime.now())) {
                throw new ValidationException();
            }
            updatedEvent.setEventDate(event.getEventDate());
        }
        if (event.getLat() != null) {
            updatedEvent.setLat(event.getLat());
        }
        if (event.getLon() != null) {
            updatedEvent.setLat(event.getLat());
        }
        if (event.getLat() != null) {
            updatedEvent.setLon(event.getLon());
        }
        if (event.getPaid() != null) {
            updatedEvent.setPaid(event.getPaid());
        }
        if (event.getParticipantLimit() != null) {
            updatedEvent.setParticipantLimit(event.getParticipantLimit());
        }
        if (event.getRequestModeration() != null) {
            updatedEvent.setRequestModeration(event.getRequestModeration());
        }
        if (event.getTitle() != null) {
            updatedEvent.setTitle(event.getTitle());
        }
        if (actionState == ActionState.CANCEL_REVIEW) {
            updatedEvent.setState(EventState.CANCELED);
        } else if (actionState == ActionState.SEND_TO_REVIEW) {
            updatedEvent.setState(EventState.PENDING);
        }
        Category category = categoryRepository.findById(updatedEvent.getCategory()).orElseThrow(NotFoundException::new);
        Event savedEvent = eventRepository.save(updatedEvent);
        return MappingEvent.mapToEventFullDto(savedEvent, category, user);
    }

    @Override
    public List<EventFullDto> getEventsAdmin(List<Integer> users,
                                              List<EventState> states,
                                              List<Integer> categories,
                                              String rangeStart,
                                              String rangeEnd,
                                              Integer from,
                                              Integer size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        List<Predicate> params = new ArrayList<>();
        if (users != null && users.size() > 0) {
            Expression<Integer> userExp = root.get("initiator");
            params.add(userExp.in(users));
        }
        if (states != null && states.size() > 0) {
            Expression<Integer> stateExp = root.get("state");
            params.add(stateExp.in(states));
        }
        if (categories != null && categories.size() > 0) {
            Expression<Integer> categoryExp = root.get("category");
            params.add(categoryExp.in(categories));
        }
        if (rangeStart != null) {
            params.add(cb.greaterThanOrEqualTo(root.get("eventDate"), LocalDateTime.parse(rangeStart, DTF)));
        }
        if (rangeEnd != null) {
            params.add(cb.lessThanOrEqualTo(root.get("eventDate"), LocalDateTime.parse(rangeEnd, DTF)));
        }
        cq.where(cb.and(params.toArray(new Predicate[params.size()])));
        cq.orderBy(cb.asc(root.get("eventDate")));
        cq.select(root);
        List<Event> cdEventList = em.createQuery(cq).setFirstResult(from).setMaxResults(size).getResultList();
        List<EventFullDto> eventFDList = new ArrayList<>();
        for (Event event : cdEventList) {
            User user = userRepository.findById(event.getInitiator()).orElseThrow(NotFoundException::new);
            Category category = categoryRepository.findById(event.getCategory()).orElseThrow(NotFoundException::new);
            eventFDList.add(MappingEvent.mapToEventFullDto(event, category, user));
        }
        return eventFDList;
    }

    @Override
    public EventFullDto changeEventByAdmin(int eventId, ActionState actionState, Event event) {

        Event updatedEvent = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);
        if (event.getAnnotation() != null) {
            updatedEvent.setAnnotation(event.getAnnotation());
        }
        if (event.getCategory() != null) {
            updatedEvent.setCategory(event.getCategory());
        }
        if (event.getDescription() != null) {
            updatedEvent.setDescription(event.getDescription());
        }
        if (event.getEventDate() != null) {
            //проверить что новая дата не в прошлом
            if (event.getEventDate().isBefore(LocalDateTime.now())) {
                throw new ValidationException();
            }
            updatedEvent.setEventDate(event.getEventDate());
        }
        if (event.getLat() != null) {
            updatedEvent.setLat(event.getLat());
        }
        if (event.getLon() != null) {
            updatedEvent.setLat(event.getLat());
        }
        if (event.getLat() != null) {
            updatedEvent.setLon(event.getLon());
        }
        if (event.getPaid() != null) {
            updatedEvent.setPaid(event.getPaid());
        }
        if (event.getParticipantLimit() != null) {
            updatedEvent.setParticipantLimit(event.getParticipantLimit());
        }
        if (event.getRequestModeration() != null) {
            updatedEvent.setRequestModeration(event.getRequestModeration());
        }
        if (event.getTitle() != null) {
            updatedEvent.setTitle(event.getTitle());
        }
        if (actionState == ActionState.PUBLISH_EVENT) {
            //проверить что событие еще не опубликовано
            if (updatedEvent.getState() != EventState.PENDING) {
                throw new ConflictException("Событие можно публиковать, только если оно в состоянии ожидания публикации");
            }
            updatedEvent.setState(EventState.PUBLISHED);
        } else if (actionState == ActionState.REJECT_EVENT) {
            //проверить что событие еще не опубликовано
            if (updatedEvent.getState() == EventState.PUBLISHED) {
                throw new ConflictException("событие можно отклонить, только если оно еще не опубликовано");
            }
            updatedEvent.setState(EventState.CANCELED);
            updatedEvent.setRequestModeration(true);
        }
        Category category = categoryRepository.findById(updatedEvent.getCategory()).orElseThrow(NotFoundException::new);
        User user = userRepository.findById(updatedEvent.getInitiator()).orElseThrow(NotFoundException::new);
        Event savedEvent = eventRepository.save(updatedEvent);
        return MappingEvent.mapToEventFullDto(savedEvent, category, user);
    }

    @Override
    public List<EventShortDto> getEventsPublic(String text,
                                               List<Integer> categories,
                                               Boolean paid,
                                               String rangeStart,
                                               String rangeEnd,
                                               Boolean onlyAvailable,
                                               SortState sort,
                                               Integer from,
                                               Integer size,
                                               HttpServletRequest request) {
        //проверка на корректность категорий
        if (categories != null && categories.size() > 0) {
            for (int category : categories) {
                categoryRepository.findById(category).orElseThrow(ValidationException::new);
            }
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        List<Predicate> params = new ArrayList<>();
        if (text != null) {
            params.add(cb.or(cb.like(root.get("annotation"), "%" + text + "%"),
                             cb.like(root.get("description"), "%" + text + "%")));
        }
        if (categories != null && categories.size() > 0) {
            Expression<Integer> categoryExp = root.get("category");
            params.add(categoryExp.in(categories));
        }
        if (paid != null) {
            params.add(cb.equal(root.get("paid"), paid));
        }
        if (rangeStart != null) {
            params.add(cb.greaterThanOrEqualTo(root.get("eventDate"), LocalDateTime.parse(rangeStart, DTF)));
        }
        if (rangeEnd != null) {
            params.add(cb.lessThanOrEqualTo(root.get("eventDate"), LocalDateTime.parse(rangeEnd, DTF)));
        }
        if (onlyAvailable) {
            params.add(cb.lessThan(root.get("confirmedRequests"), root.get("participantLimit")));
        }
        params.add(cb.equal(root.get("state"), EventState.PUBLISHED));
        cq.where(cb.and(params.toArray(new Predicate[params.size()])));
        if (sort == SortState.EVENT_DATE) {
            cq.orderBy(cb.asc(root.get("eventDate")));
        } else if (sort == SortState.VIEWS) {
            cq.orderBy(cb.desc(root.get(("views"))));
        } else {
            cq.orderBy(cb.asc(root.get(("id"))));
        }
        cq.select(root);
        List<Event> cdEventList = em.createQuery(cq).setFirstResult(from).setMaxResults(size).getResultList();
        //сохранить в сервер статистики
        log.info("Получен запрос в сервис получения событий для публичного API");
        HitDto hit = new HitDto();
        hit.setApp("ewm-main-service");
        hit.setUri(request.getRequestURI());
        hit.setTimestamp(DTF.format(LocalDateTime.now()));
        hit.setIp(request.getRemoteAddr());
        statClient.createHit(hit);
        List<EventShortDto> eventSDList = new ArrayList<>();
        for (Event event : cdEventList) {
            User user = userRepository.findById(event.getInitiator()).orElseThrow(NotFoundException::new);
            Category category = categoryRepository.findById(event.getCategory()).orElseThrow(NotFoundException::new);
            eventSDList.add(MappingEvent.mapToEventShortDto(event, category, user));
        }
        return eventSDList;
    }

    @Override
    public EventFullDto getEventByIdPublic(int id, HttpServletRequest request) {
        log.info("Получен запрос в сервис получения события по ИД для публичного API");
        HitDto hit = new HitDto();
        hit.setApp("ewm-main-service");
        hit.setUri(request.getRequestURI());
        hit.setTimestamp(DTF.format(LocalDateTime.now()));
        hit.setIp(request.getRemoteAddr());
        statClient.createHit(hit);
        Event event = eventRepository.findEventByIdAndState(id, EventState.PUBLISHED);
        if (event == null) {
            throw new NotFoundException();
        }
        String[] uris = new String[1];
        uris[0] = hit.getUri();
        ResponseEntity<StatDto[]> response = statClient.getStat(event.getCreatedOn().minusYears(1L).format(DTF),
                LocalDateTime.now().plusYears(1L).format(DTF),
                uris,
                true);
        StatDto[] objectList = response.getBody();
        StatDto statDto = objectList[0];
        Long hits = statDto.getHits();
        event.setViews(hits);
        eventRepository.save(event);
        User user = userRepository.findById(event.getInitiator()).orElseThrow(NotFoundException::new);
        Category category = categoryRepository.findById(event.getCategory()).orElseThrow(NotFoundException::new);
        return MappingEvent.mapToEventFullDto(event, category, user);
    }
}
