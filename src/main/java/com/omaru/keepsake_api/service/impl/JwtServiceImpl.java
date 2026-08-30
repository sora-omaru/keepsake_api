package com.omaru.keepsake_api.service.impl;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
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
        // 秘密鍵を使って、JWTの署名・検証を行うHMAC SHA-256アルゴリズムを生成
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
                .withIssuer("keepsake-api")
                .withSubject(account.getId().toString())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                // ヘッダーとペイロードを秘密鍵で署名し、改ざんを検知できるJWTを生成
                .sign(algorithm);
    }

    @Override
    public Long verifyAndGetAccountId(String token) {

        // 発行時と同じアルゴリズムとissuerを検証条件として設定
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("keepsake-api")
                .build();

        // 署名・issuer・有効期限を検証し、正常な場合のみJWTの内容を取得
        DecodedJWT decodedJWT = verifier.verify(token);

        Long accountId = Long.valueOf(decodedJWT.getSubject());

        return accountId;
    }
}
