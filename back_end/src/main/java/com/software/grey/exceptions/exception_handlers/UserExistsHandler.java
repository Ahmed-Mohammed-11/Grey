package com.software.grey.exceptions.exception_handlers;

import com.software.grey.exceptions.exceptions.UserExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExistsHandler {
    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<String> handleUserExistsException(UserExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
    }
}
