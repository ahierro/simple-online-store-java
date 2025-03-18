package com.iron.tec.labs.ecommercejava.mappers.category;

import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.dto.CategoryDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CategoryDTOToDomain implements Converter<CategoryDTO, CategoryDomain> {

    @Override
    public CategoryDomain convert(@NonNull CategoryDTO source) {
        return CategoryDomain.builder()
                .id(source.getId() != null ? UUID.fromString(source.getId()) : null)
                .name(source.getName())
                .description(source.getDescription())
                .createdAt(source.getCreatedAt())
                .build();
    }
}