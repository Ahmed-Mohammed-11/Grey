package com.software.grey.exceptions.exception_handlers;

import com.software.grey.exceptions.exceptions.PostNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PostNotFoundHandler {
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<String> handlePostNotFoundException(PostNotFoundException postNotFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(postNotFoundException.getMessage());
    }
}
