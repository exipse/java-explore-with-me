package ru.practicum.explore_with_me.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.InvalidParameterException;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler({InvalidParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final Exception e) {
        log.error(e.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse dbconstrainException(final DBConstraintException e) {
        log.error(e.getMessage());
        return new ErrorResponse(HttpStatus.CONFLICT.getReasonPhrase(), e.getMessage());
    }
}
