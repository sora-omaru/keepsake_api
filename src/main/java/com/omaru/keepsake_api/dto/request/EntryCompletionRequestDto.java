package com.omaru.keepsake_api.dto.request;

import jakarta.validation.constraints.NotNull;

public record EntryCompletionRequestDto(
        @NotNull(message = "済・未済を指定してください") Boolean completed
) {}
