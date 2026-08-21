package com.omaru.keepsake_api.service.impl;

import com.omaru.keepsake_api.dto.request.EntryCreateRequestDto;
import com.omaru.keepsake_api.dto.request.EntryUpdateRequestDto;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {
    private final WorkspaceRepository workspaceRepository;
    private final TopicRepository topicRepository;
    private final MemberRepository memberRepository;
    private final EntryRepository entryRepository;

    @Override
    @Transactional
    public List<EntryResponseDto> getEntries(Long workspaceId, Long topicId) {
        getTopic(workspaceId, topicId);

        return entryRepository.findByWorkspace_IdAndTopic_Id(workspaceId, topicId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public EntryResponseDto createEntry(Long workspaceId, Long topicId, EntryCreateRequestDto request) {
        WorkspaceEntity workspace = getWorkspace(workspaceId);
        TopicEntity topic = getTopic(workspaceId, topicId);
        MemberEntity member = getMember(workspaceId, request.memberId());

        EntryEntity entry = new EntryEntity();
        entry.setWorkspace(workspace);
        entry.setTopic(topic);
        entry.setMember(member);
        entry.setTitle(request.title().trim());
        entry.setContent(request.content());

        return toResponse(entryRepository.save(entry));
    }

    @Override
    @Transactional
    public EntryResponseDto updateEntry(Long workspaceId, Long topicId, Long entryId, EntryUpdateRequestDto request) {

        EntryEntity entry = getEntry(workspaceId, topicId, entryId);

        entry.setTitle(request.title().trim());
        entry.setContent(request.content());

        return toResponse(entryRepository.save(entry));

    }

    @Override
    @Transactional
    public void deleteEntry(Long workspaceId, Long topicId, Long entryId) {
        EntryEntity entry = getEntry(workspaceId, topicId, entryId);
        entryRepository.delete(entry);
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

    //Entry検索用メソッド
    private EntryEntity getEntry(Long workspaceId, Long topicId, Long entryId) {
        return entryRepository.findByIdAndWorkspace_IdAndTopic_Id(entryId, workspaceId, topicId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Entryが見つかりません"));
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
