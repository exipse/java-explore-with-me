package ru.practicum.explore_with_me.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class ErrorResponse {
    private final String error;
    private String message;
}

