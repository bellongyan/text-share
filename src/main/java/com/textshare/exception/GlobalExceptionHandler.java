package com.textshare.exception;

import com.textshare.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(TextNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTextNotFound(TextNotFoundException ex) {
        log.warn("Text not found: {}", ex.getMessage());
        String message = resolveMessage(ex.getMessage(), TextNotFoundException.MESSAGE_KEY);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("TEXT_NOT_FOUND", message));
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleRateLimitExceeded(RateLimitExceededException ex) {
        log.warn("Rate limit exceeded: {}", ex.getMessage());
        String message = resolveMessage(ex.getMessage(), RateLimitExceededException.MESSAGE_KEY);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(ApiResponse.error("RATE_LIMIT_EXCEEDED", message));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<Void>> handleForbidden(ForbiddenException ex) {
        log.warn("Forbidden access: {}", ex.getMessage());
        String message = resolveMessage(ex.getMessage(), ForbiddenException.MESSAGE_KEY);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("FORBIDDEN", message));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(ValidationException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        String message = resolveMessage(ex.getMessage(), ValidationException.MESSAGE_KEY);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("INVALID_CONTENT", message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(this::resolveFieldError)
                .collect(Collectors.joining(", "));
        log.warn("Validation failed: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("INVALID_CONTENT", message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex) {
        log.error("Internal error: ", ex);
        String message = messageSource.getMessage("textshare.error.server.internal", null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("INTERNAL_ERROR", message));
    }

    private String resolveMessage(String message, String defaultKey) {
        if (message == null) {
            return messageSource.getMessage(defaultKey, null, LocaleContextHolder.getLocale());
        }
        if (message.contains(".") && !message.contains(" ")) {
            try {
                return messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
            } catch (Exception e) {
                return message;
            }
        }
        return message;
    }

    private String resolveFieldError(FieldError fieldError) {
        String message = fieldError.getDefaultMessage();
        if (message != null && message.contains(".")) {
            try {
                Object[] args = fieldError.getRejectedValue() != null
                    ? new Object[]{fieldError.getRejectedValue().toString().length()}
                    : new Object[]{50000};
                return messageSource.getMessage(message, args, LocaleContextHolder.getLocale());
            } catch (Exception e) {
                return message;
            }
        }
        return message;
    }
}
