package com.textshare.exception;

public class ValidationException extends RuntimeException {
    public static final String MESSAGE_KEY = "textshare.error.validation.content.size";

    public ValidationException(String message) {
        super(message);
    }
}