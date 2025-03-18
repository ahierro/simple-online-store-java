package com.iron.tec.labs.ecommercejava.mappers.category;

import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.dto.CategoryCreationDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CategoryCreationDTOToDomain implements Converter<CategoryCreationDTO, CategoryDomain> {

    @Override
    public CategoryDomain convert(@NonNull CategoryCreationDTO source) {
        CategoryDomain categoryDomain = new CategoryDomain();

        if (source.getId() != null) {
            categoryDomain.setId(UUID.fromString(source.getId()));
        }

        categoryDomain.setName(source.getName());
        categoryDomain.setDescription(source.getDescription());
        categoryDomain.setDeleted(source.getDeleted() != null ? source.getDeleted() : false);

        return categoryDomain;
    }
}
