package com.ruh.mis.security.jwt;

import com.ruh.mis.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtils {

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    private Integer jwtExpirationMs;

    @Value("${spring.app.jwtCookieName}")
    private String jwtCookie;

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Modified method to include role
    public String generateTokenFromUsername(String username, Set<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", String.join(",", roles))  // Add roles as a comma-separated string
                .signWith(key(), SignatureAlgorithm.HS512)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .compact();
    }

    // Modified method to pass roles
    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        // Extract roles from authorities and map to a Set of role names
        Set<String> roles = userPrincipal.getAuthorities().stream()
                .map(authority -> authority.getAuthority()) // Get role name (e.g., ROLE_USER)
                .collect(Collectors.toSet());

        String jwt = generateTokenFromUsername(userPrincipal.getUsername(), roles);
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt)
                .path("/api")
                .maxAge(24 * 60 * 60) // 1 day expiration
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
        return cookie;
    }

    // Method to remove JWT cookie
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, "")
                .path("/api")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
        return cookie;
    }

    // Method to extract username from JWT token
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Method to extract roles from JWT token
    public Set<String> getRolesFromJwtToken(String token) {
        String rolesClaim = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles", String.class); // Extract the roles claim

        // Split the roles into a set and return
        return Set.of(rolesClaim.split(","));
    }

    // Method to validate JWT token
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        } catch (JwtException e) {
            log.error("JWT parsing or validation error: {}", e.getMessage());
        }
        return false;
    }
}

