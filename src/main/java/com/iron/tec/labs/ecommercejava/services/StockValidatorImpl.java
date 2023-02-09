package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLine;
import com.iron.tec.labs.ecommercejava.db.repository.ProductRepository;
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
    public Mono<PurchaseOrder> validateStock(PurchaseOrder purchaseOrder) {

        return productRepository.findByIdIn(purchaseOrder.getLines().stream().map(PurchaseOrderLine::getIdProduct).toList()).collectList()
                .map(products -> products.stream().allMatch(product -> {
                    var line = purchaseOrder.getLines().stream().filter(x -> x.getIdProduct().equals(product.getId())).findFirst().orElse(null);
                    if (line == null) return false;
                    return product.getStock() >= line.getQuantity();
                })).map(valid -> {
                    if(BooleanUtils.isTrue(valid)){
                        return purchaseOrder;
                    }
                    throw new BadRequest("Stock not available");
                });
    }
}
