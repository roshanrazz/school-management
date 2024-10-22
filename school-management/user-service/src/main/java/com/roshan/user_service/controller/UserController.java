package com.roshan.user_service.controller;

import com.roshan.user_service.dto.UserRequestDto;
import com.roshan.user_service.dto.UserResponseDto;
import com.roshan.user_service.service.UserService;
import com.roshan.user_service.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers() {
        return new ResponseEntity<>(ApiResponse.builder()
                .success(true)
                .code(200)
                .message("Users retrieved successfully")
                .data(userService.getAllUsers()).build(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        UserResponseDto user = userService.getUserById(id);
        if (userDetails.getUsername().equals(user.getEmail())) {
            return new ResponseEntity<>(ApiResponse.builder()
                    .success(true)
                    .code(200)
                    .message("User retrieved successfully")
                    .data(userService.getUserById(id)).build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(ApiResponse.builder()
                .success(false)
                .code(403)
                .message("Access denied. You do not have permission to access this resource.")
                .build(), HttpStatus.FORBIDDEN);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(ApiResponse.builder()
                .success(true)
                .code(200)
                .message("User deleted successfully")
                .build(), HttpStatus.OK);
    }

    @GetMapping("/grade/{grade}")
    public ResponseEntity<ApiResponse> getUsersByGrade(@PathVariable Integer grade) {
        return new ResponseEntity<>(ApiResponse.builder()
                .success(true)
                .code(200)
                .message("Users retrieved successfully")
                .data(userService.getUsersByGrade(grade)).build(), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDto userRequestDto, @AuthenticationPrincipal UserDetails userDetails) {
        UserResponseDto user = userService.getUserById(id);
        if (userDetails.getUsername().equals(user.getEmail())) {
            return new ResponseEntity<>(ApiResponse.builder()
                    .success(true)
                    .code(200)
                    .message("User updated successfully")
                    .data(userService.updateUser(id, userRequestDto)).build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(ApiResponse.builder()
                .success(false)
                .code(403)
                .message("Access denied. You do not have permission to access this resource.")
                .build(), HttpStatus.FORBIDDEN);
    }
}
