package com.iron.tec.labs.ecommercejava.config.security;

import com.iron.tec.labs.ecommercejava.db.repository.UserRepository;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
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

@EnableWebFluxSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final RsaKeyProperties jwtConfigProperties;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange()
                .pathMatchers(HttpMethod.POST, "/login").permitAll()
                .pathMatchers(HttpMethod.POST, "/signup").permitAll()
                .pathMatchers(HttpMethod.GET, "/confirm").permitAll()
                .pathMatchers(HttpMethod.GET, "/webjars/swagger-ui/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
                .pathMatchers(HttpMethod.GET, "/v1/product/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/v1/category/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/v1/purchase-order/**").hasAuthority(SCOPE_ROLE_USER)
                .pathMatchers(HttpMethod.POST, V_1).hasAuthority(SCOPE_ROLE_ADMIN)
                .pathMatchers(HttpMethod.PUT, V_1).hasAuthority(SCOPE_ROLE_ADMIN)
                .pathMatchers(HttpMethod.DELETE, V_1).hasAuthority(SCOPE_ROLE_ADMIN)
                .pathMatchers(HttpMethod.GET, "/v1/purchase-order/**").hasAuthority(SCOPE_ROLE_ADMIN)
                .anyExchange()
                .authenticated()
                .and()
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .formLogin()
                .disable()
                .csrf()
                .disable()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(
            ReactiveUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager
                = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService(UserRepository users) {
        return username -> users
                .findByUsername(username)
                .map(Function.identity());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withPublicKey(jwtConfigProperties.publicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(jwtConfigProperties.publicKey())
                .privateKey(jwtConfigProperties.privateKey()).build();
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
    }
}
