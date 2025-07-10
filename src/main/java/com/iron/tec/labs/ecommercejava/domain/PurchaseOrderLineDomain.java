package com.iron.tec.labs.ecommercejava.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderLineDomain {
    private UUID id;
    private UUID idPurchaseOrder;
    private UUID idProduct;
    private String productName;
    private Integer stock;
    private BigDecimal price;
    private String bigImageUrl;
    private String smallImageUrl;
    private String categoryName;
    private Integer quantity;
}
