package com.roshan.assignment_service.exception;

public class AssignmentNotFoundException extends RuntimeException{
    public AssignmentNotFoundException(String message) {
        super(message);
    }
}
