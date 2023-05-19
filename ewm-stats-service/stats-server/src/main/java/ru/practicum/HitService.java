package ru.practicum;

import ru.practicum.model.Hit;
import ru.practicum.modelDto.StatDto;
import java.time.LocalDateTime;
import java.util.List;

public interface HitService {

    Hit createHit(Hit hit);

    List<StatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
