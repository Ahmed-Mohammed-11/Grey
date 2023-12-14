package com.software.grey.exceptions.exception_handlers;


import com.software.grey.exceptions.exceptions.FailedToUpdateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FailedToUpdateExceptionHandler {
    @ExceptionHandler(FailedToUpdateException.class)
    @ResponseBody
    ResponseEntity<String> onDuplicateDataException(FailedToUpdateException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
