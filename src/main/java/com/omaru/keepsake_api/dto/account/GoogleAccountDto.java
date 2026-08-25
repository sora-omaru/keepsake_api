package com.omaru.keepsake_api.dto.account;

public record GoogleAccountDto(
        String googleSub,
        String email,
        String displayName,
        String pictureUrl
) {
}
