package com.project.WorkSphere.auth.dto;

public record RefreshResult(
        AuthResponse authResponse,
        String refreshToken
) {
}