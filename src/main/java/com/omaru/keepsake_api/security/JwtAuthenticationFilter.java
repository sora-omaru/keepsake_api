package com.omaru.keepsake_api.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.omaru.keepsake_api.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = getAccessToken(request);

        // ① Cookieからtokenを取得できているか確認
        System.out.println("① token exists = " + (token != null));
        System.out.println(
                "authentication = "
                        + SecurityContextHolder.getContext().getAuthentication()
        );
        // Cookie JWT takes precedence over authentication restored from the OAuth2 session.
        if (token != null) {
            try {

                // ② JWT検証処理に入ったか
                System.out.println("② JWT検証開始");

                Long accountId = jwtService.verifyAndGetAccountId(token);

                System.out.println("③ accountId = " + accountId);
                var authentication =
                        new UsernamePasswordAuthenticationToken(
                                accountId,
                                null,
                                Collections.emptyList()
                        );
//         認証済みであると伝える
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            } catch (JWTVerificationException | NumberFormatException exception) {
                //不正・期限切れJWTは未認証として扱う
                // ④ JWT検証で失敗した場合
                System.out.println("④ JWT検証失敗");
                exception.printStackTrace();
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }


    private String getAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;

        }

        for (Cookie cookie : cookies) {
            if ("access_token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;

    }
}
