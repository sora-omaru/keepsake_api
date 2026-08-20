package com.omaru.keepsake_api.controller;

import com.omaru.keepsake_api.dto.request.EntryCreateRequestDto;
import com.omaru.keepsake_api.dto.response.EntryResponseDto;
import com.omaru.keepsake_api.dto.response.TagResponseDto;
import com.omaru.keepsake_api.service.EntryService;
import com.omaru.keepsake_api.service.EntryTagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workspaces/{workspaceId}/topics/{topicId}/entries")
public class EntryController {
    private final EntryService entryService;
    private final EntryTagService entryTagService;

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

    @GetMapping("/{entryId}/tags")
    public List<TagResponseDto> getTags(
            @PathVariable Long workspaceId,
            @PathVariable Long topicId,
            @PathVariable Long entryId
    ) {
        return entryTagService.getTags(workspaceId, topicId, entryId);
    }

    @PutMapping("/{entryId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addTag(
            @PathVariable Long workspaceId,
            @PathVariable Long topicId,
            @PathVariable Long entryId,
            @PathVariable Long tagId
    ) {
        entryTagService.addTag(workspaceId, topicId, entryId, tagId);
    }

    @DeleteMapping("/{entryId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTag(
            @PathVariable Long workspaceId,
            @PathVariable Long topicId,
            @PathVariable Long entryId,
            @PathVariable Long tagId
    ) {
        entryTagService.removeTag(workspaceId, topicId, entryId, tagId);
    }
}
