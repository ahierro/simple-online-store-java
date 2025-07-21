package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.repository.ProductRepository;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.exceptions.BadRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StockValidatorImpl implements StockValidator {

    private final ProductRepository productRepository;

    @Override
    public PurchaseOrderDomain validateStock(PurchaseOrderDomain purchaseOrder) {
        List<com.iron.tec.labs.ecommercejava.db.entities.Product> products = 
                productRepository.findByIdIn(purchaseOrder.getLines().stream()
                        .map(line -> line.getProduct().getId()).toList());
        
        boolean valid = products.stream().allMatch(product -> {
            var line = purchaseOrder.getLines().stream()
                    .filter(x -> x.getProduct().getId().equals(product.getId()))
                    .findFirst().orElse(null);
            if (line == null) return false;
            return product.getStock() >= line.getQuantity();
        });
        
        if (valid) {
            return purchaseOrder;
        } else {
            throw new BadRequest("Stock not available");
        }
    }
}
