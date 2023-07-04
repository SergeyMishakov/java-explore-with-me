package ru.practicum.request;

import java.util.List;

public interface RequestService {

    ParticipationRequest createRequest(int userId, int eventId);

    List<ParticipationRequestDto> getRequestsByUser(int userId);

    ParticipationRequest cancelRequest(int userId, int requestId);

    List<ParticipationRequestDto> getRequest(int userId, int eventId);

    EventRequestStatusUpdateResult updateRequests(int userId,
                                                  int eventId,
                                                  EventRequestStatusUpdateRequest requestList);
}
