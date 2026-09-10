package com.omaru.keepsake_api.service;

import java.util.List;

import com.omaru.keepsake_api.dto.request.WorkspaceCreateRequestDto;
import com.omaru.keepsake_api.dto.response.WorkspaceResponseDto;

public interface WorkspaceService {
    WorkspaceResponseDto createWorkspace(Long accountId, WorkspaceCreateRequestDto request);
    List<WorkspaceResponseDto> getWorkspaces(Long accountId);
}
