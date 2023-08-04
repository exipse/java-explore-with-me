package ru.practicum.explore_with_me.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.comment.dto.CommentDto;
import ru.practicum.explore_with_me.comment.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@Slf4j
@RequestMapping("/comment/event/{eventId}")
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getCommentsByEventId(@PathVariable @Positive Long eventId,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                 @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("POST /comment/event/{}", eventId);
        List<CommentDto> result = commentService.getCommentsByEventId(eventId, from, size);
        return result;
    }
}
