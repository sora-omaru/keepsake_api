package com.omaru.keepsake_api.dto.response;

// Entry一覧にTagを含める場合に使用する。
// import java.util.List;

public record EntryResponseDto(
        Long id,
        Long workspaceId,
        Long topicId,
        Long memberId,
        Long creatorId,
        String creatorName,
        // Entry一覧ではTagが不要になったため、レスポンス項目から除外している。
        // List<TagResponseDto> tags,
        String title,
        String content,
        boolean completed
) {
}
