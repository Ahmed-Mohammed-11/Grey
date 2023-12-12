package com.software.grey.exceptions.exception_handlers;


import com.software.grey.exceptions.exceptions.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class DataNotFoundExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    Map<String, String> onDataNotFoundException(DataNotFoundException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", exception.getMessage());
        return error;
    }
}
