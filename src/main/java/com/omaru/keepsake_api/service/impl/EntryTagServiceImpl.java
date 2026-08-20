package com.omaru.keepsake_api.service.impl;

import com.omaru.keepsake_api.dto.response.TagResponseDto;
import com.omaru.keepsake_api.entity.EntryEntity;
import com.omaru.keepsake_api.entity.EntryTagEntity;
import com.omaru.keepsake_api.entity.EntryTagId;
import com.omaru.keepsake_api.entity.TagEntity;
import com.omaru.keepsake_api.exception.ApiException;
import com.omaru.keepsake_api.repository.EntryRepository;
import com.omaru.keepsake_api.repository.EntryTagRepository;
import com.omaru.keepsake_api.repository.TagRepository;
import com.omaru.keepsake_api.service.EntryTagService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EntryTagServiceImpl implements EntryTagService {
    private final TagRepository tagRepository;
    private final EntryRepository entryRepository;
    private final EntryTagRepository entryTagRepository;

    //TagとEntryを紐づける
    @Override
    @Transactional
    public void addTag(Long workspaceId, Long topicId, Long entryId, Long tagId) {
        EntryEntity entry = getEntry(workspaceId, topicId, entryId);
        TagEntity tag = getTag(workspaceId, tagId);

        //すでに紐づいているのであればreturn
        if (entryTagRepository.existsByEntry_IdAndTag_Id(entryId, tagId)) {
            return;
        }


        EntryTagEntity entryTag = new EntryTagEntity();
        entryTag.setId(new EntryTagId(entryId, tagId));
        entryTag.setWorkspace(entry.getWorkspace());
        entryTag.setEntry(entry);
        entryTag.setTag(tag);

        entryTagRepository.save(entryTag);
    }

    //TagとEntryの紐づけ解除
    @Override
    @Transactional
    public void removeTag(Long workspaceId, Long topicId, Long entryId, Long tagId) {
        getEntry(workspaceId, topicId, entryId);
        getTag(workspaceId, tagId);

        entryTagRepository.deleteByEntry_IdAndTag_Id(entryId, tagId);
    }

    @Override
    @Transactional
    public List<TagResponseDto> getTags(Long workspaceId, Long topicId, Long entryId) {
        getEntry(workspaceId, topicId, entryId);

        return entryTagRepository.findTagsByEntryId(entryId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private EntryEntity getEntry(Long workspaceId, Long topicId, Long entryId) {
        return entryRepository.findByIdAndWorkspace_IdAndTopic_Id(entryId, workspaceId, topicId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Entryが見つかりません"));
    }

    private TagEntity getTag(Long workspaceId, Long tagId) {
        return tagRepository.findByIdAndWorkspace_Id(tagId, workspaceId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Tagが見つかりません"));
    }

    private TagResponseDto toResponse(TagEntity tag) {
        return new TagResponseDto(
                tag.getId(),
                tag.getName(),
                tag.getWorkspace().getId()
        );
    }
}
