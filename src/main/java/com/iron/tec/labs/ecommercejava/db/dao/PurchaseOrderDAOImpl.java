package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderView;
import com.iron.tec.labs.ecommercejava.db.repository.PurchaseOrderLineViewRepository;
import com.iron.tec.labs.ecommercejava.db.repository.PurchaseOrderRepository;
import com.iron.tec.labs.ecommercejava.db.repository.PurchaseOrderViewRepository;
import com.iron.tec.labs.ecommercejava.db.repository.UserRepository;
import com.iron.tec.labs.ecommercejava.exceptions.Conflict;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import com.iron.tec.labs.ecommercejava.services.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.iron.tec.labs.ecommercejava.constants.Constants.*;

@Repository
@AllArgsConstructor
@Transactional
@Log4j2
public class PurchaseOrderDAOImpl implements PurchaseOrderDAO {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final MessageService messageService;
    private final PurchaseOrderViewRepository purchaseOrderViewRepository;
    private final PurchaseOrderLineViewRepository purchaseOrderLineViewRepository;
    private final UserRepository appUserRepository;

    @Override
    public Mono<PurchaseOrder> getById(UUID id) {
        return this.purchaseOrderRepository.findById(id).switchIfEmpty(Mono.defer(() -> {
                    throw new NotFound(messageService.getRequestLocalizedMessage(ERROR_PURCHASE_ORDER, NOT_FOUND, id.toString()));
                }))
                .zipWith(purchaseOrderLineViewRepository.getPurchaseOrderLineByIdPurchaseOrder(id).collectList(),
                        (purchaseOrder, purchaseOrderLines) -> {
                            purchaseOrder.setViewLines(purchaseOrderLines);
                            return purchaseOrder;
                        }).flatMap(purchaseOrder -> appUserRepository.findById(purchaseOrder.getIdUser()).map(user -> {
                    purchaseOrder.setUser(user);
                    return purchaseOrder;
                }));
    }

    @Override
    public Mono<Page<PurchaseOrderView>> getPage(int page, int size, PurchaseOrderView purchaseOrderExample) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Example<PurchaseOrderView> example = Example.of(purchaseOrderExample);
        return this.purchaseOrderViewRepository.findBy(example,
                reactiveFluentQuery -> reactiveFluentQuery
                        .sortBy(Sort.by("createdAt").descending())
                        .page(pageRequest));
    }

    @Override
    public Mono<PurchaseOrder> create(PurchaseOrder product) {
        return purchaseOrderRepository.save(product).doOnError(DataIntegrityViolationException.class, e -> {
            throw new Conflict(messageService.getRequestLocalizedMessage(ERROR_PURCHASE_ORDER,
                    ALREADY_EXISTS, ObjectUtils.nullSafeToString(product.getId())));
        });
    }

    @Override
    public Mono<PurchaseOrder> update(PurchaseOrder product) {
        return purchaseOrderRepository.save(product).doOnError(TransientDataAccessResourceException.class, e -> {
            throw new NotFound(messageService.getRequestLocalizedMessage(ERROR_PURCHASE_ORDER, NOT_FOUND,
                    ObjectUtils.nullSafeToString(product.getId())));
        });
    }

}
