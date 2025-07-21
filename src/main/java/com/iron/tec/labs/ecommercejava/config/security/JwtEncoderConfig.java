package com.iron.tec.labs.ecommercejava.config.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
@AllArgsConstructor
public class JwtEncoderConfig {

    private final JwtConfig jwtConfig;

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(
                new OctetSequenceKey.Builder(jwtConfig.secretKey().getBytes())
                        .keyID(jwtConfig.secretKey())
                        .algorithm(JWSAlgorithm.HS256)
                        .build())));
    }
}
