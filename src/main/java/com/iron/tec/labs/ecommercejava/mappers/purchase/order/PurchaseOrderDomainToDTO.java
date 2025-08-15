package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderDTO;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderLineDTO;
import com.iron.tec.labs.ecommercejava.dto.UserDTO;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.PurchaseOrderLineDomainToDTO;
import com.iron.tec.labs.ecommercejava.mappers.user.AppUserDomainToDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderDomainToDTO implements Converter<PurchaseOrderDomain, PurchaseOrderDTO> {
    private final PurchaseOrderLineDomainToDTO purchaseOrderLineDomainToEntity;
    private final AppUserDomainToDTO appUserDomainToDTO;

    public PurchaseOrderDomainToDTO(PurchaseOrderLineDomainToDTO purchaseOrderLineDomainToEntity,
                                    AppUserDomainToDTO appUserDomainToDTO) {
        this.purchaseOrderLineDomainToEntity = purchaseOrderLineDomainToEntity;
        this.appUserDomainToDTO = appUserDomainToDTO;
    }

    @Override
    public PurchaseOrderDTO convert(@NonNull PurchaseOrderDomain source) {
        PurchaseOrderDTO.PurchaseOrderDTOBuilder builder = PurchaseOrderDTO.builder()
                .id(source.getId() != null ? source.getId().toString() : null)
                .total(source.getTotal())
                .status(source.getStatus())
                .createdAt(source.getCreatedAt());
        
        // Set idUser from user association
        if (source.getUser() != null) {
            UserDTO userDTO = appUserDomainToDTO.convert(source.getUser());
            builder.user(userDTO);
        }
        
        if (source.getLines() != null) {
            for (PurchaseOrderLineDomain line : source.getLines()) {
                PurchaseOrderLineDTO dto = purchaseOrderLineDomainToEntity.convert(line);
                if (dto != null) {
                    builder.line(dto);
                }
            }
        }
        
        return builder.build();
    }
}
