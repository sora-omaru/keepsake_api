package com.omaru.keepsake_api.service.impl;

import com.omaru.keepsake_api.dto.request.TagCreateRequestDto;
import com.omaru.keepsake_api.dto.response.TagResponseDto;
import com.omaru.keepsake_api.entity.TagEntity;
import com.omaru.keepsake_api.entity.WorkspaceEntity;
import com.omaru.keepsake_api.exception.ApiException;
import com.omaru.keepsake_api.repository.TagRepository;
import com.omaru.keepsake_api.repository.WorkspaceRepository;
import com.omaru.keepsake_api.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final WorkspaceRepository workspaceRepository;
    private final TagRepository tagRepository;

    @Override
    public List<TagResponseDto> getTags(Long workspaceId) {
        getWorkspace(workspaceId);

        return tagRepository.findByWorkspace_Id(workspaceId).stream().map(this::toResponse).toList();
    }

    @Override
    public TagResponseDto createTag(TagCreateRequestDto request, Long workspaceId) {
        WorkspaceEntity workspace = getWorkspace(workspaceId);
        String name = request.name().trim();
        TagEntity tag = new TagEntity();
        tag.setWorkspace(workspace);
        tag.setName(name);


        return toResponse(tagRepository.save(tag));
    }

    private WorkspaceEntity getWorkspace(Long workspaceId) {
        return workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND, "ワークスペースが見つかりません"
                ));
    }

    private TagResponseDto toResponse(TagEntity tag) {
        return new TagResponseDto(tag.getId(), tag.getName(), tag.getWorkspace().getId());
    }
}
