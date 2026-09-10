package com.omaru.keepsake_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AccountUpdateRequestDto(
        @NotBlank(message = "作成者名を入力してください")
        @Size(max = 100, message = "作成者名は100文字以内で入力してください")
        String displayName
) {}
