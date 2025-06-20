package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLine;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderCreationDTO;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderLineCreationDTO;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.SavePurchaseOrderLineMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class SavePurchaseOrderMapper implements Converter<PurchaseOrderCreationDTO, PurchaseOrder> {

    private final Converter<PurchaseOrderLineCreationDTO, PurchaseOrderLine> lineMapper;

    public SavePurchaseOrderMapper(SavePurchaseOrderLineMapper lineMapper) {
        this.lineMapper = lineMapper;
    }

    @Override
    public PurchaseOrder convert(@NonNull PurchaseOrderCreationDTO source) {
        PurchaseOrder.PurchaseOrderBuilder<?, ?> builder = PurchaseOrder.builder()
                .id(source.getId() != null ? UUID.fromString(source.getId()) : null)
                .total(source.getTotal())
                .status("CREATED")
                .createdAt(LocalDateTime.now());

        if (source.getLines() != null && !source.getLines().isEmpty()) {
            List<PurchaseOrderLine> lines = new ArrayList<>();
            for (PurchaseOrderLineCreationDTO lineDTO : source.getLines()) {
                PurchaseOrderLine line = lineMapper.convert(lineDTO);
                if (line != null) {
                    lines.add(line);
                }
            }
            builder.lines(lines);
        }

        return builder.build();
    }
}