package com.iron.tec.labs.ecommercejava.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;
@Data
@Builder
public class ProductUpdateDTO {
    @NotEmpty private String productName;
    @NotEmpty private String productDescription;
    @UUID private String brandId;
    @PositiveOrZero private Integer stock;
    @Positive private BigDecimal price;
    @NotEmpty @URL private String smallImageUrl;
    @NotEmpty @URL private String bigImageUrl;
    @UUID private String discountId;
}