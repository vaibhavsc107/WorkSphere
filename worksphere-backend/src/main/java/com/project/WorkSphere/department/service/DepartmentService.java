package com.project.WorkSphere.department.service;

import com.project.WorkSphere.department.dto.DepartmentRequest;
import com.project.WorkSphere.department.dto.DepartmentResponse;
import com.project.WorkSphere.department.entity.Department;
import com.project.WorkSphere.department.repository.DepartmentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // Create department
    @Transactional
    public DepartmentResponse createDepartment(DepartmentRequest request) {

        if (departmentRepository.existsByNameIgnoreCase(request.getName().trim())) {
            throw new IllegalArgumentException("Department already exists");
        }

        Department department = new Department();
        department.setName(request.getName().trim());
        department.setDescription(request.getDescription());
        department.setActive(true);

        Department savedDepartment = departmentRepository.save(department);

        return mapToResponse(savedDepartment);
    }

    // Get all departments
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {

        return departmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get department by ID
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Department not found with ID: " + id));

        return mapToResponse(department);
    }

    // Update department
    @Transactional
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Department not found with ID: " + id));

        String newName = request.getName().trim();

        if (!department.getName().equalsIgnoreCase(newName)
                && departmentRepository.existsByNameIgnoreCase(newName)) {
            throw new IllegalArgumentException("Department name already exists");
        }

        department.setName(newName);
        department.setDescription(request.getDescription());

        return mapToResponse(departmentRepository.save(department));
    }

    // Deactivate department
    @Transactional
    public void deactivateDepartment(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Department not found with ID: " + id));

        department.setActive(false);

        departmentRepository.save(department);
    }

    // Get active departments
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getActiveDepartments() {

        return departmentRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Entity to DTO mapping
    private DepartmentResponse mapToResponse(Department department) {

        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getDescription(),
                department.isActive(),
                department.getCreatedAt()
        );
    }
}
