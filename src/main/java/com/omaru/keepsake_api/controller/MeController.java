package com.omaru.keepsake_api.controller;

import com.omaru.keepsake_api.dto.response.AccountResponseDto;
import com.omaru.keepsake_api.service.AccountService;
import lombok.RequiredArgsConstructor;
import com.omaru.keepsake_api.dto.request.AccountUpdateRequestDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/me")
public class MeController {
    private final AccountService accountService;

    @PatchMapping
    public AccountResponseDto updateMe(
            @AuthenticationPrincipal Long accountId,
            @Valid @RequestBody AccountUpdateRequestDto request
    ) {
        return accountService.updateDisplayName(accountId, request.displayName());
    }

    @GetMapping
    public AccountResponseDto getMe(
            @AuthenticationPrincipal Long accountId
    ) {
        return accountService.getMe(accountId);
    }
}
