package com.omaru.keepsake_api.service.impl;

import com.omaru.keepsake_api.dto.request.WorkspaceCreateRequestDto;
import com.omaru.keepsake_api.dto.response.WorkspaceResponseDto;
import com.omaru.keepsake_api.entity.WorkspaceEntity;
import com.omaru.keepsake_api.repository.WorkspaceRepository;
import com.omaru.keepsake_api.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {
    private final WorkspaceRepository workspaceRepository;

    @Override
    public WorkspaceResponseDto createWorkspace(WorkspaceCreateRequestDto request) {
        WorkspaceEntity workspace = new WorkspaceEntity();

        workspace.setName(request.name().trim());

        return toResponse(workspaceRepository.save((workspace)));
    }

    private WorkspaceResponseDto toResponse(WorkspaceEntity workspace) {
        return new WorkspaceResponseDto(workspace.getId(), workspace.getName());
    }
}
