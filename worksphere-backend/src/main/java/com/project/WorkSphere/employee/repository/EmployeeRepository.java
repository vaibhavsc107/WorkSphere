package com.project.WorkSphere.employee.repository;

import com.project.WorkSphere.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    Optional<Employee> findByEmployeeCode(String employeeCode);

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByUserId(Long userId);

    Optional<Employee> findByUserEmail(String email);

    List<Employee> findByDepartmentId(Long departmentId);

    List<Employee> findByDesignationId(Long designationId);

    List<Employee> findByEmploymentStatus(
            com.project.WorkSphere.employee.entity.EmploymentStatus status
    );
}
