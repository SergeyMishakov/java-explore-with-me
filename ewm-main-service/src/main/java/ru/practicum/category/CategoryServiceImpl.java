package ru.practicum.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Category createCategory(Category category) {
        log.info("Получен запрос в сервис создания категории");
        Optional<Category> optCategory = categoryRepository.getCategoryByName(category.getName());
        if (optCategory.isPresent()) {
            throw new ConflictException("Категория с таким наименованием уже существует");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category changeCategory(int catId, Category category) {
        log.info("Получен запрос в сервис изменения категории");
        categoryRepository.findById(catId).orElseThrow(NotFoundException::new);
        Optional<Category> optCategory = categoryRepository.getCategoryByName(category.getName());
        if (optCategory.isPresent() && optCategory.get().getId() != catId) {
            throw new ConflictException("Категория с таким наименованием уже существует");
        }
        category.setId(catId);
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(int catId) {
        log.info("Получен запрос в сервис удаления категории");
        //проверить что категория не используется
        Optional<Event> optEvent = eventRepository.findFirstByCategory(catId);
        if (optEvent.isPresent()) {
            throw new ConflictException("Существуют события с данной категорией");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        List<Category> catList = categoryRepository.getCategories(size, from);
        List<CategoryDto> catDtoList = new ArrayList<>();
        for (Category category : catList) {
            CategoryDto categoryDto = MappingCategory.mapToCategoryDto(category);
            catDtoList.add(categoryDto);
        }
        return catDtoList;
    }

    @Override
    public Category getCategoryById(int catId) {
        return categoryRepository.findById(catId).orElseThrow(NotFoundException::new);
    }
}
