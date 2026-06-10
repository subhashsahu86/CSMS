package com.school.auth.dto.response;

public record TokenResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}
