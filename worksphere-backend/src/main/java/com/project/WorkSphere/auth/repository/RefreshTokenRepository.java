package com.project.WorkSphere.auth.repository;


import com.project.WorkSphere.auth.entity.RefreshToken;
import com.project.WorkSphere.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    List<RefreshToken> findAllByUserAndRevokedFalse(User user);
}