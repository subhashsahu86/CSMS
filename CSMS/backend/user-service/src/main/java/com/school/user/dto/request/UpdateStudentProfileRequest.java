package com.school.user.dto.request;

import com.school.user.domain.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

public record UpdateStudentProfileRequest(
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

        String registrationNumber,
        String admissionNumber,
        LocalDate admissionDate,
        String bloodGroup,
        String category,
        String previousSchool,

        @Valid
        List<AddressRequest> addresses
) {}
