package com.omaru.keepsake_api.service;


import com.omaru.keepsake_api.dto.request.EntryCreateRequestDto;
import com.omaru.keepsake_api.dto.request.EntryUpdateRequestDto;
import com.omaru.keepsake_api.dto.response.EntryResponseDto;

import java.util.List;

public interface EntryService {
    List<EntryResponseDto> getEntries(Long workspaceId, Long topicId);

    EntryResponseDto createEntry(Long workspaceId, Long topicId, EntryCreateRequestDto request);

    EntryResponseDto updateEntry(Long workspaceId, Long topicId, Long entryId, EntryUpdateRequestDto request);
}
