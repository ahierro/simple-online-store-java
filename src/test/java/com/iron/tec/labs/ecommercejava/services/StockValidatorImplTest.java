package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.db.repository.ProductRepository;
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
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.UUID;
import java.util.stream.Stream;

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
                .thenReturn(Flux.fromStream(Stream.of(Product.builder().id(idProduct1).stock(2).build(),
                        Product.builder().id(idProduct2).stock(2).build())));

        PurchaseOrderDomain purchaseOrder = PurchaseOrderDomain.builder()
                .line(PurchaseOrderLineDomain.builder().idProduct(idProduct1).quantity(1).build())
                .line(PurchaseOrderLineDomain.builder().idProduct(idProduct2).quantity(5).build())
                .build();

        StepVerifier.create(stockValidatorImpl.validateStock(purchaseOrder))
                .verifyErrorMatches(throwable -> throwable instanceof BadRequest
                        && throwable.getMessage().equals("Stock not available"));

    }

    @Test
    @DisplayName("Should validate stock successfully when available")
    void validateStockOk() {
        UUID idProduct1 = UUID.randomUUID();
        UUID idProduct2 = UUID.randomUUID();

        Mockito.when(productRepository.findByIdIn(Mockito.anyList()))
                .thenReturn(Flux.fromStream(Stream.of(Product.builder().id(idProduct1).stock(20).build(),
                        Product.builder().id(idProduct2).stock(20).build())));

        PurchaseOrderDomain purchaseOrder = PurchaseOrderDomain.builder()
                .line(PurchaseOrderLineDomain.builder().idProduct(idProduct1).quantity(1).build())
                .line(PurchaseOrderLineDomain.builder().idProduct(idProduct2).quantity(5).build())
                .build();

        StepVerifier.create(stockValidatorImpl.validateStock(purchaseOrder))
                .expectNextCount(1)
                .verifyComplete();

    }
}