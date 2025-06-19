package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderCreationDTO;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderLineCreationDTO;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class PurchaseOrderCreationDTOToDomain implements Converter<PurchaseOrderCreationDTO, PurchaseOrderDomain> {
    private final ConversionService conversionService;

    public PurchaseOrderCreationDTOToDomain(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public PurchaseOrderDomain convert(@NonNull PurchaseOrderCreationDTO source) {
        List<PurchaseOrderLineDomain> lines = new ArrayList<>();
        if (source.getLines() != null) {
            for (PurchaseOrderLineCreationDTO lineDTO : source.getLines()) {
                PurchaseOrderLineDomain line = conversionService.convert(lineDTO, PurchaseOrderLineDomain.class);
                if (line != null) {
                    lines.add(line);
                }
            }
        }
        return PurchaseOrderDomain.builder()
                .id(source.getId() != null ? UUID.fromString(source.getId()) : null)
                .lines(lines)
                .total(source.getTotal())
                .status("CREATED")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
