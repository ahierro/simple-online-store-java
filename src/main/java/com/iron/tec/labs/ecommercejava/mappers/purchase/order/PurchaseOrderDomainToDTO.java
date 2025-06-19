package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderDTO;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderLineDTO;
import com.iron.tec.labs.ecommercejava.dto.UserDTO;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.PurchaseOrderLineDomainToDTO;
import com.iron.tec.labs.ecommercejava.mappers.user.AppUserEntityToDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderDomainToDTO implements Converter<PurchaseOrderDomain, PurchaseOrderDTO> {
    private final PurchaseOrderLineDomainToDTO purchaseOrderLineDomainToEntity;
    private final AppUserEntityToDTO appUserEntityToDTO;

    public PurchaseOrderDomainToDTO(PurchaseOrderLineDomainToDTO purchaseOrderLineDomainToEntity,
                                    AppUserEntityToDTO appUserEntityToDTO) {
        this.purchaseOrderLineDomainToEntity = purchaseOrderLineDomainToEntity;
        this.appUserEntityToDTO = appUserEntityToDTO;
    }

    @Override
    public PurchaseOrderDTO convert(@NonNull PurchaseOrderDomain source) {
        PurchaseOrderDTO.PurchaseOrderDTOBuilder builder = PurchaseOrderDTO.builder()
                .id(source.getId() != null ? source.getId().toString() : null)
                .idUser(source.getIdUser() != null ? source.getIdUser().toString() : null)
                .total(source.getTotal())
                .status(source.getStatus())
                .createdAt(source.getCreatedAt());
        if (source.getLines() != null) {
            for (PurchaseOrderLineDomain line : source.getLines()) {
                PurchaseOrderLineDTO dto = purchaseOrderLineDomainToEntity.convert(line);
                if (dto != null) {
                    builder.line(dto);
                }
            }
        }
        if (source.getUser() != null) {
            UserDTO userDTO = appUserEntityToDTO.convert(source.getUser());
            builder.user(userDTO);
        }
        return builder.build();
    }
}
