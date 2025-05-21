package tech.stl.hcm.candidate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "An unexpected error occurred");
        body.put("error", ex.getMessage());
        body.put("path", request.getDescription(false));
        
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public final ResponseEntity<Object> handleOptimisticLockingFailure(
            OptimisticLockingFailureException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Concurrent modification detected");
        body.put("error", ex.getMessage());
        body.put("path", request.getDescription(false));
        
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFound(
            ResourceNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Resource not found");
        body.put("error", ex.getMessage());
        body.put("path", request.getDescription(false));
        
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
} 