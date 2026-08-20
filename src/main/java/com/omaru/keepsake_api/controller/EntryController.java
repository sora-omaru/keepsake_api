package com.omaru.keepsake_api.controller;

import com.omaru.keepsake_api.dto.request.EntryCreateRequestDto;
import com.omaru.keepsake_api.dto.response.EntryResponseDto;
import com.omaru.keepsake_api.service.EntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workspaces/{workspaceId}/topics/{topicId}/entries")
public class EntryController {
    private final EntryService entryService;

    @GetMapping
    public List<EntryResponseDto> getEntries(
            @PathVariable Long workspaceId,
            @PathVariable Long topicId
    ) {
        return entryService.getEntries(workspaceId, topicId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntryResponseDto createEntry(
            @PathVariable Long workspaceId,
            @PathVariable Long topicId,
            @Valid @RequestBody EntryCreateRequestDto request
    ) {
        return entryService.createEntry(workspaceId, topicId, request);
    }
}
