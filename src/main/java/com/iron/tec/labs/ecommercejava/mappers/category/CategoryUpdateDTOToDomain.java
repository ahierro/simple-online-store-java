package com.iron.tec.labs.ecommercejava.mappers.category;

import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.dto.CategoryUpdateDTO;
import org.springframework.core.convert.converter.Converter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CategoryUpdateDTOToDomain implements Converter<@NonNull CategoryUpdateDTO,@NonNull CategoryDomain> {

    @Override
    public CategoryDomain convert(@NonNull CategoryUpdateDTO source) {
        CategoryDomain categoryDomain = new CategoryDomain();

        // ID and createdAt are intentionally not set as they should be preserved from the existing entity
        categoryDomain.setName(source.getName());
        categoryDomain.setDescription(source.getDescription());

        return categoryDomain;
    }
}
