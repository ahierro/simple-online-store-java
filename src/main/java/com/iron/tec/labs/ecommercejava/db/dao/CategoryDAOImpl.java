package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.entities.Category;
import com.iron.tec.labs.ecommercejava.db.repository.CategoryRepository;
import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.exceptions.Conflict;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import com.iron.tec.labs.ecommercejava.services.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.iron.tec.labs.ecommercejava.constants.Constants.*;

@Repository
@AllArgsConstructor
@Log4j2
public class CategoryDAOImpl implements CategoryDAO {
    private final ConversionService conversionService;
    private final CategoryRepository categoryRepository;
    private final MessageService messageService;

    @Override
    public CategoryDomain getById(UUID id) {
        Optional<Category> categoryOpt = this.categoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            throw new NotFound(messageService.getRequestLocalizedMessage(ERROR_CATEGORY, NOT_FOUND, id.toString()));
        }
        return conversionService.convert(categoryOpt.get(), CategoryDomain.class);
    }

    @Override
    public PageDomain<CategoryDomain> getPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Example<Category> example = Example.of(Category.builder().build());
        Page<Category> categoryPage = this.categoryRepository.findAll(example, pageRequest);
        
        return new PageDomain<>(
                categoryPage.getContent().stream()
                        .map(x -> conversionService.convert(x, CategoryDomain.class))
                        .toList(),
                categoryPage.getTotalPages(), 
                (int) categoryPage.getTotalElements(),
                categoryPage.getNumber()
        );
    }

    @Override
    public CategoryDomain create(CategoryDomain category) {
        Category entity = conversionService.convert(category, Category.class);
        if (entity == null) throw new RuntimeException("Entity cannot be null");
        
        try {
            Category savedCategory = categoryRepository.save(entity);
            return conversionService.convert(savedCategory, CategoryDomain.class);
        } catch (DataIntegrityViolationException e) {
            throw new Conflict(messageService.getRequestLocalizedMessage(ERROR_CATEGORY,
                    ALREADY_EXISTS, ObjectUtils.nullSafeToString(category.getId())));
        }
    }

    @Override
    @Transactional
    public CategoryDomain update(CategoryDomain categoryDomain) {
        Category entity = conversionService.convert(categoryDomain, Category.class);
        if (entity == null) throw new RuntimeException("Entity cannot be null");
        Category savedCategory = categoryRepository.findById(categoryDomain.getId())
                .map(existing -> {
                    existing.setUpdatedAt(LocalDateTime.now());
                    existing.setName(categoryDomain.getName());
                    existing.setDescription(categoryDomain.getDescription());
                    return categoryRepository.save(existing);
                })
                .orElseThrow(() -> new NotFound(messageService.getRequestLocalizedMessage(ERROR_CATEGORY,
                        NOT_FOUND, ObjectUtils.nullSafeToString(categoryDomain.getId()))));
        return conversionService.convert(savedCategory, CategoryDomain.class);
    }

    @Override
    @Transactional
    public void delete(String id) {
        try {
            int numberOfDeletedRows = this.categoryRepository.deleteCategoryById(UUID.fromString(id));
            if (numberOfDeletedRows == 0) {
                throw new NotFound(messageService.getRequestLocalizedMessage(ERROR_CATEGORY, NOT_FOUND, id));
            }
        } catch (DataIntegrityViolationException e) {
            throw new Conflict(messageService.getRequestLocalizedMessage(ERROR_CATEGORY, CONFLICT));
        }
    }
}
