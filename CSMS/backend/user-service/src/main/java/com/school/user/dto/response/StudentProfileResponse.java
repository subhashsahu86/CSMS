package com.school.user.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record StudentProfileResponse(
        UUID id,
        String registrationNumber,
        String admissionNumber,
        LocalDate admissionDate,
        String bloodGroup,
        String category,
        String previousSchool
) {}
