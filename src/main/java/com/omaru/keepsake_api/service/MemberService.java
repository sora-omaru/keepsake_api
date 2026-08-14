package com.omaru.keepsake_api.service;

import com.omaru.keepsake_api.dto.request.MemberCreateRequestDto;
import com.omaru.keepsake_api.dto.response.MemberResponseDto;

public interface MemberService {
    MemberResponseDto createMember(MemberCreateRequestDto requestDto, Long workspaceId);
}
