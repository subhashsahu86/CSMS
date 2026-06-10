package com.school.user.dto.request;

import com.school.user.domain.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

public record UpdateStaffProfileRequest(
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
        String department,
        String designation,
        LocalDate joiningDate,

        @Valid
        List<AddressRequest> addresses
) {}
