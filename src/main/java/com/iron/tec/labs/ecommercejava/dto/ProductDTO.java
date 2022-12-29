package com.iron.tec.labs.ecommercejava.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductDTO {
    private String productId;
    private String productName;
    private String productDescription;
    private String brandId;
    private String brandName;
    private Integer stock;
    private BigDecimal price;
    private String smallImageUrl;
    private String bigImageUrl;
    private DiscountDTO discountId;
    private LocalDateTime createdAt;
}