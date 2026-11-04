package com.project.WorkSphere.employee.dto;

import com.project.WorkSphere.employee.entity.EmploymentStatus;

import lombok.Data;

@Data
public class EmployeeSummaryResponse {

    private Long id;
    private String employeeCode;
    private String fullName;
    private String email;
    private String departmentName;
    private String designationName;
    private EmploymentStatus employmentStatus;

    public EmployeeSummaryResponse() {
    }

    public EmployeeSummaryResponse(
            Long id,
            String employeeCode,
            String fullName,
            String email,
            String departmentName,
            String designationName,
            EmploymentStatus employmentStatus) {

        this.id = id;
        this.employeeCode = employeeCode;
        this.fullName = fullName;
        this.email = email;
        this.departmentName = departmentName;
        this.designationName = designationName;
        this.employmentStatus = employmentStatus;
    }

    // Generate getters and setters using your IDE.
}
