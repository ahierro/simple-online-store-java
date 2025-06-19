package com.iron.tec.labs.ecommercejava.mappers.user;

import com.iron.tec.labs.ecommercejava.db.entities.AppUser;
import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class AppUserEntityToDomain implements Converter<AppUser, AppUserDomain> {
    @Override
    public AppUserDomain convert(@NonNull AppUser source) {
        return AppUserDomain.builder()
                .id(source.getId())
                .username(source.getUsername())
                .email(source.getEmail())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .build();
    }
}
