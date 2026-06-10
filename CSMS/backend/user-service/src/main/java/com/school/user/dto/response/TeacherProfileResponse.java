package com.school.user.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record TeacherProfileResponse(
        UUID id,
        String employeeCode,
        String qualification,
        String specialization,
        LocalDate joiningDate,
        Integer experienceYears
) {}
