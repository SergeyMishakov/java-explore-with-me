package ru.practicum.statClient.modelDto;

import com.sun.istack.NotNull;
import lombok.Data;
import javax.validation.constraints.Size;

@Data
public class Hit {

    private Long id;
    @NotNull
    @Size(max = 100)
    private String app;
    @NotNull
    @Size(max = 100)
    private String uri;
    @NotNull
    @Size(max = 40)
    private String ip;
    @NotNull
    private String timestamp;
}
