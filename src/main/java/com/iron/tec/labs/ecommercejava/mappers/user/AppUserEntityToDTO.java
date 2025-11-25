package com.iron.tec.labs.ecommercejava.mappers.user;

import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import com.iron.tec.labs.ecommercejava.dto.UserDTO;
import org.springframework.core.convert.converter.Converter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class AppUserEntityToDTO implements Converter<@NonNull AppUserDomain,@NonNull UserDTO> {
    @Override
    public UserDTO convert(@NonNull AppUserDomain source) {
        return UserDTO.builder()
                .username(source.getUsername())
                .email(source.getEmail())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .build();
    }
}
