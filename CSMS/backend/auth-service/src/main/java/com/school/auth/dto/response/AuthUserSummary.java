package com.school.auth.dto.response;

import java.util.Set;
import java.util.UUID;

public record AuthUserSummary(
        UUID id,
        String username,
        Set<String> roles
) {
}
