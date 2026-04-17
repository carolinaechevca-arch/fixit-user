package com.fixit.user.domain.exceptions;

public class TaskMustBeProgressToStartException extends RuntimeException {
    public TaskMustBeProgressToStartException(String message) {
        super(message);
    }
}
