package com.iron.tec.labs.ecommercejava.dto;

import java.util.List;

public class PurchaseOrderPage extends PageResponseDTO<PurchaseOrderViewDTO>{
    public PurchaseOrderPage(List<PurchaseOrderViewDTO> content) {
        super(content);
    }
}
