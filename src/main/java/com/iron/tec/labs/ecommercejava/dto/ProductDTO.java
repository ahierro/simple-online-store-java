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
    private Integer stock;
    private BigDecimal price;
    private String smallImageUrl;
    private String bigImageUrl;
    private LocalDateTime createdAt;
    private CategoryDTO category;
}