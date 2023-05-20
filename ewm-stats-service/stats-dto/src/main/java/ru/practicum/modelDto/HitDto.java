package ru.practicum.modelDto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HitDto {
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
