package com.omaru.keepsake_api.dto.response;

public record EntryResponseDto(
        Long id,
        Long workspaceId,
        Long topicId,
        Long memberId,
        String title,
        String content
) {
}
