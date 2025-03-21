package com.ruh.mis.security.jwt;

import com.ruh.mis.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private String parseJwt(HttpServletRequest request) {
        // First try to get JWT from cookies
        String jwt = jwtUtils.getJwtFromCookies(request);
        log.debug("JWT from cookies: {}", jwt);
        
        // If not found in cookies, try to get from Authorization header
        if (jwt == null || jwt.isEmpty()) {
            String headerAuth = request.getHeader("Authorization");
            log.debug("Authorization header: {}", headerAuth);
            
            if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
                jwt = headerAuth.substring(7);
                log.debug("JWT from Authorization header: {}", jwt);
            }
        }
        
        return jwt;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        boolean isPublicEndpoint = requestURI.startsWith("/api/transcripts/") || 
                                  requestURI.startsWith("/public/transcripts/") || 
                                  requestURI.startsWith("/api/auth/") || 
                                  requestURI.startsWith("/swagger-ui/") || 
                                  requestURI.startsWith("/v3/api-docs/") || 
                                  requestURI.startsWith("/v2/api-docs") || 
                                  requestURI.startsWith("/configuration/ui") || 
                                  requestURI.startsWith("/swagger-resources") || 
                                  requestURI.startsWith("/configuration/security") || 
                                  requestURI.startsWith("/swagger-ui.html") || 
                                  requestURI.startsWith("/webjars/") || 
                                  requestURI.startsWith("/h2-console/") || 
                                  requestURI.startsWith("/api/test/") || 
                                  requestURI.startsWith("/images/");
        
        if (isPublicEndpoint) {
            log.info("Bypassing authentication for public endpoint: {}", requestURI);
        }
        
        return isPublicEndpoint;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        log.debug("AuthTokenFilter processing URI: {}", requestURI);

        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());
                log.debug("Roles from JWT: {}", userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (RuntimeException e) {
            log.error("JWT token validation error: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }
}
