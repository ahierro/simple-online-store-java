package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.dto.*;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PurchaseOrderService {
    Mono<PurchaseOrderDTO> getById(UUID id);

    Mono<PurchaseOrderDTO> createPurchaseOrder(PurchaseOrderCreationDTO categoryCreationDTO, Authentication authentication);

    Mono<PurchaseOrderDTO> patchPurchaseOrder(String id, PurchaseOrderPatchDTO categoryCreationDTO);

    Mono<PageResponseDTO<PurchaseOrderViewDTO>> getPurchaseOrderPage(PageRequestDTO pageRequest);

}
