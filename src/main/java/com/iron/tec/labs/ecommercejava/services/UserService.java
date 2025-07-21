package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.dto.RegisterUserDTO;

public interface UserService {
    void create(RegisterUserDTO user);

    String confirm(String token);
}
