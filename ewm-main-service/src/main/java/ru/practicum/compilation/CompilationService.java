package ru.practicum.compilation;

import java.util.List;

public interface CompilationService {

    //API Подборки для администратора
    CompilationDto createCompilation(NewCompilationDto compilation);

    void deleteCompilation(int compId);

    CompilationDto updateCompilation(int compId, UpdateCompilationRequest compilation);

    //API Публичная Подборки
    List<CompilationDto> getCompList(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompById(int compId);
}
