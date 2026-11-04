package com.project.WorkSphere.employee.dto;

import com.project.WorkSphere.employee.entity.Gender;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeRequest {

    @NotBlank(message = "Employee code is required")
    @Size(max = 20)
    private String employeeCode;

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;

    @Pattern(
        regexp = "^[0-9+\\-() ]{7,15}$",
        message = "Invalid phone number"
    )
    private String phone;

    @Past(message = "Date of birth must be in the past")
    @Min(18)
    private LocalDate dateOfBirth;

    private Gender gender;

    @Size(max = 255)
    private String address;

    @NotNull(message = "Joining date is required")
    @PastOrPresent(message = "Joining date cannot be in the future")
    private LocalDate joiningDate;

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;

    @NotNull(message = "Department ID is required")
    @Positive(message = "Department ID must be positive")
    private Long departmentId;

    @NotNull(message = "Designation ID is required")
    @Positive(message = "Designation ID must be positive")
    private Long designationId;

    public EmployeeRequest() {
    }

    // Generate getters and setters using your IDE.
}
