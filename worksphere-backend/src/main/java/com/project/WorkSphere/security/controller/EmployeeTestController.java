package com.project.WorkSphere.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeTestController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER', 'EMPLOYEE')")
    public Map<String, String> employeeDashboard() {

        return Map.of(
            "message", "Welcome to WorkSphere!",
            "access", "EMPLOYEE ACCESS"
        );
    }
}
