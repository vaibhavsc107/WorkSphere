package com.project.WorkSphere.designation.repository;

import com.project.WorkSphere.designation.entity.Designation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DesignationRepository
        extends JpaRepository<Designation, Long> {

    Optional<Designation> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<Designation> findByActiveTrue();
}
