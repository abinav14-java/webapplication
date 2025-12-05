package com.abinav.webapplication.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret:}")
    private String jwtSecret;

    private Key secretKey;

    private final long expirationMillis = 3600_000; // 1 hour

    @PostConstruct
    public void init() {
        if (jwtSecret != null && !jwtSecret.isBlank()) {
            // Use provided secret (must be at least 32 bytes for HS256)
            secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        } else {
            // Fallback to an in-memory random key (tokens will not survive restart)
            secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }
    }

    // Generate JWT
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    // Validate JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Extract username (email) from JWT
    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Getter for secret key (used if needed elsewhere)
    public Key getSecretKey() {
        return secretKey;
    }
}
