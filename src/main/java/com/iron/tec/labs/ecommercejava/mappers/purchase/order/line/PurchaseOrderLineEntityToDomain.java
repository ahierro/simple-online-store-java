package com.iron.tec.labs.ecommercejava.mappers.purchase.order.line;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLine;
import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PurchaseOrderLineEntityToDomain implements Converter<PurchaseOrderLine, PurchaseOrderLineDomain> {

    @Override
    public PurchaseOrderLineDomain convert(@NonNull PurchaseOrderLine source) {
        return PurchaseOrderLineDomain.builder()
                .id(source.getId())
                .purchaseOrder(PurchaseOrderDomain.builder().id(source.getIdPurchaseOrder()).build())
                .product(source.getProduct()!=null? ProductDomain.builder()
                        .id(source.getProduct().getId())
                        .name(source.getProduct().getProductName())
                        .stock(source.getProduct().getStock())
                        .price(source.getProduct().getPrice())
                        .smallImageUrl(source.getProduct().getSmallImageUrl())
                        .bigImageUrl(source.getProduct().getBigImageUrl())
                        .createdAt(source.getProduct().getProductCreatedAt())
                        .updatedAt(source.getProduct().getProductUpdatedAt())
                        .category(CategoryDomain.builder()
                                .id(source.getProduct().getIdCategory())
                                .name(source.getProduct().getCategoryName())
                                .description(source.getProduct().getCategoryDescription())
                                .createdAt(source.getProduct().getCategoryCreatedAt())
                                .updatedAt(source.getProduct().getCategoryUpdatedAt())
                                .build())
                        .build() :
                        source.getIdProduct() != null ?
                                ProductDomain.builder().id(source.getIdProduct()).build() : null)
                .quantity(source.getQuantity())
                .build();
    }
}
