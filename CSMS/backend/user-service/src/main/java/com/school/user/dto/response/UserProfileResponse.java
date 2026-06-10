package com.school.user.dto.response;

import com.school.user.domain.Gender;
import com.school.user.domain.ProfileStatus;
import com.school.user.domain.UserType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        UUID authUserId,
        String username,
        UserType userType,
        String firstName,
        String middleName,
        String lastName,
        String displayName,
        Gender gender,
        LocalDate dateOfBirth,
        String primaryEmail,
        String primaryPhone,
        String profileImageUrl,
        ProfileStatus status,
        List<AddressResponse> addresses,
        StudentProfileResponse studentProfile,
        TeacherProfileResponse teacherProfile,
        ParentProfileResponse parentProfile,
        StaffProfileResponse staffProfile,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
