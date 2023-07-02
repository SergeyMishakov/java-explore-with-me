package ru.practicum.event;

import ru.practicum.enums.ActionState;
import ru.practicum.enums.EventState;
import ru.practicum.enums.SortState;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    //Закрытый API События для авторизованных пользователей
    EventFullDto createEvent(int userId, Event event);

    List<EventShortDto> getEvents(int userId, Integer from, Integer size);

    EventFullDto getEventById(int userId, int eventId);

    EventFullDto changeEventByUser(int userId, int eventId, ActionState actionState, Event event);

    //API События для админа
    List<EventFullDto> getEventsAdmin(List<Integer> users,
                                        List<EventState> states,
                                        List<Integer> categories,
                                        String rangeStart,
                                        String rangeEnd,
                                        Integer from,
                                        Integer size);

    EventFullDto changeEventByAdmin(int eventId, ActionState actionState, Event event);

    //Публичный API События
    List<EventShortDto> getEventsPublic(String text,
                                        List<Integer> categories,
                                        Boolean paid,
                                        String rangeStart,
                                        String rangeEnd,
                                        Boolean onlyAvailable,
                                        SortState sort,
                                        Integer from,
                                        Integer size,
                                        HttpServletRequest request);

    EventFullDto getEventByIdPublic(int id, HttpServletRequest request);
}
