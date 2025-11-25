package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderViewDTO;
import org.springframework.core.convert.converter.Converter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderDomainToViewDTO implements Converter<@NonNull PurchaseOrderDomain,@NonNull PurchaseOrderViewDTO> {
    @Override
    public PurchaseOrderViewDTO convert(@NonNull PurchaseOrderDomain source) {
        return PurchaseOrderViewDTO.builder()
                .id(source.getId() != null ? source.getId().toString() : null)
                .idUser(source.getUser().getId().toString())
                .total(source.getTotal())
                .status(source.getStatus())
                .createdAt(source.getCreatedAt())
                .username(source.getUser() != null ? source.getUser().getUsername() : null)
                .email(source.getUser() != null ? source.getUser().getEmail() : null)
                .firstName(source.getUser() != null ? source.getUser().getFirstName() : null)
                .lastName(source.getUser() != null ? source.getUser().getLastName() : null)
                .build();
    }
}
