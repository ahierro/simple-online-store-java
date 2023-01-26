package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.CategoryDAO;
import com.iron.tec.labs.ecommercejava.db.entities.Category;
import com.iron.tec.labs.ecommercejava.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDAO categoryDAO;
    private final ConversionService conversionService;

    @Override
    public Mono<CategoryDTO> getById(UUID id) {
        return categoryDAO.getById(id)
                .mapNotNull(product -> conversionService.convert(product, CategoryDTO.class));
    }

    public Mono<CategoryDTO> createCategory(CategoryCreationDTO productCreationDTO) {
        return categoryDAO.create(conversionService.convert(productCreationDTO, Category.class))
                .mapNotNull(product -> conversionService.convert(product, CategoryDTO.class));
    }

    @Override
    public Mono<CategoryDTO> updateCategory(String id, CategoryUpdateDTO productCreationDTO) {
        Category product = conversionService.convert(productCreationDTO, Category.class);
        if (product != null) {
            product.setId(UUID.fromString(id));
        }
        return categoryDAO.update(product)
                .mapNotNull(productUpdated -> conversionService.convert(productUpdated, CategoryDTO.class));
    }

    @Override
    public Mono<PageResponseDTO<CategoryDTO>> getCategoryPage(PageRequestDTO pageRequest) {
        return categoryDAO.getPage(pageRequest.getPage(), pageRequest.getSize())
                .mapNotNull(page ->
                        new PageResponseDTO<>(
                                page.getContent().stream()
                                        .map(x -> conversionService.convert(x, CategoryDTO.class)).toList()
                                , page.getPageable()
                                , page.getTotalPages()));
    }

    @Override
    public Mono<Void> deleteCategory(String id) {
        return categoryDAO.delete(id);
    }
}
