package com.abinav.webapplication.exception;

import com.abinav.webapplication.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * Centralized exception handler for the entire application
 * Handles all exceptions and returns standardized error responses
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

        /**
         * Handle ResourceNotFoundException
         */
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
                        ResourceNotFoundException ex,
                        WebRequest request) {

                log.warn("Resource not found: {}", ex.getMessage());

                ErrorResponse error = ErrorResponse.builder()
                                .status(HttpStatus.NOT_FOUND.value())
                                .message(ex.getMessage())
                                .error("Resource Not Found")
                                .path(request.getDescription(false).replace("uri=", ""))
                                .timestamp(LocalDateTime.now())
                                .build();

                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        /**
         * Handle AuthenticationException
         */
        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<ErrorResponse> handleAuthenticationException(
                        AuthenticationException ex,
                        WebRequest request) {

                log.warn("Authentication failed: {}", ex.getMessage());

                ErrorResponse error = ErrorResponse.builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message(ex.getMessage())
                                .error("Authentication Failed")
                                .path(request.getDescription(false).replace("uri=", ""))
                                .timestamp(LocalDateTime.now())
                                .build();

                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        /**
         * Handle UnauthorizedException
         */
        @ExceptionHandler(UnauthorizedException.class)
        public ResponseEntity<ErrorResponse> handleUnauthorizedException(
                        UnauthorizedException ex,
                        WebRequest request) {

                log.warn("Unauthorized access: {}", ex.getMessage());

                ErrorResponse error = ErrorResponse.builder()
                                .status(HttpStatus.FORBIDDEN.value())
                                .message(ex.getMessage())
                                .error("Access Denied")
                                .path(request.getDescription(false).replace("uri=", ""))
                                .timestamp(LocalDateTime.now())
                                .build();

                return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
        }

        /**
         * Handle ValidationException
         */
        @ExceptionHandler(ValidationException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(
                        ValidationException ex,
                        WebRequest request) {

                log.warn("Validation error: {}", ex.getMessage());

                ErrorResponse error = ErrorResponse.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message(ex.getMessage())
                                .error("Validation Error")
                                .path(request.getDescription(false).replace("uri=", ""))
                                .timestamp(LocalDateTime.now())
                                .build();

                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        /**
         * Handle IllegalArgumentException and IllegalStateException
         */
        @ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class })
        public ResponseEntity<ErrorResponse> handleIllegalException(
                        RuntimeException ex,
                        WebRequest request) {

                log.warn("Illegal operation: {}", ex.getMessage());

                ErrorResponse error = ErrorResponse.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message(ex.getMessage())
                                .error("Invalid Operation")
                                .path(request.getDescription(false).replace("uri=", ""))
                                .timestamp(LocalDateTime.now())
                                .build();

                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        /**
         * Handle Spring Security UsernameNotFoundException
         */
        @ExceptionHandler(UsernameNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(
                        UsernameNotFoundException ex,
                        WebRequest request) {

                log.warn("User not found: {}", ex.getMessage());

                ErrorResponse error = ErrorResponse.builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid username or password")
                                .error("Authentication Failed")
                                .path(request.getDescription(false).replace("uri=", ""))
                                .timestamp(LocalDateTime.now())
                                .build();

                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        /**
         * Handle Spring Security BadCredentialsException
         */
        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorResponse> handleBadCredentialsException(
                        BadCredentialsException ex,
                        WebRequest request) {

                log.warn("Bad credentials provided");

                ErrorResponse error = ErrorResponse.builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid username or password")
                                .error("Authentication Failed")
                                .path(request.getDescription(false).replace("uri=", ""))
                                .timestamp(LocalDateTime.now())
                                .build();

                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        /**
         * Handle all other generic exceptions
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGlobalException(
                        Exception ex,
                        WebRequest request) {

                log.error("An unexpected error occurred", ex);

                ErrorResponse error = ErrorResponse.builder()
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .message("An unexpected error occurred. Please try again later.")
                                .error("Internal Server Error")
                                .path(request.getDescription(false).replace("uri=", ""))
                                .timestamp(LocalDateTime.now())
                                .build();

                return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
}
