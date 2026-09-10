package com.omaru.keepsake_api.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.List;

import com.omaru.keepsake_api.dto.request.WorkspaceCreateRequestDto;
import com.omaru.keepsake_api.dto.response.WorkspaceResponseDto;
import com.omaru.keepsake_api.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @GetMapping
    public List<WorkspaceResponseDto> list(
            @AuthenticationPrincipal Long accountId) {
        return workspaceService.getWorkspaces(accountId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkspaceResponseDto create(
            @AuthenticationPrincipal Long accountId,
            @Valid @RequestBody WorkspaceCreateRequestDto request
    ) {
        return workspaceService.createWorkspace(accountId, request);
    }
}
