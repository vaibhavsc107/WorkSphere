package com.project.WorkSphere.role.repository;

import com.project.WorkSphere.role.RoleName;
import com.project.WorkSphere.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
    
    boolean existsByName(RoleName name);
}