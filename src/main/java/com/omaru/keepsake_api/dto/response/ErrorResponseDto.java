package com.omaru.keepsake_api.dto.response;

import java.time.OffsetDateTime;

public record ErrorResponseDto(
        int status,
        String error,
        String message,
        OffsetDateTime timestamp
) {
}
