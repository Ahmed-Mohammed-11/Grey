package com.software.grey.utils;

public class ErrorMessages {

    public static final String POST_LENGTH_LIMIT = "Post length must be at least 1 character and less than 5000";
    public static final String POST_REPORTED_BEFORE = "Post already reported";
    public static final String INVALID_REQUEST_BODY = "Invalid request body";
    public static final String INVALID_EMAIL = "Invalid email";
    public static final String INVALID_PASSWORD = "Invalid password";
    public static final String INVALID_USERNAME = "Invalid username";
    public static final String USERNAME_EXISTS = "Username already exists";
    public static final String EMAIL_EXISTS = "Email already exists";
    public static final String POST_NOT_FOUND = "Post not found";
    public static final String POST_ALREADY_DELETED = "Post already deleted";
    public static final String INVALID_POST_ID = "Invalid post id";

    private ErrorMessages() {
    }
}
