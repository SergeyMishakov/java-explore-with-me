package ru.practicum.comment;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class NewCommentDto {
    @NotBlank
    @Size(min = 3, max = 100)
    private String text;
}
