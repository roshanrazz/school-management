package com.roshan.assignment_service.controller;

import com.roshan.assignment_service.dto.AssignmentDto;
import com.roshan.assignment_service.entity.Assignment;
import com.roshan.assignment_service.entity.AssignmentStatus;
import com.roshan.assignment_service.service.AssignmentService;
import com.roshan.assignment_service.service.impl.RedisService;
import com.roshan.assignment_service.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assignments")
@RequiredArgsConstructor
@Slf4j
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final RedisService redisService;

    @PostMapping
    public ResponseEntity<ApiResponse> createAssignment(@Valid @RequestBody AssignmentDto assignmentDto, @RequestHeader("X-Auth-User") String email, @RequestHeader("X-User-Role") String role) {
        if (!role.equals("ROLE_TEACHER")) {
            return new ResponseEntity<>(ApiResponse.builder().success(false).code(403).message("Access denied").build(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(ApiResponse.builder().success(true).code(201).message("Assignment created successfully").data(assignmentService.createAssignment(assignmentDto, email)).build(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllAssignments(@RequestParam(name = "status", required = false) AssignmentStatus status) {
        if (status != null) {
            return new ResponseEntity<>(ApiResponse.builder().success(true).code(200).message("All assignments fetched successfully").data(assignmentService.getAllAssignmentsByStatus(status)).build(), HttpStatus.OK);
        }
//        Redis Caching
        List<Assignment> redisAssignments = redisService.getList("assignments", Assignment.class);
        if (redisAssignments!=null) {
            log.info("Retrieved assignments from Redis: {}", redisAssignments);
            return new ResponseEntity<>(ApiResponse.builder().success(true).code(200).message("All assignments fetched successfully").data(redisAssignments).build(), HttpStatus.OK);
        }

        List<Assignment> assignments = assignmentService.getAllAssignments();
        redisService.set("assignments", assignments, 300L);
        return new ResponseEntity<>(ApiResponse.builder().success(true).code(200).message("All assignments fetched successfully").data(assignments).build(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAssignmentById(@PathVariable Long id) {
        return new ResponseEntity<>(ApiResponse.builder().success(true).code(200).message("Assignment fetched successfully").data(assignmentService.getAssignmentById(id)).build(), HttpStatus.OK);
    }

    @GetMapping("/grade/{grade}")
    public ResponseEntity<ApiResponse> getAssignmentByGrade(@PathVariable Integer grade) {
        return new ResponseEntity<>(ApiResponse.builder().success(true).code(200).message("Assignment fetched successfully").data(assignmentService.getAllAssignmentsByGrade(grade)).build(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAssignmentById(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return new ResponseEntity<>(ApiResponse.builder().success(true).code(200).message("Assignment deleted successfully").build(), HttpStatus.OK);
    }

}
