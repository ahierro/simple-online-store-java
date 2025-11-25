package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderCreationDTO;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderLineCreationDTO;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.PurchaseOrderLineCreationDTOToDomain;
import org.springframework.core.convert.converter.Converter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.iron.tec.labs.ecommercejava.enums.PurchaseOrderStatus.PENDING;

@Component
public class PurchaseOrderCreationDTOToDomain implements Converter<@NonNull PurchaseOrderCreationDTO,@NonNull PurchaseOrderDomain> {
    private final PurchaseOrderLineCreationDTOToDomain conversionService;

    public PurchaseOrderCreationDTOToDomain(PurchaseOrderLineCreationDTOToDomain conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public PurchaseOrderDomain convert(@NonNull PurchaseOrderCreationDTO source) {
        List<PurchaseOrderLineDomain> lines = new ArrayList<>();
        if (source.getLines() != null) {
            for (PurchaseOrderLineCreationDTO lineDTO : source.getLines()) {
                PurchaseOrderLineDomain line = conversionService.convert(lineDTO);
                if (line != null) {
                    line.setPurchaseOrder(PurchaseOrderDomain.builder()
                            .id(UUID.fromString(source.getId())).build());
                    lines.add(line);
                }
            }
        }
        PurchaseOrderDomain purchaseOrderDomain = PurchaseOrderDomain.builder()
                .id(source.getId() != null ? UUID.fromString(source.getId()) : null)
                .lines(lines)
                .total(source.getTotal())
                .status(PENDING.name())
                .createdAt(LocalDateTime.now())
                .build();

        purchaseOrderDomain.getLines()
                .forEach(line -> line.setPurchaseOrder(purchaseOrderDomain));

        return purchaseOrderDomain;
    }
}
