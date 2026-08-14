package com.omaru.keepsake_api.service.impl;

import com.omaru.keepsake_api.dto.request.MemberCreateRequestDto;
import com.omaru.keepsake_api.dto.response.MemberResponseDto;
import com.omaru.keepsake_api.entity.MemberEntity;
import com.omaru.keepsake_api.entity.WorkspaceEntity;
import com.omaru.keepsake_api.repository.MemberRepository;
import com.omaru.keepsake_api.repository.WorkspaceRepository;
import com.omaru.keepsake_api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final WorkspaceRepository workspaceRepository;

    @Override
    public MemberResponseDto createMember(MemberCreateRequestDto request, Long workspaceId) {
        WorkspaceEntity workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new RuntimeException("Workspace Not Found"));

        MemberEntity member = new MemberEntity();
        member.setWorkspace(workspace);
        member.setName(request.name());

        MemberEntity saved = memberRepository.save(member);
        return new MemberResponseDto(saved.getId(), saved.getWorkspace().getId(), saved.getName());
    }
}
