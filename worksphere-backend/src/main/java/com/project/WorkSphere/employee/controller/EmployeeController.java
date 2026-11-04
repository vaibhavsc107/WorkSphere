package com.project.WorkSphere.employee.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;



import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaTypeFactory;

import com.project.WorkSphere.employee.dto.EmployeeProfileUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.project.WorkSphere.employee.dto.EmployeeRequest;
import com.project.WorkSphere.employee.dto.EmployeeResponse;
import com.project.WorkSphere.employee.dto.EmployeeSummaryResponse;
import com.project.WorkSphere.employee.entity.EmploymentStatus;
import com.project.WorkSphere.employee.service.EmployeeService;
import com.project.WorkSphere.storage.service.ProfileImageService;
import com.project.WorkSphere.user.entity.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    
    private final ProfileImageService profileImageService;


    public EmployeeController(EmployeeService employeeService, ProfileImageService profileImageService) {
        this.employeeService = employeeService;
        this.profileImageService = profileImageService;
    }
    
    @DeleteMapping("/me/profile-image")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER', 'EMPLOYEE')")
    public ResponseEntity<Void> removeMyProfileImage(
            @AuthenticationPrincipal User user) {

        employeeService.removeMyProfileImage(user.getEmail());

        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/me/profile-image")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER', 'EMPLOYEE')")
    public ResponseEntity<Resource> getMyProfileImage(
            @AuthenticationPrincipal User user) {

        EmployeeResponse employee =
                employeeService.getMyProfile(user.getEmail());

        if (employee.getProfileImage() == null
                || employee.getProfileImage().isBlank()) {
            return ResponseEntity.notFound().build();
        }

        Resource image = profileImageService.loadProfileImage(
                employee.getProfileImage()
        );

        MediaType mediaType = MediaTypeFactory
                .getMediaType(image)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + employee.getProfileImage() + "\""
                )
                .body(image);
    }
    
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER', 'EMPLOYEE')")
    public ResponseEntity<EmployeeResponse> getMyProfile(
            @AuthenticationPrincipal User user) {

        EmployeeResponse response =
                employeeService.getMyProfile(user.getEmail());

        return ResponseEntity.ok(response);
    }
    
    @PostMapping(value = "/me/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER', 'EMPLOYEE')")
    public ResponseEntity<EmployeeResponse> uploadProfileImage(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file) {

        EmployeeResponse response =
                employeeService.updateProfileImage(
                        user.getEmail(), file
                );

        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER', 'EMPLOYEE')")
    public ResponseEntity<EmployeeResponse> updateMyProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody EmployeeProfileUpdateRequest request) {

        EmployeeResponse response =
                employeeService.updateMyProfile(user.getEmail(), request);

        return ResponseEntity.ok(response);
    }

    // Create employee
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER')")
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeRequest request) {

        EmployeeResponse response =
                employeeService.createEmployee(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }   
    
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER')")
    public ResponseEntity<Page<EmployeeResponse>> searchEmployees(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long designationId,
            @RequestParam(required = false) EmploymentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                employeeService.searchEmployees(
                        search, departmentId, designationId, status, pageable
                )
        );
    }

    // Get all employees
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER')")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {

        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    // Get employee by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER')")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable Long id) {

        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    // Update employee
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER')")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(id, request)
        );
    }

    // Deactivate employee
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER')")
    public ResponseEntity<Void> deactivateEmployee(
            @PathVariable Long id) {

        employeeService.deactivateEmployee(id);

        return ResponseEntity.noContent().build();
    }

    // Get active employees
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER')")
    public ResponseEntity<List<EmployeeResponse>> getActiveEmployees() {

        return ResponseEntity.ok(employeeService.getActiveEmployees());
    }

    // Get employee summaries
    @GetMapping("/summaries")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'HR_MANAGER')")
    public ResponseEntity<List<EmployeeSummaryResponse>>
            getEmployeeSummaries() {

        return ResponseEntity.ok(
                employeeService.getEmployeeSummaries()
        );
    }
}
