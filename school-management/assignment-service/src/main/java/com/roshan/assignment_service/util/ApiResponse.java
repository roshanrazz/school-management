package com.roshan.assignment_service.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse {

    private boolean success;
    private int code;
    private String message;
    private Object data;
}
