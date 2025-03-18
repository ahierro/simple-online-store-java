package com.iron.tec.labs.ecommercejava.mappers.user;

import com.iron.tec.labs.ecommercejava.db.entities.AppUser;
import com.iron.tec.labs.ecommercejava.dto.RegisterUserDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class RegisterUserMapper implements Converter<RegisterUserDTO, AppUser> {

    @Override
    public AppUser convert(@NonNull RegisterUserDTO source) {
        return AppUser.builder()
                .username(source.getUsername())
                .password(source.getPassword())
                .email(source.getEmail())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .build();
    }
}