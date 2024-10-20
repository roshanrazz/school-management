package com.roshan.assignment_service.repository;

import com.roshan.assignment_service.entity.Assignment;
import com.roshan.assignment_service.entity.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment,Long> {
    @Query("select a from Assignment a where a.status = :status")
    List<Assignment> findAllByStatus(AssignmentStatus status);

    List<Assignment> findAllByGrade(Integer grade);

}
