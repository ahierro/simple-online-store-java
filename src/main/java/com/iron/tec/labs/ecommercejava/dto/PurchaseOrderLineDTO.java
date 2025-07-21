package com.iron.tec.labs.ecommercejava.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PurchaseOrderLineDTO {
    private Integer quantity;
    private ProductDTO product;
}