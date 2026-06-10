package com.school.user.dto.request;

import com.school.user.domain.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressRequest(
        @NotNull(message = "Address type is required")
        AddressType addressType,

        @NotBlank(message = "Address line 1 is required")
        String addressLine1,

        String addressLine2,

        @NotBlank(message = "City is required")
        String city,

        @NotBlank(message = "State is required")
        String state,

        @NotBlank(message = "Postal code is required")
        String postalCode,

        @NotBlank(message = "Country is required")
        String country
) {}
