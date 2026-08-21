package com.omaru.keepsake_api.service;

import com.omaru.keepsake_api.dto.request.TagCreateRequestDto;
import com.omaru.keepsake_api.dto.response.TagResponseDto;

import java.util.List;

public interface TagService {
    List<TagResponseDto>getTags(Long workspaceId);

    TagResponseDto createTag(TagCreateRequestDto request,Long workspaceId);

    void deleteTag(Long workspaceId, Long tagId);
}
