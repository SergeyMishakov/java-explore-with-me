package ru.practicum.event;

import lombok.Data;
import ru.practicum.category.CategoryDto;
import ru.practicum.comment.CommentDto;
import ru.practicum.enums.EventState;
import ru.practicum.user.UserShortDto;
import java.util.List;

@Data
public class EventWithCommentsDto {

    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private int id;
    private UserShortDto initiator;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private String publishedOn;
    private boolean requestModeration;
    private EventState state;
    private String title;
    private long views;
    private List<CommentDto> commentList;
}
