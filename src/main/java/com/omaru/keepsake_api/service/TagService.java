package com.omaru.keepsake_api.service;

import com.omaru.keepsake_api.dto.request.TagCreateRequestDto;
import com.omaru.keepsake_api.dto.response.TagResponseDto;

public interface TagService {
    TagResponseDto createTag(TagCreateRequestDto request,Long workspaceId);
}
