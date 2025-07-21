package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import com.iron.tec.labs.ecommercejava.db.repository.PurchaseOrderLineViewRepository;
import com.iron.tec.labs.ecommercejava.db.repository.PurchaseOrderRepository;
import com.iron.tec.labs.ecommercejava.db.repository.PurchaseOrderViewRepository;
import com.iron.tec.labs.ecommercejava.db.repository.UserRepository;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import com.iron.tec.labs.ecommercejava.services.MessageService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static com.iron.tec.labs.ecommercejava.constants.Constants.ERROR_PURCHASE_ORDER;
import static com.iron.tec.labs.ecommercejava.constants.Constants.NOT_FOUND;

@Repository
@AllArgsConstructor
@Log4j2
@Transactional
public class PurchaseOrderDAOTransactionHandlerImpl implements PurchaseOrderDAOTransactionHandler {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final MessageService messageService;
    private final PurchaseOrderViewRepository purchaseOrderViewRepository;
    private final PurchaseOrderLineViewRepository purchaseOrderLineViewRepository;
    private final UserRepository appUserRepository;
    private final org.springframework.core.convert.ConversionService conversionService;


    @Override
    public PurchaseOrderDomain create(PurchaseOrderDomain purchaseOrderDomain) {
        PurchaseOrder entity = conversionService.convert(purchaseOrderDomain, PurchaseOrder.class);
        if (entity == null) throw new RuntimeException("Entity cannot be null");

        PurchaseOrder saved = purchaseOrderRepository.save(entity);
        return conversionService.convert(saved, PurchaseOrderDomain.class);
    }

    @Override
    public PurchaseOrderDomain update(PurchaseOrderDomain purchaseOrderDomain) {
        Optional<PurchaseOrder> po = purchaseOrderRepository.findById(purchaseOrderDomain.getId());
        if (po.isEmpty()) {
            throw new NotFound(messageService.getRequestLocalizedMessage(ERROR_PURCHASE_ORDER,
                    NOT_FOUND, ObjectUtils.nullSafeToString(purchaseOrderDomain.getId())));
        } else {
            PurchaseOrder existing = po.get();
            existing.setUpdatedAt(LocalDateTime.now());
            existing.setStatus(purchaseOrderDomain.getStatus());
            existing.setLines(new ArrayList<>());
            PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(existing);
            return conversionService.convert(savedPurchaseOrder, PurchaseOrderDomain.class);
        }
    }

}
