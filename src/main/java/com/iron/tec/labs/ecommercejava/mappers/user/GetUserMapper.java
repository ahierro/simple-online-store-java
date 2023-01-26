package com.iron.tec.labs.ecommercejava.mappers.user;

import com.iron.tec.labs.ecommercejava.db.entities.AppUser;
import com.iron.tec.labs.ecommercejava.dto.UserDTO;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface GetUserMapper extends Converter<UserDTO, AppUser>  {
    UserDTO convert(AppUser appUser);
}