package com.project.WorkSphere.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminTestController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Map<String, String> adminDashboard() {

        return Map.of(
            "message", "Welcome Super Admin!",
            "access", "FULL ACCESS"
        );
    }
}
