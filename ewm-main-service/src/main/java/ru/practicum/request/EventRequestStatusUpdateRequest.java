package ru.practicum.request;

import lombok.Data;
import ru.practicum.enums.RequestState;
import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {

    private List<Integer> requestIds;
    private RequestState status;

}
