package ru.practicum.explore_with_me.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDto {

    public static final String DateTime = "yyyy-MM-dd HH:mm:ss";

    private Long id;

    private String text;

    private Long user;

    private Long event;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTime)
    private LocalDateTime created;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTime)
    private LocalDateTime updated;
}
