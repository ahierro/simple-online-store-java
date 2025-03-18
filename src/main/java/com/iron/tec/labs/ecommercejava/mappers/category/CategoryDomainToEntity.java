package com.iron.tec.labs.ecommercejava.mappers.category;

import com.iron.tec.labs.ecommercejava.db.entities.Category;
import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CategoryDomainToEntity implements Converter<CategoryDomain, Category> {

    @Override
    public Category convert(@NonNull CategoryDomain source) {
        return Category.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .deleted(source.getDeleted())
                .createdAt(source.getCreatedAt())
                .build();
    }
}