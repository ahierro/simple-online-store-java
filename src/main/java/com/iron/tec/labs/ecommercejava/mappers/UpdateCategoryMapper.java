package com.iron.tec.labs.ecommercejava.mappers;

import com.iron.tec.labs.ecommercejava.db.entities.Category;
import com.iron.tec.labs.ecommercejava.dto.CategoryUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface UpdateCategoryMapper extends Converter<CategoryUpdateDTO, Category>  {
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Category convert(@NonNull CategoryUpdateDTO product);

}