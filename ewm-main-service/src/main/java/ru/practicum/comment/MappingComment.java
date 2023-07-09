package ru.practicum.comment;

import ru.practicum.user.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MappingComment {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Comment mapToComment(Integer eventId, Integer userId, NewCommentDto newCommentDto) {
        Comment comment = new Comment();
        comment.setEvent(eventId);
        comment.setAuthor(userId);
        comment.setText(newCommentDto.getText());
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

    public static CommentDto mapToCommentDto(Comment comment, User user) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setCreated(comment.getCreated().format(DTF));
        commentDto.setAuthorName(user.getName());
        commentDto.setEvent(comment.getEvent());
        return commentDto;
    }

    public static Comment mapToComment(Integer eventId, Integer userId, UpdateCommentDto updateCommentDto) {
        Comment comment = new Comment();
        comment.setId(updateCommentDto.getId());
        comment.setEvent(eventId);
        comment.setAuthor(userId);
        comment.setText(updateCommentDto.getText());
        comment.setCreated(LocalDateTime.now());
        return comment;
    }
}
