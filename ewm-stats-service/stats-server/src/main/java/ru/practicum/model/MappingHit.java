package ru.practicum.model;

import ru.practicum.modelDto.HitDto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MappingHit {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Hit mapToHit(HitDto hitDto) {
        Hit hit = new Hit();
        hit.setApp(hitDto.getApp());
        hit.setIp(hitDto.getIp());
        hit.setUri(hitDto.getUri());
        hit.setTimestamp(LocalDateTime.parse(hitDto.getTimestamp(), DTF));
        return hit;
    }

    public static HitDto mapToHitDto(Hit hit) {
        HitDto hitDto = new HitDto();
        hitDto.setId(hit.getId());
        hitDto.setApp(hit.getApp());
        hitDto.setIp(hit.getIp());
        hitDto.setUri(hit.getUri());
        hitDto.setTimestamp(hit.getTimestamp().format(DTF));
        return hitDto;
    }
}
