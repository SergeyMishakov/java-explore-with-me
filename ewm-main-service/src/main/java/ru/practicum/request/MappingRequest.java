package ru.practicum.request;

import java.time.format.DateTimeFormatter;

public class MappingRequest {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ParticipationRequestDto mapToDto(ParticipationRequest request) {
        ParticipationRequestDto requestDto = new ParticipationRequestDto();
        requestDto.setCreated(request.getCreated().format(DTF));
        requestDto.setEvent(request.getEvent());
        requestDto.setId(request.getId());
        requestDto.setRequester(request.getRequester());
        requestDto.setStatus(request.getStatus());
        return requestDto;
    }
}
