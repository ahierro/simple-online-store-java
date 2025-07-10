package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.db.entities.ProductView;
import com.iron.tec.labs.ecommercejava.db.repository.CustomProductRepository;
import com.iron.tec.labs.ecommercejava.db.repository.ProductRepository;
import com.iron.tec.labs.ecommercejava.db.repository.ProductViewRepository;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.exceptions.Conflict;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import com.iron.tec.labs.ecommercejava.services.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@AllArgsConstructor
@Transactional
@Log4j2
public class ProductDAOImpl implements ProductDAO {
    private final ProductRepository productRepository;
    private final MessageService messageService;
    private final CustomProductRepository customProductRepository;
    private final ProductViewRepository productViewRepository;
    private final ConversionService conversionService;

    @Override
    public Mono<PageDomain<ProductDomain>> getProductViewPage(int page, int size, ProductDomain productDomainExample, Sort.Direction sortByPrice) {
        ProductView productViewExample = conversionService.convert(productDomainExample, ProductView.class);
        if (productViewExample == null) throw new RuntimeException("Entity cannot be null");
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withMatcher("productDescription", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<ProductView> example = Example.of(productViewExample, matcher);
        PageRequest pageRequest = PageRequest.of(page, size);
        return this.productViewRepository.findBy(example,
                queryFunction ->
                        queryFunction.sortBy((sortByPrice==null)?Sort.by("productCreatedAt").descending()
                                        :Sort.by(new Sort.Order(sortByPrice, "price")))
                        .page(pageRequest))
            .map(pageResult -> new PageDomain<>(
                pageResult.getContent().stream()
                    .map(productView -> conversionService.convert(productView, ProductDomain.class))
                    .toList(),
                pageResult.getTotalPages(),
                (int) pageResult.getTotalElements(),
                pageResult.getNumber()
            ));
    }

    @Override
    public Mono<ProductDomain> findById(UUID id) {
        return this.customProductRepository.findById(id)
            .mapNotNull(product -> conversionService.convert(product, ProductDomain.class))
            .switchIfEmpty(Mono.error(new NotFound(messageService.getRequestLocalizedMessage("error.product", "not_found", id.toString()))));
    }

    @Override
    public Mono<ProductDomain> create(ProductDomain productDomain) {
        Product entity = conversionService.convert(productDomain, Product.class);
        if (entity == null) throw new RuntimeException("Entity cannot be null");
        return productRepository.save(entity)
            .mapNotNull(savedProduct -> conversionService.convert(savedProduct, ProductDomain.class))
            .doOnError(DataIntegrityViolationException.class, e -> {
                log.error("Data integrity violation while creating product", e);
                throw new Conflict(messageService.getRequestLocalizedMessage("error.product", "already_exists", String.valueOf(productDomain.getId())));
            });
    }

    @Override
    public Mono<ProductDomain> update(ProductDomain productDomain) {
        Product entity = conversionService.convert(productDomain, Product.class);
        if (entity == null) throw new RuntimeException("Entity cannot be null");
        entity.setUpdatedAt(java.time.LocalDateTime.now());
        return productRepository.save(entity)
            .mapNotNull(savedProduct -> conversionService.convert(savedProduct, ProductDomain.class))
            .doOnError(TransientDataAccessResourceException.class, e -> {
                throw new NotFound(messageService.getRequestLocalizedMessage("error.product", "not_found", String.valueOf(productDomain.getId())));
            });
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.productRepository.deleteProductById(UUID.fromString(id)).flatMap(numberOfDeletedRows -> {
            if (numberOfDeletedRows == 0)
                return Mono.error(new NotFound(messageService.getRequestLocalizedMessage("error.product", "not_found", id)));
            return Mono.empty();
        });
    }
}
