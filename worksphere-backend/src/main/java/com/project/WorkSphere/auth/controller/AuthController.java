package com.project.WorkSphere.auth.controller;


import com.project.WorkSphere.auth.dto.RegisterRequest;
import com.project.WorkSphere.auth.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.WorkSphere.auth.dto.AuthResponse;
import com.project.WorkSphere.auth.dto.LoginRequest;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }     
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @Valid @RequestBody RegisterRequest request) {

        try {
            String message = authService.register(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("message", message));

        } catch (IllegalArgumentException ex) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message", ex.getMessage()));
        }
    }
    

@PostMapping("/login")
public ResponseEntity<?> login(
        @Valid @RequestBody LoginRequest request) {

    try {
        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);

    } catch (org.springframework.security.core.AuthenticationException ex) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid email or password"));
    }
}

}
