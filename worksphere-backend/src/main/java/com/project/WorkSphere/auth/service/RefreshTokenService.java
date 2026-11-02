package com.project.WorkSphere.auth.service;

import com.project.WorkSphere.auth.entity.RefreshToken;
import com.project.WorkSphere.auth.repository.RefreshTokenRepository;
import com.project.WorkSphere.user.entity.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final SecureRandom secureRandom = new SecureRandom();

    private final long refreshExpiration;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            @Value("${jwt.refresh-expiration}") long refreshExpiration) {

        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshExpiration = refreshExpiration;
    }

    @Transactional
    public String createRefreshToken(User user) {

        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);

        String rawToken = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setTokenHash(hashToken(rawToken));
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(
                Instant.now().plusMillis(refreshExpiration)
        );

        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }

    public RefreshToken validateRefreshToken(String rawToken) {

        String tokenHash = hashToken(rawToken);

        RefreshToken refreshToken = refreshTokenRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid refresh token")
                );

        if (refreshToken.isRevoked()) {
            throw new IllegalArgumentException("Refresh token revoked");
        }

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Refresh token expired");
        }

        if (!refreshToken.getUser().isActive()) {
            throw new IllegalArgumentException("User account is disabled");
        }

        return refreshToken;
    }

    @Transactional
    public void revokeRefreshToken(String rawToken) {

        String tokenHash = hashToken(rawToken);

        refreshTokenRepository.findByTokenHash(tokenHash)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }

    private String hashToken(String token) {

        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );

            return Base64.getEncoder().encodeToString(hash);

        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "SHA-256 algorithm unavailable", exception
            );
        }
    }
}