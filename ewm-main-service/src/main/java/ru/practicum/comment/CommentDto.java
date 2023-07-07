package ru.practicum.comment;

import lombok.Data;

@Data
public class CommentDto {
    private int id;
    private String text;
    private int event;
    private String authorName;
    private String created;
}
