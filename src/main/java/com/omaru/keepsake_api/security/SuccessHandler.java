package com.omaru.keepsake_api.security;

import com.omaru.keepsake_api.dto.account.GoogleAccountDto;
import com.omaru.keepsake_api.entity.AccountEntity;
import com.omaru.keepsake_api.service.AccountService;
import com.omaru.keepsake_api.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

//Google認証成功時の処理
@Component
@RequiredArgsConstructor
public class SuccessHandler implements AuthenticationSuccessHandler {
    private final AccountService accountService;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        //AuthenticationからGoogleのアカウント情報を取得
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        GoogleAccountDto googleAccount = new GoogleAccountDto(
                oidcUser.getSubject(),
                oidcUser.getEmail(),
                oidcUser.getFullName(),
                oidcUser.getPicture()
        );

        AccountEntity account = accountService.findOrCreateByGoogle(googleAccount);

        String accessToken = jwtService.generateAccessToken(account);

        Cookie cookie = new Cookie("access_token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);//1時間

        response.addCookie(cookie);
        //フロントができまでは、ログイン中のユーザー情報を表示する。
        response.sendRedirect("/api/v1/me");

    }

}
