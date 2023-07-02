package ru.practicum.category;

import java.util.List;

public interface CategoryService {

    Category createCategory(Category category);

    Category changeCategory(int catId, Category category);

    void deleteCategory(int catId);

    List<CategoryDto> getCategories(int from, int size);

    Category getCategoryById(int catId);
}
