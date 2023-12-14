package com.software.grey.exceptions;

public class UserReportedPostBeforeException extends RuntimeException {
    public UserReportedPostBeforeException(String message) {
        super(message);
    }
}
