package com.omaru.keepsake_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{
        http
                .authorizeHttpRequests(auth->auth.requestMatchers("/","/login/**", "/oauth2/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(Customizer.withDefaults());

        return http.build();

    }

}
