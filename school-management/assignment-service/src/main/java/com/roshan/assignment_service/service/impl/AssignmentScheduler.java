package com.roshan.assignment_service.service.impl;

import com.roshan.assignment_service.entity.Assignment;
import com.roshan.assignment_service.entity.AssignmentStatus;
import com.roshan.assignment_service.repository.AssignmentRepository;
import com.roshan.assignment_service.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class AssignmentScheduler {

    private final AssignmentService assignmentService;
    private final AssignmentRepository assignmentRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateAssignmentStatus() {
        List<Assignment> pendingAssignments = assignmentService.getAllAssignmentsByStatus(AssignmentStatus.PENDING);
        pendingAssignments.forEach(assignment -> {
            if (assignment.getStartDate().isEqual(LocalDate.now())) {
                log.info("Assignment activated: {}", assignment.getId());
                assignment.setStatus(AssignmentStatus.ACTIVE);
                assignmentRepository.save(assignment);
            }
        });

        List<Assignment> activeAssignments = assignmentService.getAllAssignmentsByStatus(AssignmentStatus.ACTIVE);
        activeAssignments.forEach(assignment -> {
            if (assignment.getEndDate().isBefore(LocalDate.now())) {
                log.info("Assignment expired: {}", assignment.getId());
                assignment.setStatus(AssignmentStatus.EXPIRED);
                assignmentRepository.save(assignment);
            }
        });
    }
}
