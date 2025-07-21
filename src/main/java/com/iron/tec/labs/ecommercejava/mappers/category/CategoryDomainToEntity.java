package com.iron.tec.labs.ecommercejava.mappers.category;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.iron.tec.labs.ecommercejava.db.entities.Category;
import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;

@Component
public class CategoryDomainToEntity implements Converter<CategoryDomain, Category> {

    @Override
    public Category convert(@NonNull CategoryDomain source) {
        return Category.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }
}