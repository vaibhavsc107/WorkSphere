
package com.project.WorkSphere.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.project.WorkSphere.security.JwtAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfig(
	        JwtAuthenticationFilter jwtAuthenticationFilter) {
	    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {
    
        http
            .cors(cors -> {})
            .csrf(csrf -> csrf.disable())
            
            .sessionManagement(session -> session
            	    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )            
            
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    HttpMethod.GET,                    
                    "/api/v1/test"                
                ).permitAll()
                .requestMatchers(
                        HttpMethod.POST,                    
                        "/api/v1/test",
                        "/api/v1/auth/register",
                        "/api/v1/auth/login",
                        "/api/v1/auth/refresh",
                        "/api/v1/auth/logout"
                    ).permitAll()
                .anyRequest().authenticated()
            )
            
            .addFilterBefore(
            	    jwtAuthenticationFilter,
            	    UsernamePasswordAuthenticationFilter.class
            )
            
            .exceptionHandling(ex -> ex
            	    .authenticationEntryPoint(
            	        new org.springframework.security.web.authentication
            	            .HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
            	    )
            	)
            ;

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:5173")
        );

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        );

        configuration.setAllowedHeaders(
                List.of("Authorization", "Content-Type")
        );
        
        // Allow browser cookies
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
