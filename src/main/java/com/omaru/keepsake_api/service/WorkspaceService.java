package com.omaru.keepsake_api.service;

import com.omaru.keepsake_api.dto.request.WorkspaceCreateRequestDto;
import com.omaru.keepsake_api.dto.response.WorkspaceResponseDto;

public interface WorkspaceService {
    WorkspaceResponseDto createWorkspace(WorkspaceCreateRequestDto request);
}
