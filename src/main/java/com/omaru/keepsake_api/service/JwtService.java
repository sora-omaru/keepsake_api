package com.omaru.keepsake_api.service;

import com.omaru.keepsake_api.entity.AccountEntity;

public interface JwtService {
    String generateAccessToken(AccountEntity account);
}
