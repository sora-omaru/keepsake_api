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

        WorkspaceEntity saved = workspaceRepository.save(workspace);


        return new WorkspaceResponseDto(saved.getId(), saved.getName());
    }
}
