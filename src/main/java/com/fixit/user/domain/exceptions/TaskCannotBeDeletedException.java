package com.fixit.user.domain.exceptions;

public class TaskCannotBeDeletedException extends RuntimeException {
    public TaskCannotBeDeletedException(String message) {
        super(message);
    }
}
