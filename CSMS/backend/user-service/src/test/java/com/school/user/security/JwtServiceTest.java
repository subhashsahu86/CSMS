package com.school.user.security;

import com.school.user.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    @Test
    void parsesTokenIssuedByAuthServiceContract() {
        String secret = "test-secret-test-secret-test-secret-test-secret";
        String token = Jwts.builder()
                .issuer("school-management-platform")
                .subject("STU2026000001")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(900)))
                .claim("roles", List.of("STUDENT"))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();

        JwtService jwtService = new JwtService(new JwtProperties("school-management-platform", secret));

        assertThat(jwtService.parseToken(token).getSubject()).isEqualTo("STU2026000001");
        assertThat(jwtService.parseToken(token).get("roles", List.class)).contains("STUDENT");
    }
}
