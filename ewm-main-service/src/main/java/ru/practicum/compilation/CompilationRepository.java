package ru.practicum.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    @Query(value = "select * from compilations c " +
            "order by id " +
            "limit ?1 OFFSET ?2",
            nativeQuery = true)
    List<Compilation> getCompList(Integer size, Integer from);

    @Query(value = "select * from compilations c " +
            "where pinned = ?1 " +
            "order by id " +
            "limit ?2 OFFSET ?3",
            nativeQuery = true)
    List<Compilation> getCompListWithPinned(Boolean pinned, Integer size, Integer from);
}
