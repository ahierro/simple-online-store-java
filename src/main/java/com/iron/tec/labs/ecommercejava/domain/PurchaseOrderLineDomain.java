package com.iron.tec.labs.ecommercejava.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderLineDomain {
    private UUID id;
    private PurchaseOrderDomain purchaseOrder;
    private ProductDomain product;
    private Integer quantity;
}
