package com.omaru.keepsake_api.service.impl;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.omaru.keepsake_api.entity.AccountEntity;
import com.omaru.keepsake_api.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class JwtServiceImpl implements JwtService {
    private static final long EXPIRATION_TIME =
            1000L * 60L * 60L * 1L;

    private final Algorithm algorithm;

    public JwtServiceImpl(@Value("${app.jwt.secret}") String secretKey) {
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    @Override
    public String generateAccessToken(AccountEntity account) {

        //JWTを発行した時間
        Date issuedAt = new Date();
        //JWTが失効する時間
        Date expiresAt = new Date(issuedAt.getTime() + EXPIRATION_TIME);

//        JWT作成
        return JWT.create()
                .withIssuer("Keepsake-api")
                .withSubject(account.getId().toString())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    }
}
