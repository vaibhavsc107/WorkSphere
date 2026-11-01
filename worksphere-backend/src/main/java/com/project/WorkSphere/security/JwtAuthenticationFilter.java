package com.project.WorkSphere.security;

import com.project.WorkSphere.user.entity.User;
import com.project.WorkSphere.user.repository.UserRepository;

import io.jsonwebtoken.Claims;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository) {

        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader =
                request.getHeader(HttpHeaders.AUTHORIZATION);

        // Check whether the request contains a Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            // Extract and validate JWT
            Claims claims = jwtService.extractClaims(token);
            String email = claims.getSubject();

            if (email != null &&
                    SecurityContextHolder.getContext()
                            .getAuthentication() == null) {

                // Load the current user from database
                User user = userRepository.findByEmail(email)
                        .orElse(null);

                if (user != null &&
                        user.isActive() &&
                        jwtService.isTokenValid(token, email)) {

                    // Convert roles into Spring Security authorities
                    List<SimpleGrantedAuthority> authorities =
                            user.getRoles().stream()
                                    .map(role ->
                                            new SimpleGrantedAuthority(
                                                    "ROLE_" +
                                                    role.getName().name()
                                            )
                                    )
                                    .toList();

                    // Create authenticated principal
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    authorities
                            );

                    authentication.setDetails(
                            new org.springframework.security.web.authentication
                                    .WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    // Store authentication in SecurityContext
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            }

        } catch (io.jsonwebtoken.JwtException |
                 IllegalArgumentException ex) {

            // Invalid token: leave the request unauthenticated
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
