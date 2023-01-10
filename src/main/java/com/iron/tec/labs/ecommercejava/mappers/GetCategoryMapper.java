package com.iron.tec.labs.ecommercejava.mappers;

import com.iron.tec.labs.ecommercejava.db.entities.Category;
import com.iron.tec.labs.ecommercejava.dto.CategoryDTO;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface GetCategoryMapper extends Converter<Category, CategoryDTO> {

    CategoryDTO convert(@NonNull Category product);
}