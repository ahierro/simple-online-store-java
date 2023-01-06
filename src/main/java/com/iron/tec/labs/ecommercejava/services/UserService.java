package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.dto.RegisterUserDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<Void> create(RegisterUserDTO user);
}
