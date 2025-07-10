package com.iron.tec.labs.ecommercejava.config.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import static com.iron.tec.labs.ecommercejava.constants.Constants.*;

public class HttpConfigSetter {
    public static ServerHttpSecurity setHttpConfig(ServerHttpSecurity http) {
        return http
                .cors(Customizer.withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/signup").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/confirm").permitAll()
                        .pathMatchers(HttpMethod.GET, "/webjars/swagger-ui/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/product/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/category/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/purchase-order/**").hasAuthority(SCOPE_ROLE_USER)
                        .pathMatchers(HttpMethod.PATCH, "/api/purchase-order/**").hasAuthority(SCOPE_ROLE_ADMIN)
                        .pathMatchers(HttpMethod.POST, API).hasAuthority(SCOPE_ROLE_ADMIN)
                        .pathMatchers(HttpMethod.PUT, API).hasAuthority(SCOPE_ROLE_ADMIN)
                        .pathMatchers(HttpMethod.DELETE, API).hasAuthority(SCOPE_ROLE_ADMIN)
                        .pathMatchers(HttpMethod.GET, "/api/purchase-order/**").permitAll()
                        .anyExchange().authenticated())
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec.jwt(Customizer.withDefaults()));
    }
}
