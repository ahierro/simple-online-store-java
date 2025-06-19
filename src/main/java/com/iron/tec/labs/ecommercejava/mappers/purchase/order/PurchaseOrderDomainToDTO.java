package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderDTO;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderLineDTO;
import com.iron.tec.labs.ecommercejava.dto.UserDTO;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderDomainToDTO implements Converter<PurchaseOrderDomain, PurchaseOrderDTO> {
    private final ConversionService conversionService;

    public PurchaseOrderDomainToDTO(ConversionService conversionService) {
        this.conversionService = conversionService;
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
                PurchaseOrderLineDTO dto = conversionService.convert(line, PurchaseOrderLineDTO.class);
                if (dto != null) {
                    builder.line(dto);
                }
            }
        }
        if (source.getUser() != null) {
            UserDTO userDTO = conversionService.convert(source.getUser(), UserDTO.class);
            builder.user(userDTO);
        }
        return builder.build();
    }
}
