package com.omaru.keepsake_api.service;

import com.omaru.keepsake_api.dto.request.TopicCreateRequestDto;
import com.omaru.keepsake_api.dto.response.TopicResponseDto;

import java.util.List;

public interface TopicService {
    List<TopicResponseDto> getTopics(Long workspaceId);

    TopicResponseDto createTopic(TopicCreateRequestDto request, Long workspaceId);
}
