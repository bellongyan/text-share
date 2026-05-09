package com.textshare.exception;

public class RateLimitExceededException extends RuntimeException {

    public static final String ERROR_CODE = "RATE_LIMIT_EXCEEDED";
    public static final String MESSAGE_KEY = "textshare.error.rateLimit.exceeded";

    public RateLimitExceededException(String message) {
        super(message);
    }

    public RateLimitExceededException() {
        super(MESSAGE_KEY);
    }
}
