package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.entities.AppUser;
import com.iron.tec.labs.ecommercejava.db.repository.UserRepository;
import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import com.iron.tec.labs.ecommercejava.exceptions.Conflict;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import com.iron.tec.labs.ecommercejava.services.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.iron.tec.labs.ecommercejava.constants.Constants.*;

@Repository
@AllArgsConstructor
@Transactional
@Log4j2
public class UserDAOImpl implements UserDAO {
    private final ConversionService conversionService;
    private final UserRepository userRepository;
    private final MessageService messageService;

    @Override
    public Mono<AppUserDomain> getById(UUID id) {
        return userRepository.findById(id)
                .mapNotNull(user -> conversionService.convert(user, AppUserDomain.class))
                .switchIfEmpty(Mono.error(
                        new NotFound(messageService.getRequestLocalizedMessage(ERROR_USER, NOT_FOUND, id.toString()))));
    }

    @Override
    public Mono<AppUserDomain> getByUsername(String username) {
        return userRepository.findByUsername(username)
                .mapNotNull(user -> conversionService.convert(user, AppUserDomain.class))
                .switchIfEmpty(Mono.error(
                        new NotFound(messageService.getRequestLocalizedMessage(ERROR_USER, NOT_FOUND, username))));
    }

    @Override
    public Mono<AppUserDomain> create(AppUserDomain userDomain) {
        AppUser entity = conversionService.convert(userDomain, AppUser.class);
        if (entity == null)
            throw new RuntimeException("Entity cannot be null");

        return userRepository.save(entity)
                .mapNotNull(savedUser -> conversionService.convert(savedUser, AppUserDomain.class))
                .doOnError(DuplicateKeyException.class, e -> {
                    throw new Conflict(messageService.getRequestLocalizedMessage(ERROR_USER, ALREADY_EXISTS));
                });
    }

    @Override
    public Mono<AppUserDomain> update(AppUserDomain userDomain) {
        AppUser entity = conversionService.convert(userDomain, AppUser.class);
        if (entity == null)
            throw new RuntimeException("Entity cannot be null");

        entity.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(entity)
                .mapNotNull(savedUser -> conversionService.convert(savedUser, AppUserDomain.class));
    }

    @Override
    public Mono<AppUserDomain> findByIdAndActive(UUID id, Boolean active) {
        return userRepository.findByIdAndActive(id, active)
                .mapNotNull(user -> conversionService.convert(user, AppUserDomain.class))
                .switchIfEmpty(
                        Mono.error(new NotFound(messageService.getRequestLocalizedMessage(ERROR_USER, NOT_FOUND))));
    }
}