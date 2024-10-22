package com.roshan.user_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentRequestDto extends UserRequestDto{
    @NotNull(message = "Grade is required")
    @Min(value = 1, message = "Grade must be greater than or equal to 1")
    @Max(value = 12, message = "Grade must be less than or equal to 12")
    private Integer grade;
}
