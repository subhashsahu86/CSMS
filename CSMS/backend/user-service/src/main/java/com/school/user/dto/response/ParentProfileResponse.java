package com.school.user.dto.response;

import java.util.UUID;

public record ParentProfileResponse(
        UUID id,
        String occupation,
        String relationshipLabel,
        String alternatePhone
) {}
