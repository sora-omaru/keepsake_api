package com.omaru.keepsake_api.security;

import com.omaru.keepsake_api.config.CorsConfig;
import com.omaru.keepsake_api.config.SecurityConfig;
import com.omaru.keepsake_api.controller.MeController;
import com.omaru.keepsake_api.dto.response.AccountResponseDto;
import com.omaru.keepsake_api.entity.AccountEntity;
import com.omaru.keepsake_api.service.AccountService;
import com.omaru.keepsake_api.service.JwtService;
import jakarta.servlet.Filter;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig(LoginFlowTests.Config.class)
@WebAppConfiguration
@TestPropertySource(properties = "app.cors.allowed-origins=http://localhost:5173")
class LoginFlowTests {
    @Configuration
    @EnableWebMvc
    @EnableWebSecurity
    @Import({SecurityConfig.class, CorsConfig.class, SuccessHandler.class, MeController.class})
    static class Config {
        @Bean AccountService accountService() { return mock(AccountService.class); }
        @Bean JwtService jwtService() { return mock(JwtService.class); }
        @Bean ClientRegistrationRepository clientRegistrationRepository() {
            return new InMemoryClientRegistrationRepository(ClientRegistration.withRegistrationId("google")
                    .clientId("test-client").clientSecret("test-secret")
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                    .authorizationUri("https://example.com/authorize")
                    .tokenUri("https://example.com/token")
                    .userInfoUri("https://example.com/userinfo")
                    .userNameAttributeName("sub").build());
        }
    }

    @Autowired WebApplicationContext context;
    @Autowired AccountService accountService;
    @Autowired JwtService jwtService;
    @Autowired SuccessHandler successHandler;

    private MockMvc mvc() {
        return MockMvcBuilders.webAppContextSetup(context)
                .addFilters(context.getBean("springSecurityFilterChain", Filter.class)).build();
    }

    @Test void allowedPreflightWorksWithoutAuthentication() throws Exception {
        mvc().perform(options("/api/v1/me")
                        .header("Origin", "http://localhost:5173")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Access-Control-Request-Headers", "Content-Type"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"))
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
    }

    @Test void unapprovedOriginIsRejected() throws Exception {
        mvc().perform(options("/api/v1/me")
                        .header("Origin", "https://unapproved.example")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test void loginCookieAuthenticatesRedirectDestination() throws Exception {
        OidcUser user = mock(OidcUser.class);
        when(user.getSubject()).thenReturn("google-sub");
        AccountEntity account = new AccountEntity();
        account.setId(42L);
        when(accountService.findOrCreateByGoogle(any())).thenReturn(account);
        when(jwtService.generateAccessToken(account)).thenReturn("test-access-token");
        when(jwtService.verifyAndGetAccountId("test-access-token")).thenReturn(42L);
        when(accountService.getMe(42L)).thenReturn(
                new AccountResponseDto(42L, "test@example.com", "Test User", null));

        MockHttpServletResponse response = new MockHttpServletResponse();
        successHandler.onAuthenticationSuccess(new MockHttpServletRequest(), response,
                new UsernamePasswordAuthenticationToken(user, null));

        assertEquals(302, response.getStatus());
        assertEquals("/api/v1/me", response.getRedirectedUrl());
        Cookie cookie = response.getCookie("access_token");
        assertNotNull(cookie);
        assertTrue(cookie.isHttpOnly());
        assertEquals("/", cookie.getPath());
        assertEquals(3600, cookie.getMaxAge());

        mvc().perform(get(response.getRedirectedUrl()).cookie(cookie)
                        .header("Origin", "http://localhost:5173"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"))
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"))
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }
}
