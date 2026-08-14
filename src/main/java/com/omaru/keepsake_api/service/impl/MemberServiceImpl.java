package com.omaru.keepsake_api.service.impl;

import com.omaru.keepsake_api.dto.request.MemberCreateRequestDto;
import com.omaru.keepsake_api.dto.response.MemberResponseDto;
import com.omaru.keepsake_api.entity.MemberEntity;
import com.omaru.keepsake_api.entity.WorkspaceEntity;
import com.omaru.keepsake_api.exception.ApiException;
import com.omaru.keepsake_api.repository.MemberRepository;
import com.omaru.keepsake_api.repository.WorkspaceRepository;
import com.omaru.keepsake_api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final WorkspaceRepository workspaceRepository;

    @Override
    public List<MemberResponseDto> getMembers(Long workspaceId) {
        WorkspaceEntity workspace = workspaceRepository.findById(workspaceId).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, "ワークスペースが見つかりません。"));

        return memberRepository.findByWorkspace_Id(workspaceId)
                .stream()
                .map(member ->
                        new MemberResponseDto(member.getId(), member.getWorkspace().getId(), member.getName())).toList();
    }

    @Override
    public MemberResponseDto createMember(MemberCreateRequestDto request, Long workspaceId) {
        WorkspaceEntity workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "ワークスペースが見つかりません。"));

        if (memberRepository.existsByWorkspaceIdAndName(workspaceId, request.name())) {
            throw new ApiException(HttpStatus.CONFLICT, "この名前はすでに登録されています。");
        }
        MemberEntity member = new MemberEntity();
        member.setWorkspace(workspace);
        member.setName(request.name());

        MemberEntity saved = memberRepository.save(member);
        return new MemberResponseDto(saved.getId(), saved.getWorkspace().getId(), saved.getName());
    }
}
