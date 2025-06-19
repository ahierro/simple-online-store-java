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
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.iron.tec.labs.ecommercejava.constants.Constants.*;

@Repository
@AllArgsConstructor
@Transactional
@Log4j2
public class CategoryDAOImpl implements CategoryDAO {
    private final ConversionService conversionService;
    private final CategoryRepository categoryRepository;
    private final MessageService messageService;

    @Override
    public Mono<CategoryDomain> getById(UUID id) {
        return this.categoryRepository.findById(id)
                .mapNotNull(category -> conversionService.convert(category, CategoryDomain.class))
                .switchIfEmpty(Mono.error(new NotFound(messageService.getRequestLocalizedMessage(ERROR_CATEGORY, NOT_FOUND, id.toString()))));
    }

    @Override
    public Mono<PageDomain<CategoryDomain>> getPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Example<Category> example = Example.of(Category.builder().build());
        return this.categoryRepository.findBy(example,
                        reactiveFluentQuery ->
                                reactiveFluentQuery.sortBy(Sort.by("createdAt")
                                .descending()).page(pageRequest))
                .mapNotNull(categoryPage ->
                        new PageDomain<>(categoryPage.getContent().stream().map(
                                x -> conversionService.convert(x, CategoryDomain.class)).toList(),
                                categoryPage.getTotalPages(), (int) categoryPage.getTotalElements(),
                                categoryPage.getNumber())
                );
    }

    @Override
    public Mono<CategoryDomain> create(CategoryDomain category) {
        Category entity = conversionService.convert(category, Category.class);
        assert entity != null;
        return categoryRepository.save(entity)
                .mapNotNull(savedCategory -> conversionService.convert(savedCategory, CategoryDomain.class))
                .doOnError(DataIntegrityViolationException.class, e -> {
                    throw new Conflict(messageService.getRequestLocalizedMessage(ERROR_CATEGORY,
                            ALREADY_EXISTS, ObjectUtils.nullSafeToString(category.getId())));
                });
    }

    @Override
    public Mono<CategoryDomain> update(CategoryDomain category) {
        Category entity = conversionService.convert(category, Category.class);
        assert entity != null;
        entity.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(entity)
                .mapNotNull(savedCategory -> conversionService.convert(savedCategory, CategoryDomain.class))
                .doOnError(TransientDataAccessResourceException.class, e -> {
                    throw new NotFound(messageService.getRequestLocalizedMessage(ERROR_CATEGORY, NOT_FOUND,
                            ObjectUtils.nullSafeToString(category.getId())));
                });
    }

    @Override
    public Mono<Void> delete(String id) {

        return this.categoryRepository.deleteCategoryById(UUID.fromString(id))
                .doOnError(DataIntegrityViolationException.class, e -> {
                    throw new Conflict(messageService.getRequestLocalizedMessage(ERROR_CATEGORY, CONFLICT));
                })
                .flatMap(numberOfDeletedRows -> {
                    if (numberOfDeletedRows == 0)
                        return Mono.error(new NotFound(messageService.getRequestLocalizedMessage(ERROR_CATEGORY, NOT_FOUND, id)));
                    return Mono.empty();
                });
    }
}
