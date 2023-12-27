package com.software.grey.exceptions.exception_handlers;

import com.software.grey.exceptions.exceptions.UserIsAuthorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserIsAuthorExceptionHandler {

    @ExceptionHandler(UserIsAuthorException.class)
    @ResponseBody
    ResponseEntity<String> handleSavePostException(UserIsAuthorException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
