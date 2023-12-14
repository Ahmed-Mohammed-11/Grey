package com.software.grey.exceptions.exceptions;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String message) {
        super(message);
    }
}
