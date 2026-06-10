package com.school.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest(
        @NotBlank
        String currentPassword,

        @NotBlank
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%]).{8,}$",
                message = "new password must be at least 8 characters and include uppercase, lowercase, digit, and symbol"
        )
        String newPassword
) {
}
