package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.db.repository.UserRepository;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import java.util.function.Function;

import static com.iron.tec.labs.ecommercejava.constants.Constants.*;
import static com.iron.tec.labs.ecommercejava.constants.Constants.SCOPE_ROLE_ADMIN;

@SpringBootApplication
public class ControllersConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
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
                        .pathMatchers(HttpMethod.POST, API).hasAuthority(SCOPE_ROLE_ADMIN)
                        .pathMatchers(HttpMethod.PUT, API).hasAuthority(SCOPE_ROLE_ADMIN)
                        .pathMatchers(HttpMethod.DELETE, API).hasAuthority(SCOPE_ROLE_ADMIN)
                        .pathMatchers(HttpMethod.GET, "/api/purchase-order/**").hasAuthority(SCOPE_ROLE_ADMIN)
                        .anyExchange().authenticated())
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .build();
    }
}
