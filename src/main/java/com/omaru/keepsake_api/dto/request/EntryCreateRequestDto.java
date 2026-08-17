package com.omaru.keepsake_api.dto.request;

public record EntryCreateRequestDto(
        Long topicId,
        Long memberId,
        String title,
        String content
) {
}
