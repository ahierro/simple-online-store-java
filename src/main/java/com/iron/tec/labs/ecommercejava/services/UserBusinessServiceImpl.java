package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.UserDAO;
import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import com.iron.tec.labs.ecommercejava.dto.RegisterUserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class UserBusinessServiceImpl implements UserBusinessService {
    private final PasswordEncoder passwordEncoder;
    private final UserDAO userDAO;

    @Override
    public AppUserDomain prepareUserForCreation(RegisterUserDTO registerUserDTO) {
        log.info("Preparing user for creation: {}", registerUserDTO.getUsername());
        
        return AppUserDomain.builder()
                .id(UUID.randomUUID())
                .username(registerUserDTO.getUsername())
                .password(passwordEncoder.encode(registerUserDTO.getPassword()))
                .firstName(registerUserDTO.getFirstName())
                .lastName(registerUserDTO.getLastName())
                .email(registerUserDTO.getEmail())
                .active(false)
                .locked(false)
                .authorities(List.of(registerUserDTO.isAdmin() ? "ROLE_ADMIN" : "ROLE_USER"))
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public AppUserDomain activateUser(UUID userId) {
        log.info("Activating user with ID: {}", userId);
        
        AppUserDomain user = userDAO.findByIdAndActive(userId, false);
        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        return userDAO.update(user);
    }
}