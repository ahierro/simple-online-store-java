package com.iron.tec.labs.ecommercejava.services;

import org.springframework.security.core.Authentication;

public interface JWTGeneratorService {
    String generateToken(Authentication authentication);
}
