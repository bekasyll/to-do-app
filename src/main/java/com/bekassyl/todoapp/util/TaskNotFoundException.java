package com.bekassyl.todoapp.util;

public class TaskNotFoundException extends RuntimeException {
    private String message;

    public TaskNotFoundException(String message) {
        super(message);
    }
}
