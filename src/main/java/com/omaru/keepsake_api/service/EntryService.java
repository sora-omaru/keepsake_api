package com.omaru.keepsake_api.service;


import com.omaru.keepsake_api.dto.request.EntryCreateRequestDto;
import com.omaru.keepsake_api.dto.response.EntryResponseDto;

public interface EntryService {
    EntryResponseDto createEntry(Long workspaceId, EntryCreateRequestDto request);
}
