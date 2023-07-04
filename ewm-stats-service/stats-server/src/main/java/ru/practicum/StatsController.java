package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.MappingHit;
import ru.practicum.modelDto.HitDto;
import ru.practicum.modelDto.StatDto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
@Validated
public class StatsController {

    private final HitService hitService;

    @Autowired
    public StatsController(HitService hitService) {
        this.hitService = hitService;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto create(@RequestBody HitDto hit) {
        log.info("Пришел запрос создания записи через сервер");
        return MappingHit.mapToHitDto(hitService.createHit(MappingHit.mapToHit(hit)));
    }

    @GetMapping("/stats")
    public List<StatDto> getStats(@RequestParam String start,
                            @RequestParam String end,
                                  @RequestParam(required = false) String[] uris,
                            @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Получен запрос на получение статистики");
        List<String> urisList = new ArrayList<>();
        if (uris != null) {
            urisList = Arrays.asList(uris);
        }
        return hitService.getStats(LocalDateTime.parse(start, DTF), LocalDateTime.parse(end, DTF), urisList, unique);
    }

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
