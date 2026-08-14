package com.omaru.keepsake_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TopicCreateRequestDto(
        @NotBlank @Size(max = 100) String name,
        String description
) {
}
