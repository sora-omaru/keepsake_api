package com.omaru.keepsake_api.controller;

import com.omaru.keepsake_api.dto.request.TagCreateRequestDto;
import com.omaru.keepsake_api.dto.response.TagResponseDto;
import com.omaru.keepsake_api.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workspaces/{workspaceId}/tag")
public class TagController {
    private final TagService tagService;

    @GetMapping
    public List<TagResponseDto> getTags(@PathVariable Long workspaceId) {
        return tagService.getTags(workspaceId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagResponseDto createTag(
            @Valid @RequestBody TagCreateRequestDto request,
            @PathVariable Long workspaceId
    ) {
        return tagService.createTag(request, workspaceId);
    }
}
