package com.iron.tec.labs.ecommercejava.mappers.purchase.order.line;

import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderLineDTO;
import com.iron.tec.labs.ecommercejava.mappers.product.ProductDomainToDTO;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PurchaseOrderLineDomainToDTO implements Converter<@NonNull PurchaseOrderLineDomain,@NonNull PurchaseOrderLineDTO> {
    
    private final ProductDomainToDTO productDomainToDTO;
    
    @Override
    public PurchaseOrderLineDTO convert(@NonNull PurchaseOrderLineDomain source) {
        ProductDTO productDTO = source.getProduct() != null ? productDomainToDTO.convert(source.getProduct()) : null;
        
        return PurchaseOrderLineDTO.builder()
                .quantity(source.getQuantity())
                .product(productDTO)
                .build();
    }
}
