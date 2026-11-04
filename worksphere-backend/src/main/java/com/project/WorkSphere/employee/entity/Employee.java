
package com.project.WorkSphere.employee.entity;

import com.project.WorkSphere.user.entity.User;
import com.project.WorkSphere.department.entity.Department;
import com.project.WorkSphere.designation.entity.Designation;

import jakarta.persistence.*;
//import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(
    name = "employees",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_employee_code",
            columnNames = "employee_code"
        )
    }
)

public class Employee {
	

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_code", nullable = false, unique = true, length = 20)
    private String employeeCode;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(length = 15)
    private String phone;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = 255)
    private String address;

    @Column(nullable = false)
    private LocalDate joiningDate;

    
    @Column(name = "profile_image")
    private String profileImage;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EmploymentStatus employmentStatus = EmploymentStatus.ACTIVE;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        unique = true
    )
    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designation_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Designation designation;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    

    @Override
	public int hashCode() {
		return Objects.hash(address, createdAt, dateOfBirth, department, designation, employeeCode, employmentStatus,
				firstName, gender, id, joiningDate, lastName, phone, profileImage, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return Objects.equals(address, other.address) && Objects.equals(createdAt, other.createdAt)
				&& Objects.equals(dateOfBirth, other.dateOfBirth) && Objects.equals(department, other.department)
				&& Objects.equals(designation, other.designation) && Objects.equals(employeeCode, other.employeeCode)
				&& employmentStatus == other.employmentStatus && Objects.equals(firstName, other.firstName)
				&& gender == other.gender && Objects.equals(id, other.id)
				&& Objects.equals(joiningDate, other.joiningDate) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(phone, other.phone) && Objects.equals(profileImage, other.profileImage)
				&& Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", employeeCode=" + employeeCode + ", firstName=" + firstName + ", lastName="
				+ lastName + ", phone=" + phone + ", dateOfBirth=" + dateOfBirth + ", gender=" + gender + ", address="
				+ address + ", joiningDate=" + joiningDate + ", profileImage=" + profileImage + ", employmentStatus="
				+ employmentStatus + ", user=" + user + ", department=" + department + ", designation=" + designation
				+ ", createdAt=" + createdAt + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public EmploymentStatus getEmploymentStatus() {
		return employmentStatus;
	}

	public void setEmploymentStatus(EmploymentStatus employmentStatus) {
		this.employmentStatus = employmentStatus;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Designation getDesignation() {
		return designation;
	}

	public void setDesignation(Designation designation) {
		this.designation = designation;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

                 
}
