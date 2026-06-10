package com.school.user.dto.response;

import java.util.UUID;

public record ParentStudentLinkResponse(
        UUID id,
        UUID parentUserProfileId,
        UUID studentUserProfileId,
        String relationship,
        boolean isPrimaryGuardian,
        boolean canReceiveNotifications
) {}
