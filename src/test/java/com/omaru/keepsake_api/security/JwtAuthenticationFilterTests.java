package com.omaru.keepsake_api.security;

import com.omaru.keepsake_api.service.JwtService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTests {
    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void jwtReplacesExistingOAuth2Authentication() throws Exception {
        JwtService jwtService = mock(JwtService.class);
        when(jwtService.verifyAndGetAccountId("valid-token")).thenReturn(42L);
        SecurityContextHolder.getContext().setAuthentication(
                new OAuth2AuthenticationToken(mock(OidcUser.class), Collections.emptyList(), "google"));
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("access_token", "valid-token"));

        new JwtAuthenticationFilter(jwtService).doFilter(request, new MockHttpServletResponse(),
                (req, res) -> {
                    var authentication = SecurityContextHolder.getContext().getAuthentication();
                    assertTrue(authentication.isAuthenticated());
                    assertEquals(42L, authentication.getPrincipal());
                });

        verify(jwtService).verifyAndGetAccountId("valid-token");
    }
}
