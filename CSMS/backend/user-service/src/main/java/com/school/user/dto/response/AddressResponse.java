package com.school.user.dto.response;

import com.school.user.domain.AddressType;
import java.util.UUID;

public record AddressResponse(
        UUID id,
        AddressType addressType,
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String postalCode,
        String country
) {}
