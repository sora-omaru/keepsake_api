package com.omaru.keepsake_api.dto.request;

public record EntryUpdateRequestDto(
        String title,
        String content
) {
}
