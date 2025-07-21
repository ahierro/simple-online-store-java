package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;

import java.util.UUID;

public interface UserDAO {
    AppUserDomain getById(UUID id);
    AppUserDomain getByUsername(String username);
    AppUserDomain create(AppUserDomain user);
    AppUserDomain update(AppUserDomain user);
    AppUserDomain findByIdAndActive(UUID id, Boolean active);
}