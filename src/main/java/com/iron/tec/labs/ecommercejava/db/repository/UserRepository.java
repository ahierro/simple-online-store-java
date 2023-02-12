package com.iron.tec.labs.ecommercejava.db.repository;

import com.iron.tec.labs.ecommercejava.db.entities.AppUser;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserRepository extends R2dbcRepository<AppUser, UUID> {
    Mono<AppUser> findByIdAndActive(UUID id, Boolean active);

    Mono<AppUser> findByUsername(String username);
}
