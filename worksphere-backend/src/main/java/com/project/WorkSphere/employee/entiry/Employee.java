//package com.project.WorkSphere.employee.entiry;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "employees")
//@Data
//public class Employee {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(
//        name = "employee_code",
//        nullable = false,
//        unique = true,
//        length = 30
//    )
//    private String employeeCode;
//
//    @Column(nullable = false, length = 100)
//    private String firstName;
//
//    @Column(nullable = false, length = 100)
//    private String lastName;
//
//    @Column(nullable = false, unique = true)
//    private String email;
//
//    private String phone;
//
//    private LocalDate dateOfJoining;
//
//    private LocalDateTime createdAt;
//
//    // Constructors
//
//    public Employee() {
//    }
//
//    // Getters and Setters
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getEmployeeCode() {
//        return employeeCode;
//    }
//
//    public void setEmployeeCode(String employeeCode) {
//        this.employeeCode = employeeCode;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//}
