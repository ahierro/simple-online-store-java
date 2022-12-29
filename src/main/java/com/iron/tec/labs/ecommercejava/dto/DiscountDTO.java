package com.iron.tec.labs.ecommercejava.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Builder
public class DiscountDTO{
    private String discountId;
    private BigDecimal percentage;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
}