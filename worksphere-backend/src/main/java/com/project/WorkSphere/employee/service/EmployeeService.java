package com.project.WorkSphere.employee.service;

import com.project.WorkSphere.employee.dto.EmployeeRequest;
import com.project.WorkSphere.employee.dto.EmployeeProfileUpdateRequest;
import org.springframework.transaction.annotation.Transactional;

import com.project.WorkSphere.storage.service.ProfileImageService;
import org.springframework.web.multipart.MultipartFile;

import com.project.WorkSphere.employee.dto.EmployeeResponse;
import com.project.WorkSphere.employee.dto.EmployeeSummaryResponse;
import com.project.WorkSphere.employee.entity.Employee;
import com.project.WorkSphere.employee.entity.EmploymentStatus;
import com.project.WorkSphere.employee.repository.EmployeeRepository;

import com.project.WorkSphere.user.entity.User;
import com.project.WorkSphere.user.repository.UserRepository;

import com.project.WorkSphere.department.entity.Department;
import com.project.WorkSphere.department.repository.DepartmentRepository;

import com.project.WorkSphere.designation.entity.Designation;
import com.project.WorkSphere.designation.repository.DesignationRepository;

import org.springframework.stereotype.Service;

import com.project.WorkSphere.employee.specification.EmployeeSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    
    private final ProfileImageService profileImageService;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            UserRepository userRepository,
            DepartmentRepository departmentRepository,
            DesignationRepository designationRepository,
            ProfileImageService profileImageService) {

        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.designationRepository = designationRepository;
        this.profileImageService = profileImageService;
    }
    
    @Transactional
    public void removeMyProfileImage(String email) {

        Employee employee = employeeRepository.findByUserEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Employee not found"));

        String oldImage = employee.getProfileImage();

        if (oldImage == null || oldImage.isBlank()) {
            throw new IllegalArgumentException("No profile image to remove");
        }

        employee.setProfileImage(null);
        employeeRepository.save(employee);

        try {
            profileImageService.deleteProfileImage(oldImage);
        } catch (Exception ex) {
            // Log cleanup failure; the database reference is already cleared.
            System.err.println("Unable to delete old profile image: " + ex.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public EmployeeResponse getMyProfile(String email) {

        Employee employee = employeeRepository.findByUserEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Employee profile not found for user: " + email
                        )
                );

        return mapToResponse(employee);
    }

    @Transactional
    public EmployeeResponse updateProfileImage(
            String email,
            MultipartFile file) {

        Employee employee = employeeRepository.findByUserEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Employee profile not found for user: " + email
                        )
                );

        String oldFilename = employee.getProfileImage();

        // Upload the new image first
        String newFilename = profileImageService.uploadProfileImage(file);

        try {
            employee.setProfileImage(newFilename);

            Employee updatedEmployee = employeeRepository.save(employee);

            // Delete old image after database update succeeds
            if (oldFilename != null && !oldFilename.equals(newFilename)) {
                try {
                    profileImageService.deleteProfileImage(oldFilename);
                } catch (RuntimeException cleanupException) {
                    // Log cleanup failure; do not undo the successful replacement
                    System.err.println(
                            "Unable to delete previous profile image: "
                                    + cleanupException.getMessage()
                    );
                }
            }

            return mapToResponse(updatedEmployee);

        } catch (RuntimeException exception) {

            // Database update failed: remove newly uploaded image
            try {
                profileImageService.deleteProfileImage(newFilename);
            } catch (RuntimeException cleanupException) {
                exception.addSuppressed(cleanupException);
            }

            throw exception;
        }
    }

