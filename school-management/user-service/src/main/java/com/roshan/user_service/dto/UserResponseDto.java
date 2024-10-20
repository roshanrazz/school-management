package com.roshan.user_service.dto;

import com.roshan.user_service.entity.UserRole;
import lombok.Data;

import java.util.List;

@Data
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private UserRole role;
    private String phone;
    private String address;
    private String subject;
    private Integer grade;
    List<AssignmentDto> assignments;
}
