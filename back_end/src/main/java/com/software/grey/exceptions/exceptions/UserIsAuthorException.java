package com.software.grey.exceptions.exceptions;

public class UserIsAuthorException extends RuntimeException {
    public UserIsAuthorException(String message) {
        super(message);
    }
}
