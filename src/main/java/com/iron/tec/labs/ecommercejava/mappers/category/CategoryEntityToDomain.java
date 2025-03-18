
package com.iron.tec.labs.ecommercejava.mappers.category;

import com.iron.tec.labs.ecommercejava.db.entities.Category;
import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CategoryEntityToDomain implements Converter<Category, CategoryDomain> {

    @Override
    public CategoryDomain convert(@NonNull Category source) {
        return CategoryDomain.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .deleted(source.getDeleted())
                .createdAt(source.getCreatedAt())
                .build();
    }
}