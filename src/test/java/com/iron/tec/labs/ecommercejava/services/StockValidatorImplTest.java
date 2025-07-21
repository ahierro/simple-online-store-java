package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.db.repository.ProductRepository;
import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import com.iron.tec.labs.ecommercejava.exceptions.BadRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class StockValidatorImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    StockValidatorImpl stockValidatorImpl;

    @Test
    @DisplayName("Should throw BadRequest when stock is not available")
    void validateStockThrowsException() {
        UUID idProduct1 = UUID.randomUUID();
        UUID idProduct2 = UUID.randomUUID();

        Mockito.when(productRepository.findByIdIn(Mockito.anyList()))
                .thenReturn(Arrays.asList(
                        Product.builder().id(idProduct1).stock(2).build(),
                        Product.builder().id(idProduct2).stock(2).build()
                ));

        PurchaseOrderDomain purchaseOrder = PurchaseOrderDomain.builder()
                .line(PurchaseOrderLineDomain.builder()
                    .product(ProductDomain.builder().id(idProduct1).build())
                    .quantity(1).build())
                .line(PurchaseOrderLineDomain.builder()
                    .product(ProductDomain.builder().id(idProduct2).build())
                    .quantity(5).build())
                .build();

        try {
            stockValidatorImpl.validateStock(purchaseOrder);
            org.junit.jupiter.api.Assertions.fail("Expected BadRequest exception was not thrown");
        } catch (BadRequest e) {
            org.junit.jupiter.api.Assertions.assertEquals("Stock not available", e.getMessage());
        }

    }

    @Test
    @DisplayName("Should validate stock successfully when available")
    void validateStockOk() {
        UUID idProduct1 = UUID.randomUUID();
        UUID idProduct2 = UUID.randomUUID();

        Mockito.when(productRepository.findByIdIn(Mockito.anyList()))
                .thenReturn(Arrays.asList(
                        Product.builder().id(idProduct1).stock(20).build(),
                        Product.builder().id(idProduct2).stock(20).build()
                ));

        PurchaseOrderDomain purchaseOrder = PurchaseOrderDomain.builder()
                .line(PurchaseOrderLineDomain.builder()
                    .product(ProductDomain.builder().id(idProduct1).build())
                    .quantity(1).build())
                .line(PurchaseOrderLineDomain.builder()
                    .product(ProductDomain.builder().id(idProduct2).build())
                    .quantity(5).build())
                .build();

        PurchaseOrderDomain result = stockValidatorImpl.validateStock(purchaseOrder);
        org.junit.jupiter.api.Assertions.assertNotNull(result);

    }
}