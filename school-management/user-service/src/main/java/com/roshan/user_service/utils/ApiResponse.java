package com.roshan.user_service.utils;

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
