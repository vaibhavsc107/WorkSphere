
package com.project.WorkSphere.config;

import com.project.WorkSphere.role.RoleName;
import com.project.WorkSphere.role.entity.Role;
import com.project.WorkSphere.role.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleDataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        for (RoleName roleName : RoleName.values()) {

            if (!roleRepository.existsByName(roleName)) {

                roleRepository.save(new Role(roleName));

                System.out.println(
                    "Inserted default role: " + roleName
                );
            }
        }
    }
}
