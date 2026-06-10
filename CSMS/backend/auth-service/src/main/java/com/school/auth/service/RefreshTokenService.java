package com.school.auth.service;

import com.school.auth.config.JwtProperties;
import com.school.auth.domain.AuthUser;
import com.school.auth.domain.RefreshToken;
import com.school.auth.exception.InvalidTokenException;
import com.school.auth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;
    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtProperties jwtProperties) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtProperties = jwtProperties;
    }

    @Transactional
    public String issue(AuthUser user, String deviceInfo, String ipAddress) {
        String rawToken = generateRawToken();
        RefreshToken token = new RefreshToken(
                UUID.randomUUID(),
                user,
                hash(rawToken),
                LocalDateTime.now().plusDays(jwtProperties.refreshTokenExpirationDays()),
                deviceInfo,
                ipAddress
        );
        refreshTokenRepository.save(token);
        return rawToken;
    }

    @Transactional(readOnly = true)
    public RefreshToken validate(String rawToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHashAndRevokedFalse(hash(rawToken))
                .orElseThrow(() -> new InvalidTokenException("refresh token is invalid"));

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("refresh token has expired");
        }

        return refreshToken;
    }

    @Transactional
    public void revoke(String rawToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHashAndRevokedFalse(hash(rawToken))
                .orElseThrow(() -> new InvalidTokenException("refresh token is invalid"));
        refreshToken.revoke();
    }

    private String generateRawToken() {
        byte[] bytes = new byte[48];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 algorithm unavailable", exception);
        }
    }
}
