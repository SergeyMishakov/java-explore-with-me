package ru.practicum.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(value = "select * from categories c " +
            "order by id " +
            "limit ?1 OFFSET ?2 ",
            nativeQuery = true)
    List<Category> getCategories(Integer size, Integer from);

    Optional<Category> getCategoryByName(String name);
}
