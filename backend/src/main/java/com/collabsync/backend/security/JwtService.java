package com.collabsync.backend.security;

import com.collabsync.backend.common.exceptions.InvalidCredentialsException;
import com.collabsync.backend.config.JwtConfigProperties;
import com.collabsync.backend.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfigProperties jwtConfigProperties;

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfigProperties.getExpiration()))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfigProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        try {
        return parseClaims(token)
                .getSubject();
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid token");
        }
    }

    public boolean validateToken(String authToken, String username) {
        final String extractedUsername = extractUsername(authToken);
        return extractedUsername.equals(username) && !isTokenExpired(authToken);
    }

    public boolean isTokenExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}