package ru.practicum.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.enums.EventState;
import ru.practicum.enums.SortState;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
@Validated
public class EventController {

    private final EventService eventService;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    //Закрытый API События для авторизованных пользователей
    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable int userId,
                            @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Получен запрос создания события");
        return eventService.createEvent(userId, MappingEvent.mapToEvent(newEventDto));
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEvents(@PathVariable int userId,
                                   @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                   @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос получения событий");
        return eventService.getEvents(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEventById(@PathVariable int userId, @PathVariable int eventId) {
        log.info("Получен запрос получения события по id");
        return eventService.getEventById(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto changeEventByUser(@PathVariable int userId, @PathVariable int eventId,
                                     @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("Получен запрос изменения событий");
        return eventService.changeEventByUser(userId, eventId, updateEventUserRequest.getStateAction(), MappingEvent.mapToEvent(updateEventUserRequest));
    }

    //API События для админа
    @GetMapping("/admin/events")
    public List<EventFullDto> getEventsAdmin(@RequestParam(required = false) List<Integer> users,
                                         @RequestParam(required = false) List<EventState> states,
                                         @RequestParam(required = false) List<Integer> categories,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                         @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос получения списка событий администратором");
        List<EventFullDto> resultEventDtoList = eventService.getEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
        return resultEventDtoList;
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto changeEventByAdmin(@PathVariable int eventId,
                                          @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Получен запрос изменения события админом");
        return eventService.changeEventByAdmin(eventId, updateEventAdminRequest.getStateAction(), MappingEvent.mapToEvent(updateEventAdminRequest));
    }

    //Публичный API События
    @GetMapping("/events")
    public List<EventShortDto> getEventsPublic(@RequestParam(required = false) String text,
                                              @RequestParam(required = false) List<Integer> categories,
                                              @RequestParam(required = false) Boolean paid,
                                              @RequestParam(required = false) String rangeStart,
                                              @RequestParam(required = false) String rangeEnd,
                                              @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                              @RequestParam(required = false) SortState sort,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                              @Positive @RequestParam(defaultValue = "10") Integer size,
                                               HttpServletRequest request) {
        log.info("Получен запрос получения списка событий для публичного API");
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return eventService.getEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventByIdPublic(@PathVariable int id, HttpServletRequest request) {
        log.info("Получен запрос получения события по id для публичного API");
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return eventService.getEventByIdPublic(id, request);
    }
}
