package com.roshan.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDto {
    @NotBlank(message = "Email is required")
    @Email(regexp = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$" ,message = "Email is not valid")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    private String password;
}
