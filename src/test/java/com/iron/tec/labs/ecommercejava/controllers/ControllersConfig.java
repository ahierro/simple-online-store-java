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
                .pathMatchers(HttpMethod.GET, "/confirm").permitAll()
                .pathMatchers(HttpMethod.GET, "/v1/product/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/v1/category/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/v1/purchase-order/**").hasAuthority("SCOPE_ROLE_USER")
                .pathMatchers(HttpMethod.POST, "/v1/**").hasAuthority("SCOPE_ROLE_ADMIN")
                .pathMatchers(HttpMethod.PUT, "/v1/**").hasAuthority("SCOPE_ROLE_ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/v1/**").hasAuthority("SCOPE_ROLE_ADMIN")
                .pathMatchers(HttpMethod.GET, "/v1/purchase-order/**").hasAuthority("SCOPE_ROLE_ADMIN")
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
