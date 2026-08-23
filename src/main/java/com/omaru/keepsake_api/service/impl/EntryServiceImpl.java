package com.omaru.keepsake_api.service.impl;

// import com.omaru.keepsake_api.dto.EntryTagProjection;
import com.omaru.keepsake_api.dto.request.EntryCreateRequestDto;
import com.omaru.keepsake_api.dto.request.EntryUpdateRequestDto;
import com.omaru.keepsake_api.dto.response.EntryResponseDto;
// import com.omaru.keepsake_api.dto.response.TagResponseDto;
import com.omaru.keepsake_api.entity.EntryEntity;
import com.omaru.keepsake_api.entity.MemberEntity;
import com.omaru.keepsake_api.entity.TopicEntity;
import com.omaru.keepsake_api.entity.WorkspaceEntity;
import com.omaru.keepsake_api.exception.ApiException;
import com.omaru.keepsake_api.repository.*;
import com.omaru.keepsake_api.service.EntryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {
    private final WorkspaceRepository workspaceRepository;
    private final TopicRepository topicRepository;
    private final MemberRepository memberRepository;
    private final EntryRepository entryRepository;
    // Entry一覧へTagを含める場合に使用する。
    // private final EntryTagRepository entryTagRepository;

    @Override
    @Transactional
    public List<EntryResponseDto> getEntries(Long workspaceId, Long topicId) {
        getTopic(workspaceId, topicId);

        // 現在はEntryだけを取得し、Tag取得クエリは実行しない。
        return entryRepository.findByWorkspace_IdAndTopic_Id(workspaceId, topicId)
                .stream()
                .map(this::toResponse)
                .toList();

        // Entry一覧へTagを含める場合は、以下のように一括取得してEntry IDごとにまとめる。
        // List<EntryEntity> entries = entryRepository.findByWorkspace_IdAndTopic_Id(workspaceId, topicId);
        // if (entries.isEmpty()) {
        //     return List.of();
        // }
        // List<Long> entryIds = entries.stream().map(EntryEntity::getId).toList();
        // Map<Long, List<TagResponseDto>> tagsByEntryId =
        //         entryTagRepository.findTagsByEntryIds(workspaceId, entryIds)
        //                 .stream()
        //                 .collect(Collectors.groupingBy(
        //                         EntryTagProjection::getEntryId,
        //                         Collectors.mapping(
        //                                 tag -> new TagResponseDto(
        //                                         tag.getTagId(),
        //                                         tag.getTagName(),
        //                                         tag.getWorkspaceId()
        //                                 ),
        //                                 Collectors.toList()
        //                         )
        //                 ));
        // return entries.stream()
        //         .map(entry -> toResponse(
        //                 entry,
        //                 tagsByEntryId.getOrDefault(entry.getId(), List.of())
        //         ))
        //         .toList();
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

        // Entry更新レスポンスにTagを含める場合に使用する。
        // EntryEntity savedEntry = entryRepository.save(entry);
        // List<TagResponseDto> tags = entryTagRepository.findTagsByEntryId(entryId)
        //         .stream()
        //         .map(this::toTagResponse)
        //         .toList();
        // return toResponse(savedEntry, tags);

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

    // Entry更新レスポンスにTagを含める場合の変換処理。
    // private TagResponseDto toTagResponse(TagEntity tag) {
    //     return new TagResponseDto(
    //             tag.getId(),
    //             tag.getName(),
    //             tag.getWorkspace().getId()
    //     );
    // }
}
