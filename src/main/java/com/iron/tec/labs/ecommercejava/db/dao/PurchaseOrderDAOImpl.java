package com.iron.tec.labs.ecommercejava.db.dao;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
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
import reactor.core.publisher.Mono;

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
    private final org.springframework.core.convert.ConversionService conversionService;

    @Override
    public Mono<PurchaseOrderDomain> getById(UUID id) {
        return this.purchaseOrderRepository.findById(id)
                .flatMap(purchaseOrder ->
                        purchaseOrderLineViewRepository.getPurchaseOrderLineByIdPurchaseOrder(id).collectList()
                                .map(purchaseOrderLines -> {
                                    purchaseOrder.setViewLines(purchaseOrderLines);
                                    return purchaseOrder;
                                })
                )
                .flatMap(purchaseOrder ->
                        appUserRepository.findById(purchaseOrder.getIdUser())
                                .map(user -> {
                                    purchaseOrder.setUser(user);
                                    return purchaseOrder;
                                })
                )
                .mapNotNull(po -> conversionService.convert(po, PurchaseOrderDomain.class))
                .switchIfEmpty(Mono.error(new NotFound(messageService.getRequestLocalizedMessage(ERROR_PURCHASE_ORDER, NOT_FOUND, id.toString()))));
    }

    @Override
    public Mono<PageDomain<PurchaseOrderDomain>> getPage(int page, int size, PurchaseOrderDomain purchaseOrderExample) {
        PageRequest pageRequest = PageRequest.of(page, size);
        PurchaseOrderView exampleView = conversionService.convert(purchaseOrderExample, PurchaseOrderView.class);
        Example<PurchaseOrderView> example = Example.of(exampleView == null ? PurchaseOrderView.builder().build() : exampleView);
        return this.purchaseOrderViewRepository.findBy(example,
                reactiveFluentQuery -> reactiveFluentQuery
                        .sortBy(Sort.by("createdAt").descending())
                        .page(pageRequest))
                .map(pageResult -> new PageDomain<>(
                        pageResult.getContent().stream()
                                .map(po -> conversionService.convert(po, PurchaseOrderDomain.class))
                                .toList(),
                        pageResult.getTotalPages(),
                        (int) pageResult.getTotalElements(),
                        pageResult.getNumber()
                ));
    }

    @Override
    public Mono<PurchaseOrderDomain> create(PurchaseOrderDomain purchaseOrderDomain) {
        PurchaseOrder entity = conversionService.convert(purchaseOrderDomain, PurchaseOrder.class);
        if (entity == null) throw new RuntimeException("Entity cannot be null");
        return purchaseOrderRepository.save(entity).doOnError(DataIntegrityViolationException.class, e -> {
            log.error("Error creating purchase order", e);
            if(e.getMessage().contains("po_id_product_fk")) {
                throw new NotFound(messageService.getRequestLocalizedMessage(ERROR_PRODUCT,
                        NOT_FOUND));
            }
            throw new Conflict(messageService.getRequestLocalizedMessage(ERROR_PURCHASE_ORDER,
                    ALREADY_EXISTS, ObjectUtils.nullSafeToString(purchaseOrderDomain.getId())));
        }).map(saved -> conversionService.convert(saved, PurchaseOrderDomain.class));
    }

    @Override
    public Mono<PurchaseOrderDomain> update(PurchaseOrderDomain product) {
        PurchaseOrder entity = conversionService.convert(product, PurchaseOrder.class);
        if (entity == null) throw new RuntimeException("Entity cannot be null");
        entity.setUpdatedAt(LocalDateTime.now());
        return purchaseOrderRepository.save(entity).doOnError(TransientDataAccessResourceException.class, e -> {
            throw new NotFound(messageService.getRequestLocalizedMessage(ERROR_PURCHASE_ORDER, NOT_FOUND,
                    ObjectUtils.nullSafeToString(product.getId())));
        }).map(saved -> conversionService.convert(saved, PurchaseOrderDomain.class));
    }

}
