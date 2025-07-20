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
                .password(source.getPassword())
                .email(source.getEmail())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .active(source.isActive())
                .locked(source.isLocked())
                .authorities(source.getAuthorities().stream().map(String::valueOf).toList())
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }
}
