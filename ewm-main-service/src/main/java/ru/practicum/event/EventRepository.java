package ru.practicum.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.enums.EventState;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>, QuerydslPredicateExecutor<Event> {

    @Query(value = "select * from events c " +
            "where initiator = ?1 " +
            "order by id " +
            "limit ?2 OFFSET ?3",
            nativeQuery = true)
    List<Event> getAllEventsByUser(int userId, Integer size, Integer from);

    Event findEventByIdAndState(Integer id, EventState state);

    Optional<Event> findFirstByCategory(Integer category);

}
