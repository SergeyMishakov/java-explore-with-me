package ru.practicum.compilation;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class NewCompilationDto {

    private Set<Integer> events;
    @Value("false")
    private Boolean pinned;
    @NotBlank
    @Size(min = 3, max = 50)
    private String title;
}
