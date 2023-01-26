package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.entities.Category;
import com.iron.tec.labs.ecommercejava.db.repository.CategoryRepository;
import com.iron.tec.labs.ecommercejava.exceptions.Conflict;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import com.iron.tec.labs.ecommercejava.services.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.iron.tec.labs.ecommercejava.constants.Constants.*;

@Repository
@AllArgsConstructor
@Transactional
@Log4j2
public class CategoryDAOImpl implements CategoryDAO {

    private final CategoryRepository categoryRepository;
    private final MessageService messageService;

    @Override
    public Mono<Category> getById(UUID id) {
        return this.categoryRepository.findById(id).switchIfEmpty(Mono.defer(() -> {
            throw new NotFound(messageService.getRequestLocalizedMessage(ERROR_CATEGORY, NOT_FOUND, id.toString()));
        }));
    }

    @Override
    public Mono<Page<Category>> getPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Example<Category> example = Example.of(Category.builder().build());
        return this.categoryRepository.findBy(example,
                reactiveFluentQuery -> reactiveFluentQuery.sortBy(Sort.by("createdAt").descending()).page(pageRequest));
    }

    @Override
    public Mono<Category> create(Category category) {
        return categoryRepository.save(category).doOnError(DataIntegrityViolationException.class, e -> {
            throw new Conflict(messageService.getRequestLocalizedMessage(ERROR_CATEGORY,
                    ALREADY_EXISTS, ObjectUtils.nullSafeToString(category.getId())));
        });
    }

    @Override
    public Mono<Category> update(Category category) {
        return categoryRepository.save(category).doOnError(TransientDataAccessResourceException.class, e -> {
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
