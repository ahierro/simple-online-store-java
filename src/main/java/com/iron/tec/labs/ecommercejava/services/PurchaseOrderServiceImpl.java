package com.iron.tec.labs.ecommercejava.services;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.iron.tec.labs.ecommercejava.db.dao.PurchaseOrderDAO;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.dto.PageRequestDTO;
import com.iron.tec.labs.ecommercejava.enums.PurchaseOrderStatus;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private final PurchaseOrderDAO purchaseOrderDAO;
    private final StockValidator stockValidator;

    @Override
    public Mono<PurchaseOrderDomain> getById(UUID id) {
        return purchaseOrderDAO.getById(id);
    }

    @Override
    public Mono<PurchaseOrderDomain> createPurchaseOrder(PurchaseOrderDomain purchaseOrder) {
        return stockValidator.validateStock(purchaseOrder)
                .flatMap(purchaseOrderDAO::create);
    }

    @Override
    public Mono<PurchaseOrderDomain> patchPurchaseOrder(String id, PurchaseOrderDomain purchaseOrder) {
        return purchaseOrderDAO.getById(UUID.fromString(id)).flatMap(existing -> {
                    existing.setStatus(purchaseOrder.getStatus());
                    return purchaseOrderDAO.update(existing);
                }
        );
    }

    @Override
    public Mono<PageDomain<PurchaseOrderDomain>> getPurchaseOrderPage(PageRequestDTO pageRequest) {
        return purchaseOrderDAO.getPage(pageRequest.getPage(), pageRequest.getSize(), PurchaseOrderDomain.builder().build());
    }

}
