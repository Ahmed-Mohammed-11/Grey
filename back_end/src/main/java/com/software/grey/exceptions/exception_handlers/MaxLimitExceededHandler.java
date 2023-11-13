package com.software.grey.exceptions.exception_handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class MaxLimitExceededHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    Map<String, String> onMaxLimitExceededException(MethodArgumentNotValidException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", exception.getBindingResult().getFieldError().getDefaultMessage());
        return error;
    }
}
