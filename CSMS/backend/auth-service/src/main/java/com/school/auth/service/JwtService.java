package com.school.auth.service;

import com.school.auth.config.JwtProperties;
import com.school.auth.domain.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(AuthUser user) {
        Instant now = Instant.now();
        Instant expiry = now.plus(jwtProperties.accessTokenExpirationMinutes(), ChronoUnit.MINUTES);
        Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(java.util.stream.Collectors.toSet());

        return Jwts.builder()
                .issuer(jwtProperties.issuer())
                .subject(user.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .claim("userId", user.getId().toString())
                .claim("roles", roles)
                .signWith(secretKey)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long accessTokenExpiresInSeconds() {
        return jwtProperties.accessTokenExpirationMinutes() * 60L;
    }
}
