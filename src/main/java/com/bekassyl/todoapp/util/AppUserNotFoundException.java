package com.bekassyl.todoapp.util;

public class AppUserNotFoundException extends RuntimeException {
    private String message;

    public AppUserNotFoundException(String message) {
        super(message);
    }
}
