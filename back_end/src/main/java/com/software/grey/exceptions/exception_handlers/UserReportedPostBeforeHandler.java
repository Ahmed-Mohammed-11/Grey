package com.software.grey.exceptions.exception_handlers;

import com.software.grey.exceptions.exceptions.UserReportedPostBeforeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserReportedPostBeforeHandler {
    @ExceptionHandler(UserReportedPostBeforeException.class)
    public ResponseEntity<String> handleUserExistsException(UserReportedPostBeforeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post already reported");
    }

}
