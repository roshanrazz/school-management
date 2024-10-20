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
    private String name;
    @Email(message = "Email is not valid")
    private String email;
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
    private String password;
    private String phone;
    private String address;
}
