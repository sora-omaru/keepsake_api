package com.omaru.keepsake_api.controller;

import com.omaru.keepsake_api.dto.request.MemberCreateRequestDto;
import com.omaru.keepsake_api.dto.response.MemberResponseDto;
import com.omaru.keepsake_api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workspaces/{workspaceId}/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public List<MemberResponseDto> getMembers(@PathVariable Long workspaceId) {
        return memberService.getMembers(workspaceId);
    }

    @PostMapping
    public MemberResponseDto createMember(
            @PathVariable Long workspaceId,
            @RequestBody MemberCreateRequestDto request
    ) {
        return memberService.createMember(request, workspaceId );
    }
}
