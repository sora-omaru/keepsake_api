package com.omaru.keepsake_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EntryCreateRequestDto(
        @NotNull(message = "メンバーを指定してください")
        Long memberId,
        @NotBlank(message = "タイトルを入力してください")
        @Size(max = 100, message = "タイトルは100文字以内で入力してください")
        String title,
        String content
) {
}
