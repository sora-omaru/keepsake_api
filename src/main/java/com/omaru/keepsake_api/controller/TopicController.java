package com.omaru.keepsake_api.controller;

import com.omaru.keepsake_api.dto.request.TopicCreateRequestDto;
import com.omaru.keepsake_api.dto.response.TopicResponseDto;
import com.omaru.keepsake_api.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workspaces/{workspaceId}/topics")
public class TopicController {
    private final TopicService topicService;

    @GetMapping
    public List<TopicResponseDto> getTopics(@PathVariable Long workspaceId) {
        return topicService.getTopics(workspaceId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TopicResponseDto createTopic(
            @PathVariable Long workspaceId,
            @Valid @RequestBody TopicCreateRequestDto request
    ) {
        return topicService.createTopic(request, workspaceId);
    }
}
