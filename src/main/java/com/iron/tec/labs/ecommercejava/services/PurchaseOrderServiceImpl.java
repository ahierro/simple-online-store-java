package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.PurchaseOrderDAO;
import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderView;
import com.iron.tec.labs.ecommercejava.dto.*;
import com.iron.tec.labs.ecommercejava.enums.PurchaseOrderStatus;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private final PurchaseOrderDAO purchaseOrderDAO;
    private final ConversionService conversionService;
    private final StockValidator stockValidator;

    @Override
    public Mono<PurchaseOrderDTO> getById(UUID id) {
        return purchaseOrderDAO.getById(id)
                .mapNotNull(product -> conversionService.convert(product, PurchaseOrderDTO.class));
    }

    public Mono<PurchaseOrderDTO> createPurchaseOrder(PurchaseOrderCreationDTO productCreationDTO, Authentication authentication) {
        var purchaseOrder = conversionService.convert(productCreationDTO, PurchaseOrder.class);
        if (purchaseOrder == null) throw new IllegalArgumentException();
        purchaseOrder.setStatus(PurchaseOrderStatus.PENDING.name());
        purchaseOrder.setIdUser(UUID.fromString(authentication.getName()));
        return stockValidator.validateStock(purchaseOrder)
                .flatMap(purchaseOrderDAO::create)
                .mapNotNull(product -> conversionService.convert(product, PurchaseOrderDTO.class));
    }

    @Override
    public Mono<PurchaseOrderDTO> patchPurchaseOrder(String id, PurchaseOrderPatchDTO productCreationDTO) {
        return purchaseOrderDAO.getById(UUID.fromString(id)).flatMap(purchaseOrder -> {
                    purchaseOrder.setStatus(productCreationDTO.getStatus().name());
                    return purchaseOrderDAO.update(purchaseOrder);
                }
        ).mapNotNull(productUpdated ->
                conversionService.convert(productUpdated, PurchaseOrderDTO.class));
    }

    @Override
    public Mono<PageResponseDTO<PurchaseOrderViewDTO>> getPurchaseOrderPage(PageRequestDTO pageRequest) {
        return purchaseOrderDAO.getPage(pageRequest.getPage(), pageRequest.getSize(), PurchaseOrderView.builder().build())
                .mapNotNull(page ->
                        new PageResponseDTO<>(
                                page.getContent().stream()
                                        .map(x -> conversionService.convert(x, PurchaseOrderViewDTO.class)).toList()
                                , page.getPageable()
                                , page.getTotalPages()));
    }

}
