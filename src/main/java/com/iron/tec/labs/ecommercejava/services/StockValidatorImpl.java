package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.repository.ProductRepository;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import com.iron.tec.labs.ecommercejava.exceptions.BadRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class StockValidatorImpl implements StockValidator {

    private final ProductRepository productRepository;

    @Override
    public Mono<PurchaseOrderDomain> validateStock(PurchaseOrderDomain purchaseOrder) {

        return productRepository.findByIdIn(purchaseOrder.getLines().stream().map(x -> x.getProduct().getId()).toList()).collectList()
                .map(products -> products.stream().allMatch(product -> {
                    var line = purchaseOrder.getLines().stream().filter(x -> x.getProduct().getId().equals(product.getId())).findFirst().orElse(null);
                    if (line == null) return false;
                    return product.getStock() >= line.getQuantity();
                })).handle((valid, sink) -> {
                    if(BooleanUtils.isTrue(valid)) {
                        sink.next(purchaseOrder);
                    }else{
                        sink.error(new BadRequest("Stock not available"));
                    }
                });
    }
}
