package com.omaru.keepsake_api.service;

public interface EntryTagService {
    void addTag(Long workspaceId,Long topicId, Long entryId,Long tagId);
}
