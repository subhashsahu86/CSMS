package com.school.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record LinkParentStudentRequest(
        @NotNull(message = "parentUserProfileId is required")
        UUID parentUserProfileId,

        @NotNull(message = "studentUserProfileId is required")
        UUID studentUserProfileId,

        @NotBlank(message = "relationship is required")
        String relationship,

        Boolean isPrimaryGuardian,
        Boolean canReceiveNotifications
) {}
