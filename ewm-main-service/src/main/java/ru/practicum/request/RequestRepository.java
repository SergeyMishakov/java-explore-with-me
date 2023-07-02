package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Integer> {

    List<ParticipationRequest> findByRequester(int userId);

    List<ParticipationRequest> findByEvent(int eventId);

    Optional<ParticipationRequest> findByRequesterAndEvent(int userId, int eventId);

}
