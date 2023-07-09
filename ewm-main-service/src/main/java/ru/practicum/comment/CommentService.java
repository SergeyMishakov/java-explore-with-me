package ru.practicum.comment;

public interface CommentService {
    CommentDto addComment(Comment comment);

    CommentDto updateComment(Comment comment);

    void deleteComment(Integer userId, Integer commentId);
}
