package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.config.security.JwtConfig;
import com.iron.tec.labs.ecommercejava.db.entities.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JWTGeneratorServiceImpl implements JWTGeneratorService {

    private final JwtEncoder encoder;
    private final JwtConfig jwtConfig;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(jwtConfig.expiration(), ChronoUnit.MILLIS))
                .id(UUID.randomUUID().toString())
                .subject(ObjectUtils.nullSafeToString(((AppUser)authentication.getPrincipal()).getId()))
                .claim("name", authentication.getName())
                .claim("email", ((AppUser)authentication.getPrincipal()).getEmail())
                .claim("firstName", ((AppUser)authentication.getPrincipal()).getFirstName())
                .claim("lastName", ((AppUser)authentication.getPrincipal()).getLastName())
                .claim("scope", scope)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),claims)).getTokenValue();
    }

}
