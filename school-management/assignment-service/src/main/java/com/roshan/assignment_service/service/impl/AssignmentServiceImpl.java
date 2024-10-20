package com.roshan.assignment_service.service.impl;

import com.roshan.assignment_service.dto.AssignmentDto;
import com.roshan.assignment_service.entity.Assignment;
import com.roshan.assignment_service.entity.AssignmentStatus;
import com.roshan.assignment_service.exception.AssignmentNotFoundException;
import com.roshan.assignment_service.repository.AssignmentRepository;
import com.roshan.assignment_service.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public Assignment createAssignment(AssignmentDto assignmentDto, String email) {

        Assignment assignment = new Assignment();
        assignment.setTitle(assignmentDto.getTitle());
        assignment.setDescription(assignmentDto.getDescription());
        assignment.setAssignedBy(email);
        assignment.setGrade(assignmentDto.getGrade());
        assignment.setStartDate(assignmentDto.getStartDate());
        assignment.setEndDate(assignmentDto.getEndDate());
        assignment.setStatus(AssignmentStatus.PENDING);

        String notificationMessage = "Assignment " + assignment.getTitle() + " has been created for Grade " +
                assignment.getGrade() + " by " + email;
        kafkaTemplate.send("assignment-notification", notificationMessage);
        return assignmentRepository.save(assignment);
    }

    @Override
    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id).orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));
    }

    @Override
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    @Override
    public List<Assignment> getAllAssignmentsByStatus(AssignmentStatus status) {
        return assignmentRepository.findAllByStatus(status);
    }

    @Override
    public List<Assignment> getAllAssignmentsByGrade(Integer grade) {
        return assignmentRepository.findAllByGrade(grade);
    }

    @Override
    public void deleteAssignment(Long id) {
        Assignment assignment = getAssignmentById(id);
        assignmentRepository.delete(assignment);
    }
}
