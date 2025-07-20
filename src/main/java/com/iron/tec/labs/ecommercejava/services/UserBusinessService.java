package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import com.iron.tec.labs.ecommercejava.dto.RegisterUserDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserBusinessService {
    Mono<AppUserDomain> prepareUserForCreation(RegisterUserDTO registerUserDTO);
    Mono<AppUserDomain> activateUser(UUID userId);
}