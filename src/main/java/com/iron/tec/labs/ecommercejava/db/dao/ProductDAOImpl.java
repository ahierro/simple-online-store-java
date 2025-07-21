package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.db.entities.ProductView;
import com.iron.tec.labs.ecommercejava.db.repository.CustomProductRepository;
import com.iron.tec.labs.ecommercejava.db.repository.ProductRepository;
import com.iron.tec.labs.ecommercejava.db.repository.ProductViewRepository;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import com.iron.tec.labs.ecommercejava.exceptions.Conflict;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import com.iron.tec.labs.ecommercejava.services.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.UUID;

import static com.iron.tec.labs.ecommercejava.constants.Constants.ERROR_CATEGORY;
import static com.iron.tec.labs.ecommercejava.constants.Constants.NOT_FOUND;

@Repository
@AllArgsConstructor
@Log4j2
public class ProductDAOImpl implements ProductDAO {
    private final ProductRepository productRepository;
    private final MessageService messageService;
    private final CustomProductRepository customProductRepository;
    private final ProductViewRepository productViewRepository;
    private final ConversionService conversionService;

    @Override
    public PageDomain<ProductDomain> getProductViewPage(int page, int size, ProductDomain productDomainExample, Sort.Direction sortByPrice) {
        ProductView productViewExample = conversionService.convert(productDomainExample, ProductView.class);
        if (productViewExample == null) productViewExample = ProductView.builder().build();
        
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withMatcher("productDescription", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<ProductView> example = Example.of(productViewExample, matcher);
        
        Sort sort = (sortByPrice == null) ? 
                Sort.by("productCreatedAt").descending() : 
                Sort.by(new Sort.Order(sortByPrice, "price"));
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        
        Page<ProductView> pageResult = this.productViewRepository.findAll(example, pageRequest);
        
        return new PageDomain<>(
                pageResult.getContent().stream()
                        .map(productView -> conversionService.convert(productView, ProductDomain.class))
                        .toList(),
                pageResult.getTotalPages(),
                (int) pageResult.getTotalElements(),
                pageResult.getNumber()
        );
    }

    @Override
    public ProductDomain findById(UUID id) {
        Optional<ProductDTO> productOpt = this.customProductRepository.findById(id);
        if (productOpt.isEmpty()) {
            throw new NotFound(messageService.getRequestLocalizedMessage("error.product", "not_found", id.toString()));
        }
        return conversionService.convert(productOpt.get(), ProductDomain.class);
    }

    @Override
    public ProductDomain create(ProductDomain productDomain) {
        Product entity = conversionService.convert(productDomain, Product.class);
        if (entity == null) throw new RuntimeException("Entity cannot be null");
        
        try {
            Product savedProduct = productRepository.save(entity);
            return conversionService.convert(savedProduct, ProductDomain.class);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while creating product", e);
            if (e.getMessage().contains("p_category_fk")) {
                throw new NotFound(messageService.getRequestLocalizedMessage(ERROR_CATEGORY,
                        NOT_FOUND, ObjectUtils.nullSafeToString(productDomain.getCategory() != null ? productDomain.getCategory().getId() : null)));
            }
            throw new Conflict(messageService.getRequestLocalizedMessage("error.product", "already_exists", String.valueOf(productDomain.getId())));
        }
    }

    @Override
    @Transactional
    public ProductDomain update(ProductDomain productDomain) {
        Product entity = conversionService.convert(productDomain, Product.class);
        if (entity == null) throw new RuntimeException("Entity cannot be null");
        Product savedProduct = productRepository.findById(productDomain.getId())
                .map(existing -> {

                    //maps fields from the incoming productDomain to the existing entity
                    existing.setUpdatedAt(java.time.LocalDateTime.now());
                    existing.setBigImageUrl(productDomain.getBigImageUrl());
                    existing.setSmallImageUrl(productDomain.getSmallImageUrl());
                    existing.setName(productDomain.getName());
                    existing.setDescription(productDomain.getDescription());
                    existing.setStock(productDomain.getStock());
                    existing.setPrice(productDomain.getPrice());
                    existing.setCategory(entity.getCategory());

                    return productRepository.save(existing);
                })
                .orElseThrow(() -> new NotFound(messageService.getRequestLocalizedMessage("error.product",
                        "not_found", String.valueOf(productDomain.getId()))));
        return conversionService.convert(savedProduct, ProductDomain.class);
    }

    @Override
    @Transactional
    public void delete(String id) {
        int numberOfDeletedRows = this.productRepository.deleteProductById(UUID.fromString(id));
        if (numberOfDeletedRows == 0) {
            throw new NotFound(messageService.getRequestLocalizedMessage("error.product", "not_found", id));
        }
    }
}
