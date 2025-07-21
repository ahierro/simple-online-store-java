package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.UserDAO;
import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import com.iron.tec.labs.ecommercejava.dto.RegisterUserDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final UserBusinessService userBusinessService;
    private final UserNotificationService userNotificationService;

    @Override
    public void create(@NotNull RegisterUserDTO registerUserDTO) {
        log.info("Creating user: {}", registerUserDTO.getUsername());
        
        AppUserDomain preparedUser = userBusinessService.prepareUserForCreation(registerUserDTO);
        AppUserDomain createdUser = userDAO.create(preparedUser);
        
        // Send welcome email asynchronously without blocking the main flow
        sendWelcomeEmailAsync(createdUser);
    }

    @Override
    public String confirm(String token) {
        log.info("Confirming user with token: {}", token);
        
        AppUserDomain activatedUser = userBusinessService.activateUser(UUID.fromString(token));
        
        // Send confirmation email asynchronously
        sendConfirmationEmailAsync(activatedUser);
        
        return "User " + activatedUser.getUsername() + " confirmed!";
    }
    
    @Async
    private void sendWelcomeEmailAsync(AppUserDomain createdUser) {
        try {
            userNotificationService.sendWelcomeEmail(createdUser);
        } catch (Exception error) {
            log.error("Failed to send welcome email for user: {}", createdUser.getUsername(), error);
        }
    }
    
    @Async
    private void sendConfirmationEmailAsync(AppUserDomain activatedUser) {
        try {
            userNotificationService.sendUserConfirmationEmail(activatedUser);
        } catch (Exception error) {
            log.error("Failed to send confirmation email for user: {}", activatedUser.getUsername(), error);
        }
    }
}
