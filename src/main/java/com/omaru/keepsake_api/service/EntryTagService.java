package com.omaru.keepsake_api.service;

import com.omaru.keepsake_api.dto.response.TagResponseDto;

import java.util.List;

public interface EntryTagService {
    void addTag(Long workspaceId, Long topicId, Long entryId, Long tagId);

    void removeTag(Long workspaceId, Long topicId, Long entryId, Long tagId);

    List<TagResponseDto> getTags(Long workspaceId, Long topicId, Long entryId);
}
