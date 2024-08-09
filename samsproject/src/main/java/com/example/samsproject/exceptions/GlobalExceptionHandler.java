package com.example.samsproject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ParentException.class)
    public ResponseEntity<ApiError> handleParentException(ParentException ex) {
        ApiError error = new ApiError(400,"Enter valid input !!!!");
        return new ResponseEntity<ApiError>(error, HttpStatus.NOT_FOUND);
    }
}
