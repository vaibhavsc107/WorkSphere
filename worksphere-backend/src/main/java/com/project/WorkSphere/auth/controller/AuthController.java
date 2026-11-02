package com.project.WorkSphere.auth.controller;


import com.project.WorkSphere.auth.dto.RegisterRequest;
import com.project.WorkSphere.auth.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.WorkSphere.auth.dto.AuthResponse;
import com.project.WorkSphere.auth.dto.LoginRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import java.time.Duration;

import com.project.WorkSphere.auth.dto.RefreshResult;
import org.springframework.web.bind.annotation.CookieValue;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(
                    name = "refreshToken",
                    required = false
            ) String refreshToken) {

        if (refreshToken != null && !refreshToken.isBlank()) {
            authService.logout(refreshToken);
        }

        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/api/v1/auth")
                .sameSite("Lax")
                .maxAge(Duration.ZERO)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "Logged out successfully"));
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @CookieValue(
                    name = "refreshToken",
                    required = false
            ) String refreshToken) {

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Refresh token missing"));
        }

        try {
            RefreshResult result = authService.refresh(refreshToken);

            ResponseCookie cookie = ResponseCookie
                    .from("refreshToken", result.refreshToken())
                    .httpOnly(true)
                    .secure(false)
                    .path("/api/v1/auth")
                    .sameSite("Lax")
                    .maxAge(Duration.ofDays(7))
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(result.authResponse());

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired refresh token"));
        }
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
        
        String refreshToken =
                authService.createRefreshToken(response.getEmail());
        
        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // Local HTTP development only
                .path("/api/v1/auth")
                .sameSite("Lax")
                .maxAge(Duration.ofDays(7))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);

    } catch (org.springframework.security.core.AuthenticationException ex) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid email or password"));
    }
}

}
