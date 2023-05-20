package ru.practicum.statClient.modelDto;

import lombok.Data;

@Data
public class Stat {

    private String app;
    private String uri;
    private Long hits;
}
