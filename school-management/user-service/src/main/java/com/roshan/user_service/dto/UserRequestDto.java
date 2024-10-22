package com.roshan.user_service.dto;

import com.roshan.user_service.entity.UserRole;
import io.swagger.v3.oas.models.annotations.OpenAPI30;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UserRequestDto {

    @Size(min = 3, max = 40, message = "Name must be between 3 and 40 characters")
    @NotBlank(message = "Name is required")
    private String name;
    @Email(regexp = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$" ,message = "Email is not valid")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    private String password;
    @Size(min = 8,max = 15 ,message = "Phone number must be between 8 and 15 characters")
    private String phone;
    private String address;
}
