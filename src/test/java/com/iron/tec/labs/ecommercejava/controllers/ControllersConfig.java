package com.iron.tec.labs.ecommercejava.controllers;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@SpringBootApplication
public class ControllersConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange()
                .pathMatchers(HttpMethod.POST, "/login").permitAll()
                .pathMatchers(HttpMethod.POST, "/signup").permitAll()
                .pathMatchers(HttpMethod.POST, "/v1/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT, "/v1/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/v1/**").hasRole("ADMIN")
                .anyExchange()
                .authenticated()
                .and()
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .formLogin()
                .disable()
                .csrf()
                .disable();
        return http.build();
    }
}
