package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserDAO {
    Mono<AppUserDomain> getById(UUID id);
    Mono<AppUserDomain> getByUsername(String username);
    Mono<AppUserDomain> create(AppUserDomain user);
    Mono<AppUserDomain> update(AppUserDomain user);
    Mono<AppUserDomain> findByIdAndActive(UUID id, Boolean active);
}