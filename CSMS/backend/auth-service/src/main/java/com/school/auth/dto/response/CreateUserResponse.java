package com.school.auth.dto.response;

import java.util.Set;
import java.util.UUID;

public record CreateUserResponse(
        UUID userId,
        String username,
        String temporaryPassword,
        boolean mustChangePassword,
        Set<String> roles
) {
}
