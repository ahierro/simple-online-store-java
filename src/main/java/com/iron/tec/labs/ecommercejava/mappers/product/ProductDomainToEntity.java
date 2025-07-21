package com.iron.tec.labs.ecommercejava.mappers.product;

import com.iron.tec.labs.ecommercejava.db.entities.Category;
import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ProductDomainToEntity implements Converter<ProductDomain, Product> {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Product convert(@NonNull ProductDomain source) {
        Product product = new Product();
        product.setId(source.getId());
        product.setName(source.getName());
        product.setDescription(source.getDescription());
        product.setStock(source.getStock());
        product.setPrice(source.getPrice());
        product.setSmallImageUrl(source.getSmallImageUrl());
        product.setBigImageUrl(source.getBigImageUrl());
        product.setCreatedAt(source.getCreatedAt());
        product.setUpdatedAt(source.getUpdatedAt());
        
        // Set category if available - using getReference for better performance
        if (source.getCategory() != null && source.getCategory().getId() != null) {
            Category category = entityManager.getReference(Category.class, source.getCategory().getId());
            product.setCategory(category);
        }
        
        return product;
    }
}
