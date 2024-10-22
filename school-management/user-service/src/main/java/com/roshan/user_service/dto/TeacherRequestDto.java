package com.roshan.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherRequestDto extends UserRequestDto{
    @NotBlank(message = "Subject is required")
    @Size(min = 3, max = 40, message = "Subject must be between 3 and 40 characters")
    private String subject;
}
