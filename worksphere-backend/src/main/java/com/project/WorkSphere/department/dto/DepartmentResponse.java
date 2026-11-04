package com.project.WorkSphere.department.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DepartmentResponse {

    private Long id;
    private String name;
    private String description;
    private boolean active;
    private LocalDateTime createdAt;

    public DepartmentResponse() {
    }

    public DepartmentResponse(
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
