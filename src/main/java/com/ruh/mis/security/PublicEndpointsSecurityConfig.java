package com.ruh.mis.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration specifically for public endpoints that don't require authentication
 */
@Configuration
@EnableWebSecurity
public class PublicEndpointsSecurityConfig {

    @Bean
    @Order(1) // This ensures this filter chain is evaluated before the main security filter chain
    public SecurityFilterChain publicEndpointsFilterChain(HttpSecurity http) throws Exception {
        http
            // Only apply this configuration to public endpoints
            .securityMatcher("/api/public-transcripts/**", "/api/transcripts/**")
            // Disable CSRF for public endpoints
            .csrf(csrf -> csrf.disable())
            // Use stateless session management
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Allow all requests to these endpoints without authentication
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            // Disable security context for these endpoints
            .securityContext(securityContext -> securityContext.disable());
        
        return http.build();
    }
}
