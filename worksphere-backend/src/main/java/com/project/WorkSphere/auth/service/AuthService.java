package com.project.WorkSphere.auth.service;

import com.project.WorkSphere.auth.dto.RegisterRequest;
import com.project.WorkSphere.role.RoleName;
import com.project.WorkSphere.role.entity.Role;
import com.project.WorkSphere.role.repository.RoleRepository;
import com.project.WorkSphere.user.entity.User;
import com.project.WorkSphere.user.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.WorkSphere.auth.dto.LoginRequest;
import com.project.WorkSphere.auth.dto.AuthResponse;
import com.project.WorkSphere.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
   
    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
    	
    	this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String register(RegisterRequest request) {

        // 1. Check duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "Email is already registered"
            );
        }

        // 2. Find default EMPLOYEE role
        Role employeeRole = roleRepository
                .findByName(RoleName.EMPLOYEE)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "EMPLOYEE role not found"
                        )
                );

        // 3. Create user
        User user = new User();

        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        // 4. Assign role
        user.addRole(employeeRole);

        // 5. Save user
        userRepository.save(user);

        return "User registered successfully";        
    }
    

public AuthResponse login(LoginRequest request) {

    // 1. Find user by email
    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                    new BadCredentialsException(
                            "Invalid email or password"
                    )
            );

    // 2. Check whether account is active
    if (!user.isActive()) {
        throw new DisabledException("Account is disabled");
    }

    // 3. Verify password using BCrypt
    if (!passwordEncoder.matches(
            request.getPassword(),
            user.getPassword())) {

        throw new BadCredentialsException(
                "Invalid email or password"
        );
    }

    // 4. Extract roles
    Set<String> roles = user.getRoles()
            .stream()
            .map(role -> role.getName().name())
            .collect(Collectors.toSet());

    // 5. Generate JWT
    String token = jwtService.generateToken(
            user.getEmail(),
            roles.stream().toList()
    );

    // 6. Return authentication response
    return new AuthResponse(
            token,
            user.getEmail(),
            roles
    );
}

}
