package com.iron.tec.labs.ecommercejava.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PurchaseOrderLineDTO {
    private String idProduct;
    private String productName;
    private Integer stock;
    private BigDecimal price;
    private String smallImageUrl;
    private String categoryName;
    private Integer quantity;
}
