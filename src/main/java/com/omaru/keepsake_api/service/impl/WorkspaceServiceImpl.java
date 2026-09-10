package com.omaru.keepsake_api.service.impl;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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
    @Transactional
    public WorkspaceResponseDto createWorkspace(Long accountId, WorkspaceCreateRequestDto request) {
        WorkspaceEntity workspace = new WorkspaceEntity();

        workspace.setName(request.name().trim());

        WorkspaceEntity saved = workspaceRepository.saveAndFlush(workspace);
        workspaceRepository.addAccount(saved.getId(), accountId);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkspaceResponseDto> getWorkspaces(Long accountId) {
        return workspaceRepository.findAllForAccount(accountId).stream().map(this::toResponse).toList();
    }

    private WorkspaceResponseDto toResponse(WorkspaceEntity workspace) {
        return new WorkspaceResponseDto(workspace.getId(), workspace.getName());
    }
}
