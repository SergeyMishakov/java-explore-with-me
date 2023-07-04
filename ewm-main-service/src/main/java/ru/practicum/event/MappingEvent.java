package ru.practicum.event;

import ru.practicum.category.Category;
import ru.practicum.category.MappingCategory;
import ru.practicum.user.MappingUser;
import ru.practicum.user.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MappingEvent {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event mapToEvent(NewEventDto newEventDto) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(newEventDto.getCategory());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), DTF));
        event.setLat(newEventDto.getLocation().getLat());
        event.setLon(newEventDto.getLocation().getLon());
        if (newEventDto.getPaid() != null) {
            event.setPaid(newEventDto.getPaid());
        } else {
            event.setPaid(false);
        }
        if (newEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(newEventDto.getParticipantLimit());
        } else {
            event.setParticipantLimit(0);
        }
        if (newEventDto.getRequestModeration() != null) {
            event.setRequestModeration(newEventDto.getRequestModeration());
        } else {
            event.setRequestModeration(true);
        }
        event.setTitle(newEventDto.getTitle());
        event.setPublishedOn(LocalDateTime.of(1990, 1, 1, 1, 0, 0));
        return event;
    }

    public static EventFullDto mapToEventFullDto(Event event, Category category, User initiator) {
        EventFullDto eventFD = new EventFullDto();
        eventFD.setAnnotation(event.getAnnotation());
        eventFD.setCategory(MappingCategory.mapToCategoryDto(category));
        eventFD.setConfirmedRequests(event.getConfirmedRequests());
        eventFD.setCreatedOn(event.getCreatedOn().format(DTF));
        eventFD.setDescription(event.getDescription());
        eventFD.setEventDate(event.getEventDate().format(DTF));
        eventFD.setId(event.getId());
        eventFD.setInitiator(MappingUser.mapToUserShortDto(initiator));
        Location location = new Location();
        location.setLat(event.getLat());
        location.setLon(event.getLon());
        eventFD.setLocation(location);
        eventFD.setPaid(event.getPaid());
        eventFD.setParticipantLimit(event.getParticipantLimit());
        eventFD.setPublishedOn(event.getPublishedOn().format(DTF));
        eventFD.setRequestModeration(event.getRequestModeration());
        eventFD.setState(event.getState());
        eventFD.setTitle(event.getTitle());
        eventFD.setViews(event.getViews());
        return eventFD;
    }

    public static EventShortDto mapToEventShortDto(Event event, Category category, User initiator) {
        EventShortDto eventSD = new EventShortDto();
        eventSD.setAnnotation(event.getAnnotation());
        eventSD.setCategory(MappingCategory.mapToCategoryDto(category));
        eventSD.setConfirmedRequests(event.getConfirmedRequests());
        eventSD.setEventDate(event.getEventDate().format(DTF));
        eventSD.setId(event.getId());
        eventSD.setInitiator(MappingUser.mapToUserShortDto(initiator));
        eventSD.setPaid(event.getPaid());
        eventSD.setTitle(event.getTitle());
        eventSD.setViews(event.getViews());
        return eventSD;
    }

    public static Event mapToEvent(UpdateEventAdminRequest updateEAR) {
        Event event = new Event();
        event.setAnnotation(updateEAR.getAnnotation());
        event.setCategory(updateEAR.getCategory());
        event.setDescription(updateEAR.getDescription());
        if (updateEAR.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(updateEAR.getEventDate(), DTF));
        }
        if (updateEAR.getLocation() != null) {
            event.setLat(updateEAR.getLocation().getLat());
            event.setLon(updateEAR.getLocation().getLon());
        }
        event.setPaid(updateEAR.getPaid());
        event.setParticipantLimit(updateEAR.getParticipantLimit());
        event.setRequestModeration(updateEAR.getRequestModeration());
        event.setTitle(updateEAR.getTitle());
        return event;
    }

    public static Event mapToEvent(UpdateEventUserRequest updateEUR) {
        Event event = new Event();
        event.setAnnotation(updateEUR.getAnnotation());
        event.setCategory(updateEUR.getCategory());
        event.setDescription(updateEUR.getDescription());
        if (updateEUR.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(updateEUR.getEventDate(), DTF));
        }
        if (updateEUR.getLocation() != null) {
            event.setLat(updateEUR.getLocation().getLat());
            event.setLon(updateEUR.getLocation().getLon());
        }
        event.setPaid(updateEUR.getPaid());
        event.setParticipantLimit(updateEUR.getParticipantLimit());
        event.setRequestModeration(updateEUR.getRequestModeration());
        event.setTitle(updateEUR.getTitle());
        return event;
    }
}
