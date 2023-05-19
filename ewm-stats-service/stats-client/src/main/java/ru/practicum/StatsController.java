package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.modelDto.HitDto;
import java.time.LocalDateTime;

@RestController
@RequestMapping
@Slf4j
@Validated
public class StatsController {

    private final StatClient statClient;

    @Autowired
    public StatsController(StatClient statClient) {
        this.statClient = statClient;
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> create(@RequestBody HitDto hit) {
        log.info("Пришел запрос создание записи через клиент");
        return statClient.createHit(hit);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam LocalDateTime start,
                                           @RequestParam LocalDateTime end,
                                           @RequestParam(required = false) String uris,
                                           @RequestParam(defaultValue = "false") boolean unique) {
        return statClient.getStat(start, end, uris, unique);
    }
}
