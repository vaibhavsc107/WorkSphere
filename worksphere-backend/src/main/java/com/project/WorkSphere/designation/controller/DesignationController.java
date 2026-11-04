package com.project.WorkSphere.designation.controller;

import com.project.WorkSphere.designation.dto.DesignationRequest;
import com.project.WorkSphere.designation.dto.DesignationResponse;
import com.project.WorkSphere.designation.service.DesignationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/designations")
public class DesignationController {

    private final DesignationService designationService;

    public DesignationController(DesignationService designationService) {
        this.designationService = designationService;
    }

    // Create designation
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER')")
    public ResponseEntity<DesignationResponse> createDesignation(
            @Valid @RequestBody DesignationRequest request) {

        DesignationResponse response =
                designationService.createDesignation(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get all designations
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<DesignationResponse>> getAllDesignations() {

        return ResponseEntity.ok(
                designationService.getAllDesignations()
        );
    }

    // Get active designations
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<DesignationResponse>> getActiveDesignations() {

        return ResponseEntity.ok(
                designationService.getActiveDesignations()
        );
    }

    // Get designation by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER', 'EMPLOYEE')")
    public ResponseEntity<DesignationResponse> getDesignationById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                designationService.getDesignationById(id)
        );
    }

    // Update designation
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER')")
    public ResponseEntity<DesignationResponse> updateDesignation(
            @PathVariable Long id,
            @Valid @RequestBody DesignationRequest request) {

        return ResponseEntity.ok(
                designationService.updateDesignation(id, request)
        );
    }

    // Deactivate designation
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER')")
    public ResponseEntity<Void> deactivateDesignation(
            @PathVariable Long id) {

        designationService.deactivateDesignation(id);

        return ResponseEntity.noContent().build();
    }
}
