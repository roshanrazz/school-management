package com.roshan.user_service.service.impl;

import com.roshan.user_service.dto.*;
import com.roshan.user_service.entity.User;
import com.roshan.user_service.entity.UserRole;
import com.roshan.user_service.exception.UserNotFoundException;
import com.roshan.user_service.repository.UserRepository;
import com.roshan.user_service.service.AssignmentFeignClient;
import com.roshan.user_service.service.UserService;
import com.roshan.user_service.utils.ApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AssignmentFeignClient assignmentFeignClient;

    @Override
    public UserResponseDto createAdmin(UserRequestDto userRequestDto) {
        User admin = modelMapper.map(userRequestDto, User.class);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRole(UserRole.ROLE_ADMIN);
        return modelMapper.map(userRepository.save(admin), UserResponseDto.class);
    }

    @Override
    public UserResponseDto createTeacher(TeacherRequestDto teacherRequestDto) {
        User teacher = modelMapper.map(teacherRequestDto, User.class);
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacher.setRole(UserRole.ROLE_TEACHER);
        return modelMapper.map(userRepository.save(teacher), UserResponseDto.class);
    }

    @Override
    public UserResponseDto createStudent(StudentRequestDto studentRequestDto) {
        User student = modelMapper.map(studentRequestDto, User.class);
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setRole(UserRole.ROLE_STUDENT);
        return modelMapper.map(userRepository.save(student), UserResponseDto.class);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = findUserById(id);
        UserResponseDto userResponse = modelMapper.map(user, UserResponseDto.class);
        if (userResponse.getGrade() != null) {
            userResponse.setAssignments(getAssignmentsByGrade(userResponse.getGrade()));
        }
        return userResponse;
    }


    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        Type listType = new TypeToken<List<UserResponseDto>>() {
        }.getType();
        return modelMapper.map(users, listType);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        User user = findUserById(id);
        if (userRequestDto.getName() != null) {
            user.setName(userRequestDto.getName());
        }
        if (userRequestDto.getPhone() != null) {
            user.setPhone(userRequestDto.getPhone());
        }
        if (userRequestDto.getAddress() != null) {
            user.setAddress(userRequestDto.getAddress());
        }
        if (userRequestDto.getEmail() != null) {
            user.setEmail(userRequestDto.getEmail());
        }
        if (userRequestDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        }

        userRepository.save(user);
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public void deleteUser(Long id) {
        User user = findUserById(id);
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public List<UserResponseDto> getUsersByGrade(Integer grade) {
        List<User> users = userRepository.findByGrade(grade);
        Type listType = new TypeToken<List<UserResponseDto>>() {
        }.getType();
        return modelMapper.map(users, listType);
    }

    private User findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (user.isDeleted())
            throw new UserNotFoundException("User not found");
        return user;
    }

    private List<AssignmentDto> getAssignmentsByGrade(Integer grade) {
        ResponseEntity<ApiResponse> response = assignmentFeignClient.getAssignmentByGrade(grade);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().isSuccess()) {
            List<AssignmentDto> assignments = (List<AssignmentDto>) response.getBody().getData();
            return assignments;
        } else {
            return List.of();
        }
    }
}
