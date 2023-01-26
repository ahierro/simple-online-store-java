package com.iron.tec.labs.ecommercejava.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.UUID;

@Data
@Builder
public class PurchaseOrderLineCreationDTO {
    @NotNull
    @UUID
    private String idProduct;
    @NotNull
    @Positive
    @EqualsAndHashCode.Exclude
    private Integer quantity;
}
