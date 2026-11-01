
package com.project.WorkSphere.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping
    public Map<String, String> testConnection() {

        return Map.of(
            "message", "React connected successfully with Spring Boot!",
            "application", "WorkSphere",
            "status", "SUCCESS"
        );
    }
}
