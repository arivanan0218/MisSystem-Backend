package com.ruh.mis.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filter that runs before Spring Security to handle public endpoints
 * This ensures that Spring Security is completely bypassed for these endpoints
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // Ensure this runs before Spring Security filters
public class PublicEndpointFilter implements Filter {
    
    private static final Logger logger = LoggerFactory.getLogger(PublicEndpointFilter.class);
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        
        // Check if this is a public transcript endpoint
        if (requestURI.startsWith("/public/transcripts/") || requestURI.startsWith("/api/transcripts/")) {
            logger.info("PublicEndpointFilter: Processing public endpoint request: {}", requestURI);
            // Just continue the filter chain without any security checks
            chain.doFilter(request, response);
        } else {
            // For all other requests, continue the filter chain normally
            chain.doFilter(request, response);
        }
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("PublicEndpointFilter initialized");
    }
    
    @Override
    public void destroy() {
        logger.info("PublicEndpointFilter destroyed");
    }
}
