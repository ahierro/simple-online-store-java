package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.db.entities.ProductView;
import com.iron.tec.labs.ecommercejava.db.repository.CustomProductRepository;
import com.iron.tec.labs.ecommercejava.db.repository.ProductRepository;
import com.iron.tec.labs.ecommercejava.db.repository.ProductViewRepository;
import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import com.iron.tec.labs.ecommercejava.exceptions.Conflict;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import com.iron.tec.labs.ecommercejava.services.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.data.domain.*;
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
public class ProductDAOImpl implements ProductDAO {
    private final ProductRepository productRepository;
    private final MessageService messageService;
    private final CustomProductRepository customProductRepository;

    private final ProductViewRepository productViewRepository;

    @Override
    public Mono<Page<ProductView>> getProductViewPage(int page, int size, ProductView productViewExample,Sort.Direction sortByPrice) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withMatcher("productDescription", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<ProductView> example = Example.of(productViewExample, matcher);
        PageRequest pageRequest = PageRequest.of(page, size);
        return this.productViewRepository.findBy(example,
                queryFunction ->
                        queryFunction.sortBy((sortByPrice==null)?Sort.unsorted()
                                        :Sort.by(new Sort.Order(sortByPrice, PRICE)))
                        .page(pageRequest));
    }

    @Override
    public Mono<ProductDTO> findById(UUID id) {
        return this.customProductRepository.findById(id).switchIfEmpty(Mono.defer(() -> {
            throw new NotFound(messageService.getRequestLocalizedMessage(ERROR_PRODUCT, NOT_FOUND, id.toString()));
        }));
    }

    @Override
    public Mono<Product> create(Product product) {
        return productRepository.save(product).doOnError(DataIntegrityViolationException.class, e -> {
            throw new Conflict(messageService.getRequestLocalizedMessage(ERROR_PRODUCT,
                    ALREADY_EXISTS, ObjectUtils.nullSafeToString(product.getId())));
        });
    }

    @Override
    public Mono<Product> update(Product product) {
        return productRepository.save(product).doOnError(TransientDataAccessResourceException.class, e -> {
            throw new NotFound(messageService.getRequestLocalizedMessage(ERROR_PRODUCT, NOT_FOUND,
                    ObjectUtils.nullSafeToString(product.getId())));
        });
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.productRepository.deleteProductById(UUID.fromString(id)).flatMap(numberOfDeletedRows -> {
            if (numberOfDeletedRows == 0)
                return Mono.error(new NotFound(messageService.getRequestLocalizedMessage(ERROR_PRODUCT, NOT_FOUND, id)));
            return Mono.empty();
        });
    }
}
