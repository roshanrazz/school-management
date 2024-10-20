package com.roshan.assignment_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class AssignmentDto {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    @Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters")
    private String description;

    @Min(value = 1, message = "Grade must be greater than or equal to 1")
    @Max(value = 12, message = "Grade must be less than or equal to 12")
    private Integer grade;

    private LocalDate startDate;
    private LocalDate endDate;
}
