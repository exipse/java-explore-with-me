package ru.practicum.explore_with_me.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.comment.dto.CommentDto;
import ru.practicum.explore_with_me.comment.service.CommentService;

import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@RestController
@Validated
@Slf4j
@RequestMapping("/admin/comment/{commentId}")
public class CommentControllerAdmin {
    private final CommentService commentService;


    @GetMapping
    public CommentDto getCommentById(@PathVariable @Positive Long commentId) {
        log.info("GET /admin/comment/{}", commentId);
        return commentService.getCommentById(commentId);
    }

    @DeleteMapping
    public void deleteCommentById(@PathVariable long commentId) {
        log.info("DELETE /admin/comment/{}", commentId);
        commentService.deleteCommentById(commentId);
    }
}


