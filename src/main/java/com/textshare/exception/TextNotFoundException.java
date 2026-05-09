package com.textshare.exception;

public class TextNotFoundException extends RuntimeException {

    public static final String ERROR_CODE = "TEXT_NOT_FOUND";
    public static final String MESSAGE_KEY = "textshare.error.text.notFound";

    public TextNotFoundException(String message) {
        super(message);
    }

    public TextNotFoundException() {
        super(MESSAGE_KEY);
    }
}
