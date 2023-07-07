package ru.practicum.comment;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.enums.EventState;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CommentServiceImpl(EventRepository eventRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentDto addComment(Comment comment) {
        //комментарии могут оставить только авторизованные пользователи
        //комментарии можно оставлять только к опубликованным событиям
        //комментарии можно оставлять как к прошедшим событиям, так и к будущим событиям (вопросы о предстоящем событии)
        //оставлять комментарии могут все пользователи (не только участвующие пользователи)
        User author = userRepository.findById(comment.getAuthor()).orElseThrow(NotFoundException::new);
        Event event = eventRepository.findById(comment.getEvent()).orElseThrow(NotFoundException::new);
        if (event.getState() != EventState.PUBLISHED) {
            throw new ValidationException();
        }
        Comment savedComment = commentRepository.save(comment);
        return MappingComment.mapToCommentDto(savedComment, author);
    }

    @Override
    public CommentDto updateComment(Comment comment) {
        Comment updatedComment = commentRepository.findById(comment.getId()).orElseThrow(NotFoundException::new);
        User author = userRepository.findById(comment.getAuthor()).orElseThrow(NotFoundException::new);
        eventRepository.findById(comment.getEvent()).orElseThrow(NotFoundException::new);
        //проверить что комментарий оставлен именно к этому событию именно этим пользователем
        if (updatedComment.getAuthor() != comment.getAuthor() || updatedComment.getEvent() != comment.getEvent()) {
            throw new ValidationException();
        }
        updatedComment.setText(comment.getText());
        return MappingComment.mapToCommentDto(commentRepository.save(updatedComment), author);
    }

    @Override
    public void deleteComment(Integer userId, Integer commentId) {
        //проверить наличие пользователя и комментария
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundException::new);
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        //проверить что комментарий принадлежит этому пользователю
        if (comment.getAuthor() != userId) {
            throw new ValidationException();
        }
        commentRepository.deleteById(commentId);
    }
}
