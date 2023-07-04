package ru.practicum.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select * from users u " +
            "order by id " +
            "limit ?1 OFFSET ?2",
            nativeQuery = true)
    List<User> findUsers(Integer size, Integer from);

    Optional<User> getUserByName(String name);
}
