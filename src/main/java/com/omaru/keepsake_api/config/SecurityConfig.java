package com.omaru.keepsake_api.config;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.authorization.AuthorizationDecision;

import com.omaru.keepsake_api.security.JwtAuthenticationFilter;
import com.omaru.keepsake_api.security.SuccessHandler;
import com.omaru.keepsake_api.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final SuccessHandler successHandler;
    private final JwtService jwtService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/", "/login/**", "/oauth2/**").permitAll()
                        .requestMatchers("/api/**").access((authentication, context) ->
                                new AuthorizationDecision(
                                        authentication.get().isAuthenticated()
                                                && authentication.get().getPrincipal() instanceof Long))
                        .anyRequest().authenticated())
                .exceptionHandling(errors -> errors.defaultAuthenticationEntryPointFor(
                        (request, response, exception) -> response.sendError(401),
                        request -> request.getServletPath().startsWith("/api/")))
                .oauth2Login(oauth2 -> oauth2.successHandler(successHandler))
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtService),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
