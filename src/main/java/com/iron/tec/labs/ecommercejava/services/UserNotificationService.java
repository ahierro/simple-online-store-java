package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;

public interface UserNotificationService {
    void sendWelcomeEmail(AppUserDomain user);
    void sendUserConfirmationEmail(AppUserDomain user);
}