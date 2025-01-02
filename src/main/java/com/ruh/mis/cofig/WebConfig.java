package com.ruh.mis.cofig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:5173", "https://mis-system-frontend.vercel.app") // Replace with actual front-end URL in production
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Ensure OPTIONS is allowed for preflight requests
                        .allowedHeaders("*") // Allow all headers
                        .exposedHeaders("Authorization", "Content-Type") // Add headers you might want to expose
                        .allowCredentials(true) // Allow cookies or authentication tokens
                        .maxAge(3600); // Cache the preflight response for 1 hour
            }
        };
    }
}