@Transactional
public EmployeeResponse updateMyProfile(
        String email,
        EmployeeProfileUpdateRequest request) {

    Employee employee = employeeRepository.findByUserEmail(email)
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "Employee profile not found for user: " + email
                    )
            );

    employee.setFirstName(request.getFirstName());
    employee.setLastName(request.getLastName());
    employee.setPhone(request.getPhone());
    employee.setDateOfBirth(request.getDateOfBirth());
    employee.setGender(request.getGender());
    employee.setAddress(request.getAddress());

    Employee updatedEmployee = employeeRepository.save(employee);

    return mapToResponse(updatedEmployee);
}


    // Create employee
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {

        String employeeCode = request.getEmployeeCode().trim();

        if (employeeRepository.existsByEmployeeCode(employeeCode)) {
            throw new IllegalArgumentException("Employee code already exists");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        if (!user.isActive()) {
            throw new IllegalArgumentException("User account is inactive");
        }

        if (employeeRepository.existsByUserId(user.getId())) {
            throw new IllegalArgumentException(
                    "This user already has an employee record");
        }

        Department department = departmentRepository
                .findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Department not found"));

        if (!department.isActive()) {
            throw new IllegalArgumentException("Department is inactive");
        }

        Designation designation = designationRepository
                .findById(request.getDesignationId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Designation not found"));

        if (!designation.isActive()) {
            throw new IllegalArgumentException("Designation is inactive");
        }

        Employee employee = new Employee();

        employee.setEmployeeCode(employeeCode);
        employee.setFirstName(request.getFirstName().trim());
        employee.setLastName(request.getLastName().trim());
        employee.setPhone(request.getPhone());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setGender(request.getGender());
        employee.setAddress(request.getAddress());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setEmploymentStatus(EmploymentStatus.ACTIVE);

        employee.setUser(user);
        employee.setDepartment(department);
        employee.setDesignation(designation);

        Employee savedEmployee = employeeRepository.save(employee);

        return mapToResponse(savedEmployee);
    }

    // Get all employees
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get employee by ID
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Employee not found with ID: " + id));

        return mapToResponse(employee);
    }

    // Update employee
    @Transactional
    public EmployeeResponse updateEmployee(
            Long id, EmployeeRequest request) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Employee not found with ID: " + id));

        String newEmployeeCode = request.getEmployeeCode().trim();

        if (!employee.getEmployeeCode().equalsIgnoreCase(newEmployeeCode)
                && employeeRepository.existsByEmployeeCode(newEmployeeCode)) {
            throw new IllegalArgumentException(
                    "Employee code already exists");
        }

        // Do not silently transfer an employee to another login account.
        if (!employee.getUser().getId().equals(request.getUserId())) {
            throw new IllegalArgumentException(
                    "Employee login account cannot be changed");
        }

        Department department = departmentRepository
                .findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Department not found"));

        if (!department.isActive()) {
            throw new IllegalArgumentException("Department is inactive");
        }

        Designation designation = designationRepository
                .findById(request.getDesignationId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Designation not found"));

        if (!designation.isActive()) {
            throw new IllegalArgumentException("Designation is inactive");
        }

        employee.setEmployeeCode(newEmployeeCode);
        employee.setFirstName(request.getFirstName().trim());
        employee.setLastName(request.getLastName().trim());
        employee.setPhone(request.getPhone());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setGender(request.getGender());
        employee.setAddress(request.getAddress());
        employee.setJoiningDate(request.getJoiningDate());

        employee.setDepartment(department);
        employee.setDesignation(designation);

        return mapToResponse(employeeRepository.save(employee));
    }

    // Deactivate employee
    @Transactional
    public void deactivateEmployee(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Employee not found with ID: " + id));

        employee.setEmploymentStatus(EmploymentStatus.INACTIVE);

        employeeRepository.save(employee);
    }

    // Get active employees
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getActiveEmployees() {

        return employeeRepository
                .findByEmploymentStatus(EmploymentStatus.ACTIVE)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get employee summary list
    @Transactional(readOnly = true)
    public List<EmployeeSummaryResponse> getEmployeeSummaries() {

        return employeeRepository.findAll()
                .stream()
                .map(employee -> new EmployeeSummaryResponse(
                        employee.getId(),
                        employee.getEmployeeCode(),
                        employee.getFirstName() + " " + employee.getLastName(),
                        employee.getUser().getEmail(),
                        employee.getDepartment().getName(),
                        employee.getDesignation().getName(),
                        employee.getEmploymentStatus()
                ))
                .toList();
    }

    // Convert Employee entity into response DTO
    private EmployeeResponse mapToResponse(Employee employee) {

        return new EmployeeResponse(
                employee.getId(),
                employee.getEmployeeCode(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getUser().getEmail(),
                employee.getPhone(),
                employee.getDateOfBirth(),
                employee.getGender(),
                employee.getAddress(),
                employee.getJoiningDate(),
                employee.getEmploymentStatus(),
                employee.getDepartment().getId(),
                employee.getDepartment().getName(),
                employee.getDesignation().getId(),
                employee.getDesignation().getName(),
                employee.getCreatedAt(),
                employee.getProfileImage()
        );
    }
    
    public Page<EmployeeResponse> searchEmployees(
            String search,
            Long departmentId,
            Long designationId,
            EmploymentStatus status,
            Pageable pageable) {

        Specification<Employee> specification = Specification
                .where(EmployeeSpecification.hasSearchTerm(search))
                .and(EmployeeSpecification.hasDepartment(departmentId))
                .and(EmployeeSpecification.hasDesignation(designationId))
                .and(EmployeeSpecification.hasStatus(status));

        return employeeRepository.findAll(specification, pageable)
                .map(this::mapToResponse);
    }
}
