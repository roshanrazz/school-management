package com.roshan.user_service.controller;


import com.roshan.user_service.dto.*;
import com.roshan.user_service.service.AuthService;
import com.roshan.user_service.service.UserService;
import com.roshan.user_service.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Login successful")
                    .code(200)
                    .data(authService.login(loginDto))
                    .build());
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.builder()
                    .success(false)
                    .code(401)
                    .message(e.getMessage())
                    .build(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/register/admin")
    public ResponseEntity<ApiResponse> createAdmin(@Valid @RequestBody UserRequestDto userRequestDto) {
        return new ResponseEntity<>(ApiResponse.builder()
                .success(true)
                .code(201)
                .message("Admin added successfully")
                .data(userService.createAdmin(userRequestDto))
                .build(), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/register/teacher")
    public ResponseEntity<ApiResponse> createTeacher(@Valid @RequestBody TeacherRequestDto teacherRequestDto) {
        return new ResponseEntity<>(ApiResponse.builder()
                .success(true)
                .code(201)
                .message("Teacher added successfully")
                .data(userService.createTeacher(teacherRequestDto))
                .build(), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/register/student")
    public ResponseEntity<ApiResponse> createStudent(@Valid @RequestBody StudentRequestDto studentRequestDto) {
        return new ResponseEntity<>(ApiResponse.builder()
                .success(true)
                .code(201)
                .message("Student added successfully")
                .data(userService.createStudent(studentRequestDto))
                .build(), HttpStatus.CREATED);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<ApiResponse> refreshToken(@Valid @RequestBody RefreshTokenDto refreshToken) {
        try {
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .code(200)
                    .message("Token refreshed successfully")
                    .data(authService.refreshToken(refreshToken))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .success(false)
                    .code(403)
                    .message(e.getMessage())
                    .build());
        }
    }
}
