package com.roshan.assignment_service.exception;

import com.roshan.assignment_service.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AssignmentNotFoundException.class)
    public ResponseEntity<ApiResponse> handleAssignmentNotFoundException(AssignmentNotFoundException ex) {
        return new ResponseEntity<>(ApiResponse.builder()
                .success(false)
                .code(404)
                .message(ex.getMessage())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(ApiResponse.builder()
                .success(false)
                .code(400)
                .message("Validation failed")
                .data(errors)
                .build(), HttpStatus.BAD_REQUEST);
    }

}
