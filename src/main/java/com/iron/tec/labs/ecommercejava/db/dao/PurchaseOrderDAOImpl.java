package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLineView;
import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderView;
import com.iron.tec.labs.ecommercejava.db.repository.PurchaseOrderLineViewRepository;
import com.iron.tec.labs.ecommercejava.db.repository.PurchaseOrderRepository;
import com.iron.tec.labs.ecommercejava.db.repository.PurchaseOrderViewRepository;
import com.iron.tec.labs.ecommercejava.db.repository.UserRepository;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.exceptions.Conflict;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import com.iron.tec.labs.ecommercejava.services.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.iron.tec.labs.ecommercejava.constants.Constants.*;

@Repository
@AllArgsConstructor
@Log4j2
public class PurchaseOrderDAOImpl implements PurchaseOrderDAO {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final MessageService messageService;
    private final PurchaseOrderViewRepository purchaseOrderViewRepository;
    private final PurchaseOrderLineViewRepository purchaseOrderLineViewRepository;
    private final org.springframework.core.convert.ConversionService conversionService;

    @Override
    public PurchaseOrderDomain getById(UUID id) {
        Optional<PurchaseOrder> purchaseOrderOpt = this.purchaseOrderRepository.findById(id);
        if (purchaseOrderOpt.isEmpty()) {
            throw new NotFound(messageService.getRequestLocalizedMessage(ERROR_PURCHASE_ORDER, NOT_FOUND, id.toString()));
        }

        PurchaseOrder purchaseOrder = purchaseOrderOpt.get();

        // Get purchase order lines
        List<PurchaseOrderLineView> purchaseOrderLines = purchaseOrderLineViewRepository.getPurchaseOrderLineByIdPurchaseOrder(id);
        purchaseOrder.setViewLines(purchaseOrderLines);

        // User is already loaded through the JPA association
        return conversionService.convert(purchaseOrder, PurchaseOrderDomain.class);
    }

    @Override
    public PageDomain<PurchaseOrderDomain> getPage(int page, int size, PurchaseOrderDomain purchaseOrderExample) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        PurchaseOrderView exampleView = conversionService.convert(purchaseOrderExample, PurchaseOrderView.class);
        Example<PurchaseOrderView> example = Example.of(exampleView == null ? PurchaseOrderView.builder().build() : exampleView);

        Page<PurchaseOrderView> pageResult = this.purchaseOrderViewRepository.findAll(example, pageRequest);

        return new PageDomain<>(
                pageResult.getContent().stream()
                        .map(po -> conversionService.convert(po, PurchaseOrderDomain.class))
                        .toList(),
                pageResult.getTotalPages(),
                (int) pageResult.getTotalElements(),
                pageResult.getNumber()
        );
    }

    @Override
    public PurchaseOrderDomain create(PurchaseOrderDomain purchaseOrderDomain) {
        try {
            PurchaseOrder entity = conversionService.convert(purchaseOrderDomain, PurchaseOrder.class);
            if (entity == null) throw new RuntimeException("Entity cannot be null");

            PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(entity);
            return conversionService.convert(savedPurchaseOrder, PurchaseOrderDomain.class);

        } catch (DataIntegrityViolationException e) {
            log.error("Error creating purchase order", e);
            if (e.getMessage().contains("po_id_product_fk")) {
                throw new NotFound(messageService.getRequestLocalizedMessage(ERROR_PRODUCT, NOT_FOUND));
            }
            throw new Conflict(messageService.getRequestLocalizedMessage(ERROR_PURCHASE_ORDER,
                    ALREADY_EXISTS, ObjectUtils.nullSafeToString(purchaseOrderDomain.getId())));
        }
    }

    @Override
    public PurchaseOrderDomain update(PurchaseOrderDomain purchaseOrderDomain) {
        Optional<PurchaseOrder> po = purchaseOrderRepository.findById(purchaseOrderDomain.getId());
        if (po.isEmpty()) {
            throw new NotFound(messageService.getRequestLocalizedMessage(ERROR_PURCHASE_ORDER,
                    NOT_FOUND, ObjectUtils.nullSafeToString(purchaseOrderDomain.getId())));
        }
        PurchaseOrder existing = po.get();
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setStatus(purchaseOrderDomain.getStatus());
        existing.setLines(new ArrayList<>());
        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(existing);
        return conversionService.convert(savedPurchaseOrder, PurchaseOrderDomain.class);
    }

}
