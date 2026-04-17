package com.fixit.user.domain.exceptions;

public class TaskMustBeAssignedToStartException extends RuntimeException {
    public TaskMustBeAssignedToStartException(String message) {
        super(message);
    }
}
