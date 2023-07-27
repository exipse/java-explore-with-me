package ru.practicum.explore_with_me.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

        @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        return new ApiError("Произошла непредвиденная ошибка",
                    "Internal Server Error", "INTERNAL_SERVER_ERROR)");
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError noCorrectValidate(final MissingServletRequestParameterException e) {
        log.debug("Ошибка MissingServletRequestParameterException. {}", e.getMessage());
        return new ApiError(e.getMessage(), "Incorrectly made request.", "BAD_REQUEST");
    }

    @ExceptionHandler(NoFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError noFound(final NoFoundException e) {
        log.debug("Ошибка NoFoundException. {}", e.getMessage());
        return new ApiError(e.getMessage(), "Not Found", "NOT_FOUND");
    }

    @ExceptionHandler({ConstraintViolationException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError noFound(final ConstraintViolationException e) {
        log.debug("Ошибка ConstraintViolationException. {}", e.getMessage());
        return new ApiError(e.getMessage(), "Conflict", "CONFLICT");
    }

    @ExceptionHandler(ValidateDataException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError noFound(final ValidateDataException e) {
        log.debug("Ошибка ValidateDataException. {}", e.getMessage());
        return new ApiError(e.getMessage(), "Conflict", "CONFLICT");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError illegalArgumentException(final IllegalArgumentException e) {
        log.debug("Ошибка illegalArgumentException. {}", e.getMessage());
        return new ApiError(e.getMessage(), "Bad_request", "BAD_REQUEST");
    }
}
