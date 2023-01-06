package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.dto.LoginRequest;
import com.iron.tec.labs.ecommercejava.dto.RegisterUserDTO;
import com.iron.tec.labs.ecommercejava.services.JWTGeneratorService;
import com.iron.tec.labs.ecommercejava.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@Log4j2
public class AuthController {

    private final UserService userService;
    private final JWTGeneratorService jwtGeneratorService;
    private final ReactiveAuthenticationManager authenticationManager;

    @PostMapping("/login")
    public Mono<String> login(@RequestBody LoginRequest userLogin) {
        return authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userLogin.username(), userLogin.password()))
                .map(jwtGeneratorService::generateToken);
    }

    @PostMapping("signup")
    public Mono<Void> signup(@RequestBody RegisterUserDTO user) {
        return userService.create(user);
    }
}
