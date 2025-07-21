package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.PurchaseOrderDAO;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.dto.PageRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private final PurchaseOrderDAO purchaseOrderDAO;
    private final StockValidator stockValidator;

    @Override
    public PurchaseOrderDomain getById(UUID id) {
        return purchaseOrderDAO.getById(id);
    }

    @Override
    public PurchaseOrderDomain createPurchaseOrder(PurchaseOrderDomain purchaseOrder) {
        PurchaseOrderDomain validatedOrder = stockValidator.validateStock(purchaseOrder);
        return purchaseOrderDAO.create(validatedOrder);
    }

    @Override
    public PurchaseOrderDomain patchPurchaseOrder(String id, PurchaseOrderDomain purchaseOrder) {
        PurchaseOrderDomain existing = purchaseOrderDAO.getById(UUID.fromString(id));
        existing.setStatus(purchaseOrder.getStatus());
        return purchaseOrderDAO.update(existing);
    }

    @Override
    public PageDomain<PurchaseOrderDomain> getPurchaseOrderPage(PageRequestDTO pageRequest) {
        return purchaseOrderDAO.getPage(pageRequest.getPage(), pageRequest.getSize(), PurchaseOrderDomain.builder().build());
    }

}
