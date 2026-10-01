
package com.blood.bloodbank.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DonationRequest(
        @NotNull
        Integer donorId,

        @NotNull
        @Min(1)
        Integer quantityMl,

        @NotNull
        @Min(1)
        Integer unitsDonated,

        @NotNull
        LocalDate donationDate
) {
}