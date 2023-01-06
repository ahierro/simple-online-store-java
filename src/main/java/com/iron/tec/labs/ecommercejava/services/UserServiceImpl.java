package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.entities.AppUser;
import com.iron.tec.labs.ecommercejava.db.repository.UserRepository;
import com.iron.tec.labs.ecommercejava.dto.RegisterUserDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ConversionService conversionService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<Void> create(@NotNull RegisterUserDTO user) {
        AppUser userDb = conversionService.convert(user, AppUser.class);
        if(userDb==null){
            throw new IllegalArgumentException();
        }
        userDb.setPassword(passwordEncoder.encode(userDb.getPassword()));
        userDb.getRoles().add("USER");
        return userRepository.save(userDb)
                .then();
    }
}
