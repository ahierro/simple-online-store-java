package com.iron.tec.labs.ecommercejava.config.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import static com.iron.tec.labs.ecommercejava.constants.Constants.*;

public class HttpConfigSetter {
    public static HttpSecurity setHttpConfig(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/signup").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/confirm").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/webjars/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/product/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/category/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/purchase-order/**").hasAuthority(SCOPE_ROLE_USER)
                        .requestMatchers(HttpMethod.PATCH, "/api/purchase-order/**").hasAuthority(SCOPE_ROLE_ADMIN)
                        .requestMatchers(HttpMethod.POST, API).hasAuthority(SCOPE_ROLE_ADMIN)
                        .requestMatchers(HttpMethod.PUT, API).hasAuthority(SCOPE_ROLE_ADMIN)
                        .requestMatchers(HttpMethod.DELETE, API).hasAuthority(SCOPE_ROLE_ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/purchase-order/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    }
}
