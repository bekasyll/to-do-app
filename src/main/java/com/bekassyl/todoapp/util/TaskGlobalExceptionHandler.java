package com.bekassyl.todoapp.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class TaskGlobalExceptionHandler {
    @ExceptionHandler(TaskNotCreatedException.class)
    public ResponseEntity<TaskErrorResponse> handlingTaskNotCreatedException(TaskNotCreatedException e) {
        TaskErrorResponse response = new TaskErrorResponse(e.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<TaskErrorResponse> handlingTaskNotFoundException(TaskNotFoundException e) {
        TaskErrorResponse response = new TaskErrorResponse(e.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AppUserNotFoundException.class)
    public ResponseEntity<TaskErrorResponse> handlingUserNotFoundException(AppUserNotFoundException e) {
        TaskErrorResponse response = new TaskErrorResponse(e.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
