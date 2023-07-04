package ru.practicum.request;

import lombok.Data;
import ru.practicum.enums.RequestState;

@Data
public class ParticipationRequestDto {

    private String created;
    private int event;
    private int id;
    private int requester;
    private RequestState status;

}
