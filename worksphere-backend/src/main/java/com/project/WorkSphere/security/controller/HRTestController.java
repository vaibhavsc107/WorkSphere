package com.project.WorkSphere.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/hr")
public class HRTestController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER')")
    public Map<String, String> hrDashboard() {

        return Map.of(
            "message", "Welcome HR Manager!",
            "access", "HR ACCESS"
        );
    }
}
