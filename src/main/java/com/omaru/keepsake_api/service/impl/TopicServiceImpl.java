package com.omaru.keepsake_api.service.impl;

import com.omaru.keepsake_api.dto.request.TopicCreateRequestDto;
import com.omaru.keepsake_api.dto.response.TopicResponseDto;
import com.omaru.keepsake_api.entity.TopicEntity;
import com.omaru.keepsake_api.entity.WorkspaceEntity;
import com.omaru.keepsake_api.exception.ApiException;
import com.omaru.keepsake_api.repository.TopicRepository;
import com.omaru.keepsake_api.repository.WorkspaceRepository;
import com.omaru.keepsake_api.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final WorkspaceRepository workspaceRepository;

    @Override
    public List<TopicResponseDto> getTopics(Long workspaceId) {
        getWorkspace(workspaceId);

        return topicRepository.findByWorkspace_Id(workspaceId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public TopicResponseDto createTopic(TopicCreateRequestDto request, Long workspaceId) {
        WorkspaceEntity workspace = getWorkspace(workspaceId);
        String name = request.name().trim();

        if (topicRepository.existsByWorkspaceIdAndName(workspaceId, name)) {
            throw new ApiException(HttpStatus.CONFLICT, "この名前はすでに登録されています。");
        }

        TopicEntity topic = new TopicEntity();
        topic.setWorkspace(workspace);
        topic.setName(name);
        topic.setDescription(request.description());

        return toResponse(topicRepository.save(topic));
    }

    private WorkspaceEntity getWorkspace(Long workspaceId) {
        return workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "ワークスペースが見つかりません。"
                ));
    }

    private TopicResponseDto toResponse(TopicEntity topic) {
        return new TopicResponseDto(
                topic.getId(),
                topic.getWorkspace().getId(),
                topic.getName(),
                topic.getDescription()
        );
    }
}
