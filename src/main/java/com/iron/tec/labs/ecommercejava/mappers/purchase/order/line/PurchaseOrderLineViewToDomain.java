package com.iron.tec.labs.ecommercejava.mappers.purchase.order.line;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLineView;
import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import org.springframework.core.convert.converter.Converter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderLineViewToDomain implements Converter<@NonNull PurchaseOrderLineView,@NonNull PurchaseOrderLineDomain> {
    @Override
    public PurchaseOrderLineDomain convert(@NonNull PurchaseOrderLineView source) {
        return PurchaseOrderLineDomain.builder()
                .id(source.getId())
                .purchaseOrder(PurchaseOrderDomain.builder()
                        .id(source.getIdPurchaseOrder())
                        .build())
                .product(ProductDomain.builder()
                        .id(source.getIdProduct())
                        .name(source.getProductName())
                        .stock(source.getStock())
                        .price(source.getPrice())
                        .bigImageUrl(source.getBigImageUrl())
                        .smallImageUrl(source.getSmallImageUrl())
                        .category(CategoryDomain.builder()
                                .name(source.getCategoryName())
                                .build())
                        .build())
                .quantity(source.getQuantity())
                .build();
    }
}
