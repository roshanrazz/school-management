package com.roshan.assignment_service.service;

import com.roshan.assignment_service.dto.AssignmentDto;
import com.roshan.assignment_service.entity.Assignment;
import com.roshan.assignment_service.entity.AssignmentStatus;

import java.util.List;

public interface AssignmentService {
    Assignment createAssignment(AssignmentDto assignmentDto,String email);

    Assignment getAssignmentById(Long id);

    List<Assignment> getAllAssignments();

    List<Assignment> getAllAssignmentsByStatus(AssignmentStatus status);

    List<Assignment> getAllAssignmentsByGrade(Integer grade);

    void deleteAssignment(Long id);
}
