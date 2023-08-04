package ru.practicum.explore_with_me.comment.service;

import ru.practicum.explore_with_me.comment.dto.CommentDto;
import ru.practicum.explore_with_me.comment.dto.NewCommentDto;
import ru.practicum.explore_with_me.comment.dto.UpdateCommentDto;

import javax.validation.Valid;
import java.util.List;

public interface CommentService {

    /**
     * Создание коментария по событию
     */
    CommentDto createComment(long userId, @Valid NewCommentDto newCommentDto);

    /**
     * Изменение коментария по событию
     */
    CommentDto updateComment(long userId, long commentId, UpdateCommentDto updateCommentDto);

    /**
     * Удаление пользователем своего коментария по событию
     */
    void deleteComment(long userId, long commentId);


    /**
     * Получение комментариев по событию
     */
    List<CommentDto> getCommentsByEventId(Long eventId, int from, int size);


    /**
     * Получение любого коментария администратором по id
     */
    CommentDto getCommentById(Long commentId);

    /**
     * Удаление любого коментария администратором по id
     **/
    void deleteCommentById(long commentId);


}
