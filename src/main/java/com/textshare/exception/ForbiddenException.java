package com.textshare.exception;

public class ForbiddenException extends RuntimeException {

    public static final String ERROR_CODE = "FORBIDDEN";
    public static final String MESSAGE_KEY = "textshare.error.text.forbidden";

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException() {
        super(MESSAGE_KEY);
    }
}
