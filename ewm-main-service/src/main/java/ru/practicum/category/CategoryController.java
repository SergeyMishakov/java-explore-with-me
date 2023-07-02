package ru.practicum.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //API Категории для администратора
    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto add(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Получен запрос создания категории");
        return MappingCategory.mapToCategoryDto(
                categoryService.createCategory(MappingCategory.mapToCategory(newCategoryDto)));
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto change(@PathVariable int catId,
                              @Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Получен запрос изменения категории");
        return MappingCategory.mapToCategoryDto(
                categoryService.changeCategory(catId, MappingCategory.mapToCategory(newCategoryDto)));
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int catId) {
        log.info("Получен запрос удаления категории");
        categoryService.deleteCategory(catId);
    }


    //API Категории публичное
    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                           @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос на получения всех категорий");
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable int catId) {
        log.info("Получен запрос на получение статистики");
        return MappingCategory.mapToCategoryDto(categoryService.getCategoryById(catId));
    }
}
