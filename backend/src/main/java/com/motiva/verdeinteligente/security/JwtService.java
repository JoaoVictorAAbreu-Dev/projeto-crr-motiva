package com.motiva.verdeinteligente.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${app.auth.jwt-secret}")
    private String jwtSecret;

    @Value("${app.auth.token-expiration-hours}")
    private long tokenExpirationHours;

    public String generateToken(UserDetails userDetails, String fullName, String role) {
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(tokenExpirationHours);
        return Jwts.builder()
            .claims(Map.of("fullName", fullName, "role", role))
            .subject(userDetails.getUsername())
            .issuedAt(new Date())
            .expiration(Date.from(expiresAt.atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(signingKey())
            .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public LocalDateTime extractExpiration(String token) {
        return LocalDateTime.ofInstant(extractAllClaims(token).getExpiration().toInstant(), ZoneId.systemDefault());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return userDetails.getUsername().equals(extractUsername(token)) && extractExpiration(token).isAfter(LocalDateTime.now());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(resolveSecretBytes()))
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private Key signingKey() {
        return Keys.hmacShaKeyFor(resolveSecretBytes());
    }

    private byte[] resolveSecretBytes() {
        return jwtSecret.matches("^[A-Za-z0-9+/=]+$") && jwtSecret.length() % 4 == 0
            ? Decoders.BASE64.decode(jwtSecret)
            : jwtSecret.getBytes(StandardCharsets.UTF_8);
    }
}
