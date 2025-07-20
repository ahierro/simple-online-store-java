package com.iron.tec.labs.ecommercejava.mappers.user;

import com.iron.tec.labs.ecommercejava.db.entities.AppUser;
import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class AppUserDomainToEntity implements Converter<AppUserDomain, AppUser> {
    @Override
    public AppUser convert(@NonNull AppUserDomain source) {
        return AppUser.builder()
                .id(source.getId())
                .username(source.getUsername())
                .password(source.getPassword())
                .email(source.getEmail())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .active(source.isActive())
                .locked(source.isLocked())
                .authorities(source.getAuthorities())
                .build();
    }
}