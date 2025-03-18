package com.iron.tec.labs.ecommercejava.mappers.category;

import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.dto.CategoryDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CategoryDomainToDTO implements Converter<CategoryDomain, CategoryDTO> {

    @Override
    public CategoryDTO convert(@NonNull CategoryDomain source) {
        return CategoryDTO.builder()
                .id(source.getId() != null ? source.getId().toString() : null)
                .name(source.getName())
                .description(source.getDescription())
                .createdAt(source.getCreatedAt())
                .build();
    }
}