package com.omaru.keepsake_api.controller;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkspaceResponseDto create(
            @Valid @RequestBody WorkspaceCreateRequestDto request
    ) {
        return workspaceService.createWorkspace(request);
    }
}
