package com.omaru.keepsake_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TagCreateRequestDto(
        @NotBlank(message = "タグの名前を決めてください")
        @Size(max = 100)
        String name
) {
}
