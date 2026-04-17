package com.fixit.user.domain.exceptions;

public class TaskAlreadyHasPriorityException extends RuntimeException {
    public TaskAlreadyHasPriorityException(String message, Long id) {
        super(message);
    }
}
