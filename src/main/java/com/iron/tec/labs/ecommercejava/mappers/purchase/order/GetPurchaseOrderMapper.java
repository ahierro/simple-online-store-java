package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLineView;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderDTO;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderLineDTO;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.GetPurchaseOrderLineViewMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class GetPurchaseOrderMapper implements Converter<PurchaseOrder, PurchaseOrderDTO> {

    private final Converter<PurchaseOrderLineView, PurchaseOrderLineDTO> lineViewMapper;

    public GetPurchaseOrderMapper(GetPurchaseOrderLineViewMapper lineViewMapper) {
        this.lineViewMapper = lineViewMapper;
    }

    @Override
    public PurchaseOrderDTO convert(@NonNull PurchaseOrder source) {
        PurchaseOrderDTO.PurchaseOrderDTOBuilder builder = PurchaseOrderDTO.builder()
                .id(source.getId() != null ? source.getId().toString() : null)
                .idUser(source.getIdUser() != null ? source.getIdUser().toString() : null)
                .total(source.getTotal())
                .status(source.getStatus())
                .createdAt(source.getCreatedAt());

        if (source.getViewLines() != null) {
            for (PurchaseOrderLineView lineView : source.getViewLines()) {
                builder.line(lineViewMapper.convert(lineView));
            }
        }

        return builder.build();
    }
}