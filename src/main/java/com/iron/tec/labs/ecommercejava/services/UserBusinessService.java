package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import com.iron.tec.labs.ecommercejava.dto.RegisterUserDTO;

import java.util.UUID;

public interface UserBusinessService {
    AppUserDomain prepareUserForCreation(RegisterUserDTO registerUserDTO);
    AppUserDomain activateUser(UUID userId);
}