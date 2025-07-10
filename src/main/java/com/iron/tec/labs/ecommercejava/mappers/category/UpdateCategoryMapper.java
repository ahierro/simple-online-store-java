package com.iron.tec.labs.ecommercejava.mappers.category;

import com.iron.tec.labs.ecommercejava.db.entities.Category;
import com.iron.tec.labs.ecommercejava.dto.CategoryUpdateDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UpdateCategoryMapper implements Converter<CategoryUpdateDTO, Category> {

    @Override
    public Category convert(@NonNull CategoryUpdateDTO source) {
        return Category.builder()
                .name(source.getName())
                .description(source.getDescription())
                .deleted(source.getDeleted() != null ? source.getDeleted() : false)
                .createdAt(LocalDateTime.now())
                .build();
    }
}