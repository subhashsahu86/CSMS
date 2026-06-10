package com.school.auth.dto.response;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        boolean mustChangePassword,
        AuthUserSummary user
) {
}
