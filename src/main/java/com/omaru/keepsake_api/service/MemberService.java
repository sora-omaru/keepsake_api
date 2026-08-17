package com.omaru.keepsake_api.service;

import com.omaru.keepsake_api.dto.request.MemberCreateRequestDto;
import com.omaru.keepsake_api.dto.response.MemberResponseDto;

import java.util.List;

public interface MemberService {
    List<MemberResponseDto> getMembers(Long workspaceId);

    public MemberResponseDto createMember(MemberCreateRequestDto requestDto, Long workspaceId);
}
