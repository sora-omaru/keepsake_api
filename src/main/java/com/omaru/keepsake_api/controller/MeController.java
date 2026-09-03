package com.omaru.keepsake_api.controller;

import com.omaru.keepsake_api.dto.response.AccountResponseDto;
import com.omaru.keepsake_api.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/me")
public class MeController {
    private final AccountService accountService;

    @GetMapping
    public AccountResponseDto getMe(
            @AuthenticationPrincipal Long accountId
    ) {
        return accountService.getMe(accountId);
    }
}
