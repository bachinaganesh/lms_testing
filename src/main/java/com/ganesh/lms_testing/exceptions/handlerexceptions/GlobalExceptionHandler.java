package com.ganesh.lms_testing.exceptions.handlerexceptions;

import com.ganesh.lms_testing.dtos.response.APIError;
import com.ganesh.lms_testing.exceptions.NotUpdateableException;
import com.ganesh.lms_testing.exceptions.ResourceAlreadyExistException;
import com.ganesh.lms_testing.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIError> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        APIError apiError = APIError.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIError> handleException(Exception ex, HttpServletRequest request) {
        APIError apiError = APIError.builder()
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<APIError> handleResourceAlreadyExistException(ResourceAlreadyExistException ex, HttpServletRequest request) {
        APIError apiError = APIError.builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        APIError apiError = APIError.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotUpdateableException.class)
    public ResponseEntity<APIError> handleNotUpdateableException(NotUpdateableException ex, HttpServletRequest request) {
        APIError apiError = APIError.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
