package com.iron.tec.labs.ecommercejava.mappers.purchase.order.line;

import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLine;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import org.springframework.core.convert.converter.Converter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PurchaseOrderLineDomainToEntity implements Converter<@NonNull PurchaseOrderLineDomain,@NonNull PurchaseOrderLine> {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public PurchaseOrderLine convert(@NonNull PurchaseOrderLineDomain source) {
        return PurchaseOrderLine.builder()
                .id(source.getId())
                .product(source.getProduct() != null ?
                        entityManager.getReference(Product.class, source.getProduct().getId()):null)
                .quantity(source.getQuantity())
                .build();
    }
}
