package ru.practicum.event;

import lombok.Data;
import ru.practicum.enums.ActionState;
import javax.validation.constraints.Size;

@Data
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000)
    private String annotation;
    private Integer category;
    @Size(min = 20, max = 7000)
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private ActionState stateAction;
    @Size(min = 3, max = 120)
    private String title;
}
