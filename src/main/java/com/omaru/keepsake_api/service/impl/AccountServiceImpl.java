package com.omaru.keepsake_api.service.impl;

import com.omaru.keepsake_api.dto.account.GoogleAccountDto;
import com.omaru.keepsake_api.dto.response.AccountResponseDto;
import com.omaru.keepsake_api.entity.AccountEntity;
import com.omaru.keepsake_api.exception.ApiException;
import com.omaru.keepsake_api.repository.AccountRepository;
import com.omaru.keepsake_api.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
                            // Preserve the name chosen by the user across logins.
                            account.setPictureUrl(googleAccount.pictureUrl());
                            return account;
                        })
                .orElseGet(() -> {
                    AccountEntity account = new AccountEntity();
                    account.setGoogleSub(googleAccount.googleSub());
                    account.setEmail(googleAccount.email());
                    account.setDisplayName(googleAccount.displayName());
                    account.setPictureUrl(googleAccount.pictureUrl());

                    return accountRepository.save(account);
                });

    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public AccountResponseDto getMe(Long accountId) {

        //accountIdがnullの場合の例外
        if (accountId == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "認証が必要です");
        }

        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "アカウントが見つかりません"));

        return new AccountResponseDto(
                account.getId(),
                account.getEmail(),
                account.getDisplayName(),
                account.getPictureUrl()
        );
    }

    @Override
    @Transactional
    public AccountResponseDto updateDisplayName(Long accountId, String displayName) {
        if (accountId == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "認証が必要です");
        }
        if (displayName == null || displayName.isBlank() || displayName.length() > 100) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "作成者名を1〜100文字で入力してください");
        }
        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "アカウントが見つかりません"));
        account.setDisplayName(displayName.strip());
        accountRepository.save(account);
        return new AccountResponseDto(account.getId(), account.getEmail(), account.getDisplayName(), account.getPictureUrl());
    }

}
