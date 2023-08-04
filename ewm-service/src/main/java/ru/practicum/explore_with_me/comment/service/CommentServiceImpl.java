package ru.practicum.explore_with_me.comment.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.comment.dto.CommentDto;
import ru.practicum.explore_with_me.comment.dto.NewCommentDto;
import ru.practicum.explore_with_me.comment.dto.UpdateCommentDto;
import ru.practicum.explore_with_me.comment.mapper.CommentMapper;
import ru.practicum.explore_with_me.comment.model.Comment;
import ru.practicum.explore_with_me.comment.repository.CommentRepository;
import ru.practicum.explore_with_me.event.dto.State;
import ru.practicum.explore_with_me.event.model.Event;
import ru.practicum.explore_with_me.event.repository.EventRepository;
import ru.practicum.explore_with_me.exception.NoFoundException;
import ru.practicum.explore_with_me.exception.ValidateDataException;
import ru.practicum.explore_with_me.user.model.User;
import ru.practicum.explore_with_me.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;


    @Override
    public CommentDto createComment(long userId, NewCommentDto newCommentDto) {
        Event event = checkEvent(newCommentDto.getEventId());
        User user = checkUser(userId);
        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidateDataException("Нельзя комментировать свое событие");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ValidateDataException("Можно комментировать события только в статусе PUBLISHED");
        }
        Comment comment = commentMapper.toCommentModel(newCommentDto, event, user);
        CommentDto commentDto = commentMapper.toCommentDto(commentRepository.save(comment));
        log.info("Новый комментарий по событию id={} добавлен", event.getId());
        return commentDto;
    }

    @Override
    public CommentDto updateComment(long userId, long commentId, UpdateCommentDto updateCommentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NoFoundException(String.format("Комментария с id=%d не найдено", commentId)));
        checkUser(userId);
        if (comment.getUser().getId() != userId) {
            throw new ValidateDataException(String.format("Пользователь id=%d не создавал комментарий id=%d ",
                    userId, commentId));
        }
        comment.setText(updateCommentDto.getText());
        CommentDto commentDto = commentMapper.toCommentDto(commentRepository.save(comment));
        log.info("Комментарий commentId={} обновлен", commentId);
        return commentDto;
    }

    @Override
    public void deleteComment(long userId, long commentId) {
        checkUser(userId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NoFoundException(String.format("Комментария с id=%d не найдено", commentId)));
        if (comment.getUser().getId() != userId) {
            throw new ValidateDataException(String.format("Пользователь id=%d не создавал комментарий id=%d ",
                    userId, commentId));
        }
        commentRepository.deleteById(commentId);
        log.info("Комментарий commentId={} удален", commentId);
    }

    @Transactional(readOnly=true)
    @Override
    public List<CommentDto> getCommentsByEventId(Long eventId, int from, int size) {
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(from / size, size, sortById);

        Event event = checkEvent(eventId);

        if (event.getState().equals(State.PENDING)) {
            throw new ValidateDataException("Нельзя получить комментарии к событию в статусе - Ожидание");
        }
        List<Comment> comments = commentRepository.findAllByEvent_Id(eventId, pageable);
        return commentMapper.toCommentDtoList(comments);
    }

    @Transactional(readOnly=true)
    @Override
    public CommentDto getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NoFoundException(String.format("Комментария с id=%d не найдено", commentId)));
        Event event = checkEvent(comment.getEvent().getId());
        if (event.getState().equals(State.PENDING)) {
            throw new ValidateDataException("Нельзя получить комментарии к событию в статусе - Ожидание");
        }
        CommentDto commentDto = commentMapper.toCommentDto(comment);
        log.info("Администратором получен комментарий commentId={} ", commentId);
        return commentDto;
    }


    @Override
    public void deleteCommentById(long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NoFoundException(String.format("Комментария с id=%d не найдено", commentId)));
        commentRepository.deleteById(commentId);
        log.info("Комментарий commentId={} удален администратором", commentId);

    }


    private User checkUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NoFoundException("Пользователь не найден"));
    }


    private Event checkEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NoFoundException("Событие не найдено"));
    }
}
