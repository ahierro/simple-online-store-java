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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.iron.tec.labs.ecommercejava.constants.Constants.*;

@Repository
@AllArgsConstructor
@Log4j2
public class UserDAOImpl implements UserDAO {
    private final ConversionService conversionService;
    private final UserRepository userRepository;
    private final MessageService messageService;

    @Override
    public AppUserDomain getById(UUID id) {
        Optional<AppUser> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new NotFound(messageService.getRequestLocalizedMessage(ERROR_USER, NOT_FOUND, id.toString()));
        }
        return conversionService.convert(userOpt.get(), AppUserDomain.class);
    }

    @Override
    public AppUserDomain getByUsername(String username) {
        Optional<AppUser> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new NotFound(messageService.getRequestLocalizedMessage(ERROR_USER, NOT_FOUND, username));
        }
        return conversionService.convert(userOpt.get(), AppUserDomain.class);
    }

    @Override
    public AppUserDomain create(AppUserDomain userDomain) {
        AppUser entity = conversionService.convert(userDomain, AppUser.class);
        if (entity == null)
            throw new RuntimeException("Entity cannot be null");

        try {
            AppUser savedUser = userRepository.save(entity);
            return conversionService.convert(savedUser, AppUserDomain.class);
        } catch (DataIntegrityViolationException e) {
            throw new Conflict(messageService.getRequestLocalizedMessage(ERROR_USER, ALREADY_EXISTS));
        }
    }

    @Override
    public AppUserDomain update(AppUserDomain userDomain) {
        AppUser entity = conversionService.convert(userDomain, AppUser.class);
        if (entity == null)
            throw new RuntimeException("Entity cannot be null");

        entity.setUpdatedAt(LocalDateTime.now());
        AppUser savedUser = userRepository.save(entity);
        return conversionService.convert(savedUser, AppUserDomain.class);
    }

    @Override
    public AppUserDomain findByIdAndActive(UUID id, Boolean active) {
        Optional<AppUser> userOpt = userRepository.findByIdAndActive(id, active);
        if (userOpt.isEmpty()) {
            throw new NotFound(messageService.getRequestLocalizedMessage(ERROR_USER, NOT_FOUND));
        }
        return conversionService.convert(userOpt.get(), AppUserDomain.class);
    }
}