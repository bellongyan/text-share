package com.textshare.exception;

public class TextNotFoundException extends RuntimeException {

    public TextNotFoundException(String message) {
        super(message);
    }
}
