package ru.practicum.explore_with_me.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.comment.dto.CommentDto;
import ru.practicum.explore_with_me.comment.dto.NewCommentDto;
import ru.practicum.explore_with_me.comment.dto.UpdateCommentDto;
import ru.practicum.explore_with_me.comment.service.CommentService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@Validated
@Slf4j
@RequestMapping("/comments/users/{userId}")
public class PrivateCommentController {

    private final CommentService commentService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable Long userId, @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("POST /comment/user/{}", userId);
        return commentService.createComment(userId, newCommentDto);
    }


    //Обновление комментария по событию
    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long commentId,
                                    @RequestBody UpdateCommentDto updateCommentDto) {
        log.info("PATCH /comment/user/{}/{}", userId, commentId);
        return commentService.updateComment(userId, commentId, updateCommentDto);
    }


    //Удаление комментария по событию
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info("DELETE /comment/user/{}/{}", userId, commentId);
        commentService.deleteComment(userId, commentId);
    }
}

