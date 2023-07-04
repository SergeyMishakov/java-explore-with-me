package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.model.Hit;
import ru.practicum.modelDto.StatDto;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HitServiceImpl implements HitService {

    private final HitRepository hitRepository;

    @Autowired
    public HitServiceImpl(HitRepository hitRepository) {
        this.hitRepository = hitRepository;
    }

    @Override
    public Hit createHit(Hit hit) {
        log.info("Получен запрос записи в статистику");
        return hitRepository.save(hit);
    }

    @Override
    public List<StatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        log.info("Получен запрос получения статитики");
        //проверить что дата начала раньше даты окончания
        if (start.isAfter(end)) {
            throw new ValidationException();
        }
        List<StatDto> statDtoList = new ArrayList<>();
        List<Object[]> hitList;
        if (unique) {
            if (uris.isEmpty()) {
                hitList = hitRepository.getStatsUniqueHitsWithoutUris(start, end);
            } else {
                hitList = hitRepository.getStatsUniqueHits(start, end, uris);
            }
        } else {
            if (uris.isEmpty()) {
                hitList = hitRepository.getStatsWithoutUris(start, end);
            } else {
                hitList = hitRepository.getStats(start, end, uris);
            }
        }
        for (Object[] stat : hitList) {
            String app = (String) stat[0];
            String uri = (String) stat[1];
            Long hits = ((BigInteger) stat[2]).longValue();
            StatDto statDto = new StatDto(app, uri, hits);
            statDtoList.add(statDto);
        }
        log.info("Запрос статистики успешно обработан, отправляется ответ");
        return statDtoList;
    }
}
