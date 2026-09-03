package com.omaru.keepsake_api.dto.response;

public record AccountResponseDto(
        Long id,
        String email,
        String displayName,
        String pictureUrl
) {
}
