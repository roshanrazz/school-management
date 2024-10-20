package com.roshan.user_service.service;

import com.roshan.user_service.dto.StudentRequestDto;
import com.roshan.user_service.dto.TeacherRequestDto;
import com.roshan.user_service.dto.UserRequestDto;
import com.roshan.user_service.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto createAdmin(UserRequestDto userRequestDto);
    UserResponseDto createTeacher(TeacherRequestDto teacherRequestDto);
    UserResponseDto createStudent(StudentRequestDto studentRequestDto);

    UserResponseDto getUserById(Long id);

    List<UserResponseDto> getAllUsers();

    UserResponseDto updateUser(Long id, UserRequestDto userRequestDto);

    void deleteUser(Long id);

    List<UserResponseDto> getUsersByGrade(Integer grade);
}
