package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.UserDAO;
import com.iron.tec.labs.ecommercejava.dto.RegisterUserDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final UserBusinessService userBusinessService;
    private final UserNotificationService userNotificationService;

    @Override
    public Mono<Void> create(@NotNull RegisterUserDTO registerUserDTO) {
        log.info("Creating user: {}", registerUserDTO.getUsername());
        
        return userBusinessService.prepareUserForCreation(registerUserDTO)
                .flatMap(userDAO::create)
                .flatMap(createdUser -> {
                    // Send welcome email asynchronously without blocking the main flow
                    userNotificationService.sendWelcomeEmail(createdUser)
                            .doOnError(error -> log.error("Failed to send welcome email for user: {}", 
                                    createdUser.getUsername(), error))
                            .subscribe(); // Fire and forget
                    
                    return Mono.empty();
                })
                .then();
    }

    @Override
    public Mono<String> confirm(String token) {
        log.info("Confirming user with token: {}", token);
        
        return userBusinessService.activateUser(UUID.fromString(token))
                .flatMap(activatedUser -> {
                    // Send confirmation email asynchronously
                    userNotificationService.sendUserConfirmationEmail(activatedUser)
                            .doOnError(error -> log.error("Failed to send confirmation email for user: {}", 
                                    activatedUser.getUsername(), error))
                            .subscribe(); // Fire and forget
                    
                    return Mono.just("User " + activatedUser.getUsername() + " confirmed!");
                });
    }
}
