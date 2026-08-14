package com.omaru.keepsake_api.dto.response;

public record TopicResponseDto(
        Long id,
        Long workspaceId,
        String name,
        String description
) {
}
