package com.school.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        String issuer,
        int accessTokenExpirationMinutes,
        int refreshTokenExpirationDays,
        String secret
) {
}
