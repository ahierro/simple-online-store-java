package com.iron.tec.labs.ecommercejava.dto;

import com.iron.tec.labs.ecommercejava.enums.PurchaseOrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderPatchDTO {
    @NotNull
    private PurchaseOrderStatus status;

}