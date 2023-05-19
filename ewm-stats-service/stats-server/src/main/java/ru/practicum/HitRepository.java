package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Hit;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query(value = "select h.app, h.uri, count(h.ip) " +
            "from hits h " +
            "where h.timestamp between ?1 and ?2 " +
            "and h.uri in (?3) " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc ",
            nativeQuery = true)
    List<Object[]> getStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select h.app, h.uri, count(h.ip) " +
            "from hits h " +
            "where h.timestamp between ?1 and ?2 " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc ",
            nativeQuery = true)
    List<Object[]> getStatsWithoutUris(LocalDateTime start, LocalDateTime end);

    @Query(value = "select h.app, h.uri, count(distinct h.ip) " +
            "from hits h " +
            "where h.timestamp between ?1 and ?2 " +
            "and h.uri in (?3) " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc ",
            nativeQuery = true)
    List<Object[]> getStatsUniqueHits(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select h.app, h.uri, count(distinct h.ip) " +
            "from hits h " +
            "where h.timestamp between ?1 and ?2 " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc ",
            nativeQuery = true)
    List<Object[]> getStatsUniqueHitsWithoutUris(LocalDateTime start, LocalDateTime end);
}
