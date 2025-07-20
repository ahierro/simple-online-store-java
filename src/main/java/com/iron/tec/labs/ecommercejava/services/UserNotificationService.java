package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import reactor.core.publisher.Mono;

public interface UserNotificationService {
    Mono<Void> sendWelcomeEmail(AppUserDomain user);
    Mono<Void> sendUserConfirmationEmail(AppUserDomain user);
}