package com.abinav.webapplication.exception;

/**
 * Exception thrown for invalid input/validation errors
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
