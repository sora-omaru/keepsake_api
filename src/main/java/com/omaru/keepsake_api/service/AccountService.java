package com.omaru.keepsake_api.service;

import com.omaru.keepsake_api.dto.account.GoogleAccountDto;
import com.omaru.keepsake_api.dto.response.AccountResponseDto;
import com.omaru.keepsake_api.entity.AccountEntity;

public interface AccountService {
    AccountEntity findOrCreateByGoogle(GoogleAccountDto googleAccount);

    AccountResponseDto getMe(Long accountId);
}
