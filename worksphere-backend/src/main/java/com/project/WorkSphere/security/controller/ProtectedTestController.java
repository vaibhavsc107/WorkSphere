package com.project.WorkSphere.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/protected")
public class ProtectedTestController {

    @GetMapping("/profile")
    public Map<String, String> profile() {

        return Map.of(
            "message", "JWT authentication successful!",
            "status", "AUTHORIZED"
        );
    }
}
