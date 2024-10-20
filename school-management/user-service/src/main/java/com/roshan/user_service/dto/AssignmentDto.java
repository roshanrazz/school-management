package com.roshan.user_service.dto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Data
public class AssignmentDto {
    private Long id;

    private String title;
    private String description;

    private String assignedBy;
    private Integer grade;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private AssignmentStatus status;
}

