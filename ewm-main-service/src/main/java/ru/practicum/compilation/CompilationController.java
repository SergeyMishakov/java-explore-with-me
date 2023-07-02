package ru.practicum.compilation;

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
public class CompilationController {

    private final CompilationService compilationService;

    @Autowired
    public CompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    //API Подборки для администратора
    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Получен запрос создания подборки");
        return compilationService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int compId) {
        log.info("Получен запрос удаления подборки");
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto update(@PathVariable int compId,
                                 @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("Получен запрос обновления подборки");
        return compilationService.updateCompilation(compId, updateCompilationRequest);
    }

    //API Публичная Подборки
    @GetMapping("/compilations")
    public List<CompilationDto> getCompList(@RequestParam (required = false) Boolean pinned,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                            @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос получения списка подборок");
        return compilationService.getCompList(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable int compId) {
        log.info("Получен запрос получения подборки по ID");
        return compilationService.getCompById(compId);
    }
}
