package com.omaru.keepsake_api.service.impl;

import com.omaru.keepsake_api.dto.account.GoogleAccountDto;
import com.omaru.keepsake_api.entity.AccountEntity;
import com.omaru.keepsake_api.repository.AccountRepository;
import com.omaru.keepsake_api.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    //googleAccountのSubの有無でアカウントの作成か、更新かを判断する。
    @Override
    @Transactional
    public AccountEntity findOrCreateByGoogle(GoogleAccountDto googleAccount) {

        return accountRepository.findByGoogleSub(googleAccount.googleSub()).map(
                        account -> {
                            account.setEmail(googleAccount.email());
                            account.setDisplayName(googleAccount.displayName());
                            account.setPictureUrl(googleAccount.pictureUrl());
                            return account;
                        })
                .orElseGet(() -> {
                    AccountEntity account = new AccountEntity();
                    account.setGoogleSub(account.getGoogleSub());
                    account.setEmail(account.getEmail());
                    account.setDisplayName(account.getDisplayName());
                    account.setPictureUrl(account.getPictureUrl());

                    return accountRepository.save(account);
                });

    }
}
