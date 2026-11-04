package com.project.WorkSphere.designation.dto;

import java.time.LocalDateTime;

public class DesignationResponse {

    private Long id;
    private String name;
    private String description;
    private boolean active;
    private LocalDateTime createdAt;

    public DesignationResponse() {
    }

    public DesignationResponse(
            Long id,
            String name,
            String description,
            boolean active,
            LocalDateTime createdAt) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
