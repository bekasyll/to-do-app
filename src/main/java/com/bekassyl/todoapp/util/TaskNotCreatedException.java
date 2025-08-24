package com.bekassyl.todoapp.util;

public class TaskNotCreatedException extends RuntimeException {
    private String message;

    public TaskNotCreatedException(String message) {
        super(message);
    }
}
