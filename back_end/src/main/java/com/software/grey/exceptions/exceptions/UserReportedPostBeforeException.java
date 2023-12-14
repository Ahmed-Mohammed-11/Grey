package com.software.grey.exceptions.exceptions;

public class UserReportedPostBeforeException extends RuntimeException {
    public UserReportedPostBeforeException(String message) {
        super(message);
    }
}
