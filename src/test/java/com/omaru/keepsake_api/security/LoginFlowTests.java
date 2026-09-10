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
    @Import({SecurityConfig.class, CorsConfig.class, SuccessHandler.class, MeController.class, com.omaru.keepsake_api.controller.CsrfController.class, com.omaru.keepsake_api.controller.WorkspaceController.class, com.omaru.keepsake_api.controller.TopicController.class, WorkspaceAccess.class})
    static class Config {
        @Bean com.omaru.keepsake_api.repository.WorkspaceRepository workspaceRepository() { return mock(com.omaru.keepsake_api.repository.WorkspaceRepository.class); }
        @Bean com.omaru.keepsake_api.service.WorkspaceService workspaceService() { return mock(com.omaru.keepsake_api.service.WorkspaceService.class); }
        @Bean com.omaru.keepsake_api.service.TopicService topicService() { return mock(com.omaru.keepsake_api.service.TopicService.class); }
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
        assertEquals("http://localhost:3000/", response.getRedirectedUrl());
        Cookie cookie = response.getCookie("access_token");
        assertNotNull(cookie);
        assertTrue(cookie.isHttpOnly());
        assertEquals("/", cookie.getPath());
        assertEquals(3600, cookie.getMaxAge());

        mvc().perform(get("/api/v1/me").cookie(cookie)
                        .header("Origin", "http://localhost:5173"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"))
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"))
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test void workspaceMembershipAndCsrfAreEnforced() throws Exception {
        when(jwtService.verifyAndGetAccountId("member-token")).thenReturn(7L);
        Cookie cookie = new Cookie("access_token", "member-token");
        var repository = context.getBean(com.omaru.keepsake_api.repository.WorkspaceRepository.class);
        when(repository.hasAccount(10L, 7L)).thenReturn(true);
        when(repository.hasAccount(20L, 7L)).thenReturn(false);
        mvc().perform(get("/api/v1/workspaces/10/topics").cookie(cookie)).andExpect(status().isOk());
        mvc().perform(get("/api/v1/workspaces/20/topics").cookie(cookie)).andExpect(status().isForbidden());
        mvc().perform(get("/api/v1/workspaces")).andExpect(status().isUnauthorized());
        mvc().perform(get("/api/v1/workspaces").cookie(cookie)).andExpect(status().isOk());
        verify(context.getBean(com.omaru.keepsake_api.service.WorkspaceService.class)).getWorkspaces(7L);

        mvc().perform(post("/api/v1/workspaces").cookie(cookie)
                .contentType("application/json").content("{\"name\":\"Test\"}"))
                .andExpect(status().isForbidden());
        var result = mvc().perform(get("/api/v1/csrf").cookie(cookie))
                .andExpect(status().isOk()).andReturn();
        var token = (org.springframework.security.web.csrf.CsrfToken) result.getRequest()
                .getAttribute(org.springframework.security.web.csrf.CsrfToken.class.getName());
        var session = (org.springframework.mock.web.MockHttpSession) result.getRequest().getSession(false);
        mvc().perform(post("/api/v1/workspaces").cookie(cookie).session(session)
                .header(token.getHeaderName(), token.getToken())
                .contentType("application/json").content("{\"name\":\"Test\"}"))
                .andExpect(status().isCreated());
        mvc().perform(post("/api/v1/workspaces/20/topics").cookie(cookie).session(session)
                .header(token.getHeaderName(), token.getToken())
                .contentType("application/json").content("{\"name\":\"Test\"}"))
                .andExpect(status().isForbidden());
        mvc().perform(options("/api/v1/workspaces").header("Origin", "http://localhost:5173")
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "Content-Type,X-CSRF-TOKEN"))
                .andExpect(status().isOk());
    }
}
