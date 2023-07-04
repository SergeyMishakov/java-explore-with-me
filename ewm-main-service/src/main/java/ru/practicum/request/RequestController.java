package ru.practicum.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
@Validated
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }


    //API Пользователя для запросов
    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable int userId, @RequestParam int eventId) {
        log.info("Получен запрос создания запроса на участие");
        return MappingRequest.mapToDto(requestService.createRequest(userId, eventId));
    }

    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDto> getRequestsByUser(@PathVariable int userId) {
        log.info("Получен запрос получения списка запросов пользователя");
        return requestService.getRequestsByUser(userId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable int userId, @PathVariable int requestId) {
        log.info("Получен запрос отмены запроса пользователем");
        return MappingRequest.mapToDto(requestService.cancelRequest(userId, requestId));
    }

    //API Приватный для запросов
    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequest(@PathVariable int userId, @PathVariable int eventId) {
        log.info("Получен запрос получения запросов пользователя по событию");
        return requestService.getRequest(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult getRequest(@PathVariable int userId,
                                                     @PathVariable int eventId,
                                                     @RequestBody EventRequestStatusUpdateRequest requestList) {
        log.info("Получен запрос обработки запросов по событию");
        return requestService.updateRequests(userId, eventId, requestList);
    }
}
