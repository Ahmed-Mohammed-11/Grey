package com.software.grey.exceptions.exception_handlers;

import com.software.grey.exceptions.exceptions.UserNotAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserNotAuthorizedHandler {
    @ExceptionHandler(UserNotAuthorizedException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotAuthorizedException userNotAuthorizedException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userNotAuthorizedException.getMessage());
    }
}
