package ru.practicum.explore_with_me.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

//        @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ApiError handleThrowable(final Throwable e) {
//        return new ApiError("Произошла непредвиденная ошибка",
//                    "Internal Server Error", "INTERNAL_SERVER_ERROR)");
//    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError noCorrectValidate(final MissingServletRequestParameterException e) {
        return new ApiError(e.getMessage(), "Incorrectly made request.", "BAD_REQUEST");
    }

    @ExceptionHandler(NoFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError noFound(final NoFoundException e) {
        return new ApiError(e.getMessage(), "Not Found", "NOT_FOUND");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError noFound(final ConstraintViolationException e) {
        return new ApiError(e.getMessage(), "Conflict", "CONFLICT");
    }

    @ExceptionHandler(ValidateDataException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError noFound(final ValidateDataException e) {
        return new ApiError(e.getMessage(), "Conflict", "CONFLICT");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError illegalArgumentException(final IllegalArgumentException e) {
        return new ApiError(e.getMessage(), "Bad_request", "BAD_REQUEST");
    }
}
