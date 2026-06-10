package com.school.auth.dto.request;

import com.school.auth.domain.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateUserRequest(
        @NotNull
        UserType userType,

        @NotNull
        Integer admissionYear,

        @Email
        String email,

        @Pattern(regexp = "^[0-9+\\-() ]{7,30}$", message = "phone number format is invalid")
        String phoneNumber,

        String createdBy
) {
}
