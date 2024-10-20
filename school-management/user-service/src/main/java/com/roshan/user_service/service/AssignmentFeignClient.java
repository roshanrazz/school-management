package com.roshan.user_service.service;

import com.roshan.user_service.utils.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "assignment-service")
public interface AssignmentFeignClient {

    @GetMapping("/api/v1/assignments/grade/{grade}")
    public ResponseEntity<ApiResponse> getAssignmentByGrade(@PathVariable Integer grade);
}
