package com.company.employee_management_system.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEmployeeNotFound(EmployeeNotFoundException ex) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                fieldError -> errors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, String>> handleBadRequest(Exception ex) {
        if (ex instanceof IllegalArgumentException && ex.getMessage() != null) {
            String message = ex.getMessage();
            String normalized = message.toLowerCase(java.util.Locale.ROOT);
            if (normalized.contains("not found") || normalized.contains("does not exist")) {
                return error(HttpStatus.NOT_FOUND, message);
            }
            if (normalized.contains("already assigned") || normalized.contains("already registered")) {
                return error(HttpStatus.CONFLICT, message);
            }
            return error(HttpStatus.BAD_REQUEST, message);
        }
        return error(HttpStatus.BAD_REQUEST,
                "Invalid request data. Check the request body and parameter types.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleConflict(DataIntegrityViolationException ex) {
        return error(HttpStatus.CONFLICT,
                "The request conflicts with existing data. Check unique fields and related records.");
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        String message = ex.getMessage() == null ? "" : ex.getMessage();
        String normalized = message.toLowerCase(java.util.Locale.ROOT);

        if (normalized.contains("not found") || normalized.contains("does not exist")
                || normalized.contains("not exist")) {
            return error(HttpStatus.NOT_FOUND, message);
        }

        if (normalized.contains("already assigned") || normalized.contains("already registered")) {
            return error(HttpStatus.CONFLICT, message);
        }

        if (normalized.contains("cannot be before") || normalized.contains("must be ")
                || normalized.contains("is required")) {
            return error(HttpStatus.BAD_REQUEST, message);
        }

        return error(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected server error occurred. Check the application logs.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleUnexpected(Exception ex) {
        // Avoid leaking SQL, class names, or internal stack details to API clients.
        return error(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected server error occurred. Check the application logs.");
    }

    private ResponseEntity<Map<String, String>> error(HttpStatus status, String message) {
        Map<String, String> body = new HashMap<>();
        body.put("error", message == null || message.isBlank() ? status.getReasonPhrase() : message);
        return ResponseEntity.status(status).body(body);
    }
}
