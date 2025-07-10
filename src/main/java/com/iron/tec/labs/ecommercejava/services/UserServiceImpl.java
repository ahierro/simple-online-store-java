package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.entities.AppUser;
import com.iron.tec.labs.ecommercejava.db.repository.UserRepository;
import com.iron.tec.labs.ecommercejava.dto.RegisterUserDTO;
import com.iron.tec.labs.ecommercejava.exceptions.Conflict;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.iron.tec.labs.ecommercejava.constants.Constants.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final MessageService messageService;

    @Override
    public Mono<Void> create(@NotNull RegisterUserDTO user) {
        return userRepository.save(AppUser.builder()
                        .id(UUID.randomUUID())
                        .username(user.getUsername())
                        .password(passwordEncoder.encode(user.getPassword()))
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .active(false)
                        .authority(user.isAdmin()?"ROLE_ADMIN":"ROLE_USER")
                        .build())
                .doOnError(DuplicateKeyException.class, e -> {
                    throw new Conflict(messageService.getRequestLocalizedMessage(ERROR_USER,
                            ALREADY_EXISTS));
                })
                .flatMap(userCreated -> {
                    emailService.sendEmailInParallel(userCreated.getEmail(),
                            "Welcome to EcommerceJava! - Please confirm your e-mail",
                            "Welcome to EcommerceJava "+userCreated.getFirstName()+"! Please confirm your e-mail by clicking on the following link:<a href=\"http://localhost:8080/api/confirm?token="+userCreated.getId()+"\">Confirm Mail!</a>" );
                    return Mono.empty();
                }).then();
    }

    @Override
    public Mono<String> confirm(String token) {
        return userRepository.findByIdAndActive(UUID.fromString(token), false)
                .switchIfEmpty(Mono.error(new NotFound(messageService.getRequestLocalizedMessage(ERROR_USER,
                        NOT_FOUND))))
                .flatMap(user -> {
                    user.setActive(true);
                    user.setUpdatedAt(LocalDateTime.now());
                    return userRepository.save(user);
                }).map(user -> "User "+user.getUsername()+" confirmed!");
    }
}
