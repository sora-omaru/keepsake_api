package com.omaru.keepsake_api.service.impl;

import com.omaru.keepsake_api.dto.request.EntryCreateRequestDto;
import com.omaru.keepsake_api.dto.response.EntryResponseDto;
import com.omaru.keepsake_api.entity.EntryEntity;
import com.omaru.keepsake_api.entity.MemberEntity;
import com.omaru.keepsake_api.entity.TopicEntity;
import com.omaru.keepsake_api.entity.WorkspaceEntity;
import com.omaru.keepsake_api.exception.ApiException;
import com.omaru.keepsake_api.repository.EntryRepository;
import com.omaru.keepsake_api.repository.MemberRepository;
import com.omaru.keepsake_api.repository.TopicRepository;
import com.omaru.keepsake_api.repository.WorkspaceRepository;
import com.omaru.keepsake_api.service.EntryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {
    private final WorkspaceRepository workspaceRepository;
    private final TopicRepository topicRepository;
    private final MemberRepository memberRepository;
    private final EntryRepository entryRepository;

    @Override
    @Transactional
    public EntryResponseDto createEntry(Long workspaceId, EntryCreateRequestDto request) {
        WorkspaceEntity workspace = getWorkspace(workspaceId);
        TopicEntity topic = getTopic(workspaceId, request.topicId());
        MemberEntity member = getMember(workspaceId, request.memberId());

        EntryEntity entry = new EntryEntity();
        entry.setWorkspace(workspace);
        entry.setTopic(topic);
        entry.setMember(member);
        entry.setTitle(request.title().trim());
        entry.setContent(request.content());

        return toResponse(entryRepository.save(entry));
    }

    //Workspace検索用
    private WorkspaceEntity getWorkspace(Long workspaceId) {
        return workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND, "ワークスペースが見つかりません"
                ));
    }

    //Topic検索用
    private TopicEntity getTopic(Long workspaceId, Long topicId) {
        return topicRepository
                .findByIdAndWorkspace_Id(topicId, workspaceId)
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "トピックが見つかりません"
                        )
                );
    }

    //Member検索用
    private MemberEntity getMember(Long workspaceId, Long memberId) {
        return memberRepository.findByIdAndWorkspace_Id(memberId, workspaceId).orElseThrow(() ->
                new ApiException(
                        HttpStatus.NOT_FOUND,
                        "メンバーが見つかりません"
                ));
    }

    //変換用メソッド
    private EntryResponseDto toResponse(EntryEntity entry) {
        return new EntryResponseDto(
                entry.getId(),
                entry.getWorkspace().getId(),
                entry.getTopic().getId(),
                entry.getMember().getId(),
                entry.getTitle(),
                entry.getContent()
        );
    }
}
