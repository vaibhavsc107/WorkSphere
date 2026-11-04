package com.project.WorkSphere.employee.specification;

import com.project.WorkSphere.employee.entity.Employee;
import com.project.WorkSphere.employee.entity.EmploymentStatus;

import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    private EmployeeSpecification() {
    }

    public static Specification<Employee> hasSearchTerm(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String pattern = "%" + search.trim().toLowerCase() + "%";

            return criteriaBuilder.or(
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("firstName")), pattern
                ),
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("lastName")), pattern
                ),
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("employeeCode")), pattern
                ),
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.join("user").get("email")), pattern
                )
            );
        };
    }

    public static Specification<Employee> hasDepartment(Long departmentId) {
        return (root, query, criteriaBuilder) ->
            departmentId == null
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.equal(
                    root.get("department").get("id"), departmentId
                );
    }

    public static Specification<Employee> hasDesignation(Long designationId) {
        return (root, query, criteriaBuilder) ->
            designationId == null
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.equal(
                    root.get("designation").get("id"), designationId
                );
    }

    public static Specification<Employee> hasStatus(EmploymentStatus status) {
        return (root, query, criteriaBuilder) ->
            status == null
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.equal(
                    root.get("employmentStatus"), status
                );
    }
}
