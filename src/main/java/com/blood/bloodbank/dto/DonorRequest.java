
package com.blood.bloodbank.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DonorRequest(
        @NotBlank
        @Size(max = 100)
        String name,

        @NotNull
        @Min(18)
        Integer age,

        String gender,

        @Size(max = 15)
        String phone,

        @NotNull
        Integer bloodGroupId
) {
}