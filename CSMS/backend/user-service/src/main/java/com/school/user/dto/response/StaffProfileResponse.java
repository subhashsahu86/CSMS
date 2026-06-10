package com.school.user.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record StaffProfileResponse(
        UUID id,
        String employeeCode,
        String department,
        String designation,
        LocalDate joiningDate
) {}
