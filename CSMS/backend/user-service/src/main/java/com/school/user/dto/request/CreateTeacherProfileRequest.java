package com.school.user.dto.request;

import com.school.user.domain.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateTeacherProfileRequest(
        @NotNull(message = "authUserId is required")
        UUID authUserId,

        @NotBlank(message = "username is required")
        String username,

        @NotBlank(message = "First name is required")
        String firstName,

        String middleName,
        String lastName,
        String displayName,
        Gender gender,
        LocalDate dateOfBirth,
        String primaryEmail,
        String primaryPhone,
        String profileImageUrl,

        String employeeCode,
        String qualification,
        String specialization,
        LocalDate joiningDate,
        Integer experienceYears,

        @Valid
        List<AddressRequest> addresses
) {}
