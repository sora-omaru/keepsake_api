package com.omaru.keepsake_api.controller;

import com.omaru.keepsake_api.dto.request.MemberCreateRequestDto;
import com.omaru.keepsake_api.dto.response.MemberResponseDto;
import com.omaru.keepsake_api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workspaces/{workspaceId}/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public MemberResponseDto createMember(
            @PathVariable Long workspaceId,
            @RequestBody MemberCreateRequestDto request
    ) {
        return memberService.createMember(request, workspaceId );
    }
}
