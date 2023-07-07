package ru.practicum.explore_with_me.exception;

public class DBConstraintException extends  RuntimeException{

    public DBConstraintException(String message) {
        super(message);
    }
}
