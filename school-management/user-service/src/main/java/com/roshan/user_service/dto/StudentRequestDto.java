package com.roshan.user_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentRequestDto extends UserRequestDto{
    private Integer grade;
}
