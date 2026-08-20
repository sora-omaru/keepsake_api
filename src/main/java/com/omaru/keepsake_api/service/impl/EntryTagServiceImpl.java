package com.omaru.keepsake_api.service.impl;

import com.omaru.keepsake_api.entity.EntryEntity;
import com.omaru.keepsake_api.entity.EntryTagEntity;
import com.omaru.keepsake_api.entity.EntryTagId;
import com.omaru.keepsake_api.entity.TagEntity;
import com.omaru.keepsake_api.exception.ApiException;
import com.omaru.keepsake_api.repository.EntryRepository;
import com.omaru.keepsake_api.repository.EntryTagRepository;
import com.omaru.keepsake_api.repository.TagRepository;
import com.omaru.keepsake_api.repository.WorkspaceRepository;
import com.omaru.keepsake_api.service.EntryTagService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntryTagServiceImpl implements EntryTagService {
    private final TagRepository tagRepository;
    private final EntryRepository entryRepository;
    private final EntryTagRepository entryTagRepository;


    @Override
    @Transactional
    public void addTag(Long workspaceId, Long topicId, Long entryId, Long tagId) {
        //EntryがWorkspaceとTopicに紐づいているかを見ている
        EntryEntity entry = entryRepository.findByIdAndWorkspace_IdAndTopic_Id(
                        entryId, workspaceId, topicId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "Entryが見つかりません"
                ));
        //Tagがworkspaceに紐づいているかを確認
        TagEntity tag = tagRepository.findByIdAndWorkspace_Id(tagId, workspaceId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "Tagが見つかりません"
                ));
       //すでに紐づいているのであればreturn
        if (entryTagRepository.existsByEntry_IdAndTag_Id(entryId, tagId)) {
            return;
        }


        EntryTagEntity entryTag = new EntryTagEntity();
        entryTag.setId(new EntryTagId(entryId,tagId));
        entryTag.setWorkspace(entry.getWorkspace());
        entryTag.setEntry(entry);
        entryTag.setTag(tag);

        entryTagRepository.save(entryTag);

    }

}
