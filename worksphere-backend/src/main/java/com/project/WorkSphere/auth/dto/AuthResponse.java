package com.project.WorkSphere.auth.dto;

import java.util.Set;

import lombok.Data;

@Data
public class AuthResponse {

    private String accessToken;
    private String tokenType;
    private String email;
    private Set<String> roles;

    public AuthResponse(
            String accessToken,
            String email,
            Set<String> roles) {

        this.accessToken = accessToken;
        this.tokenType = "Bearer";
        this.email = email;
        this.roles = roles;
    }

}
