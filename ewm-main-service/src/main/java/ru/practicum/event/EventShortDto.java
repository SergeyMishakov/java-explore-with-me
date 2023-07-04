package ru.practicum.event;

import lombok.Data;
import ru.practicum.category.CategoryDto;
import ru.practicum.user.UserShortDto;

@Data
public class EventShortDto {

    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    private String eventDate;
    private int id;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private long views;
}
