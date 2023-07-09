package ru.practicum.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping
@Slf4j
@Validated
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/users/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable Integer userId,
                                 @PathVariable Integer eventId,
                                 @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("Получен запрос добавления комментария к событию");
        return commentService.addComment(MappingComment.mapToComment(eventId, userId, newCommentDto));
    }

    @PatchMapping("/users/{userId}/events/{eventId}/comments")
    public CommentDto updateComment(@PathVariable Integer userId,
                                 @PathVariable Integer eventId,
                                 @Valid @RequestBody UpdateCommentDto updateCommentDto) {
        log.info("Получен запрос редактирования комментария к событию");
        return commentService.updateComment(MappingComment.mapToComment(eventId, userId, updateCommentDto));
    }

    @DeleteMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Integer userId,
                              @PathVariable Integer commentId) {
        log.info("Получен запрос удаления комментария к событию");
        commentService.deleteComment(userId, commentId);
    }
}
