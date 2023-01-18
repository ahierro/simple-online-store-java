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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
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
    public Flux<Category> getAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Mono<PageImpl<Category>> getPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return this.categoryRepository.findBy(pageRequest).collectList()
                .zipWith(this.categoryRepository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    @Override
    public Mono<Category> create(Category product) {
        return categoryRepository.save(product).doOnError(DataIntegrityViolationException.class, e -> {
            throw new Conflict(messageService.getRequestLocalizedMessage(ERROR_CATEGORY,
                    ALREADY_EXISTS, product.getId().toString()));
        });
    }

    @Override
    public Mono<Category> update(Category product) {
        return categoryRepository.save(product).doOnError(TransientDataAccessResourceException.class, e -> {
            throw new NotFound(messageService.getRequestLocalizedMessage(ERROR_CATEGORY, NOT_FOUND,
                    product.getId().toString()));
        });
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.categoryRepository.deleteProductById(UUID.fromString(id))
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
