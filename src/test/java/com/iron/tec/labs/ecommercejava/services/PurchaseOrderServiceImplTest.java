package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.PurchaseOrderDAO;
import com.iron.tec.labs.ecommercejava.domain.*;
import com.iron.tec.labs.ecommercejava.dto.PageRequestDTO;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderPatchDTO;
import com.iron.tec.labs.ecommercejava.enums.PurchaseOrderStatus;
import com.iron.tec.labs.ecommercejava.exceptions.BadRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseOrderServiceImplTest {

    @Mock
    PurchaseOrderDAO purchaseOrderDAO;
    @Mock
    StockValidator stockValidator;

    @InjectMocks
    PurchaseOrderServiceImpl purchaseOrderService;
    AppUserDomain appUser = AppUserDomain.builder()
            .id(UUID.randomUUID())
            .username("admin")
            .firstName("John")
            .lastName("Smith")
            .email("admin@gmail.com")
            .build();

    ProductDomain product = ProductDomain.builder()
            .id(UUID.randomUUID())
            .name("Laptop")
            .stock(16)
            .price(BigDecimal.valueOf(100))
            .description("Laptop 16gb RAM 500gb SDD CPU 8 cores")
            .smallImageUrl("https://github.com/1.jpg")
            .bigImageUrl("https://github.com/2.jpg")
            .category(CategoryDomain.builder().id(UUID.randomUUID()).build())
            .build();
    PurchaseOrderLineDomain purchaseOrderLine = PurchaseOrderLineDomain.builder()
            .id(UUID.randomUUID())
            .product(ProductDomain.builder().id(product.getId()).build())
            .quantity(10)
            .build();
    PurchaseOrderDomain purchaseOrder = PurchaseOrderDomain.builder()
            .id(UUID.randomUUID())
            .user(AppUserDomain.builder().id(appUser.getId()).build())
            .line(purchaseOrderLine)
            .status("PENDING")
            .total(BigDecimal.valueOf(1000))
            .build();

    @Test
    @DisplayName("Should create a purchase order successfully")
    void createPurchaseOrderTest() {
        when(stockValidator.validateStock(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        ArgumentCaptor<PurchaseOrderDomain> argumentCaptor = ArgumentCaptor.forClass(PurchaseOrderDomain.class);
        when(purchaseOrderDAO.create(argumentCaptor.capture())).thenReturn(Mono.just(purchaseOrder));

        PurchaseOrderDomain createdPurchaseOrder = purchaseOrderService.createPurchaseOrder(purchaseOrder).block();

        assertNotNull(createdPurchaseOrder);
        assertEquals(purchaseOrder.getId(), createdPurchaseOrder.getId());
        assertEquals(purchaseOrder, argumentCaptor.getValue());
        verify(purchaseOrderDAO).create(any(PurchaseOrderDomain.class));
    }

    @Test
    @DisplayName("Should throw BadRequest when stock is insufficient")
    void createPurchaseOrderInsufficientStockTest() {

        when(stockValidator.validateStock(any())).thenAnswer(invocation -> Mono.error(new BadRequest("Stock is not enough")));

        StepVerifier.create(purchaseOrderService.createPurchaseOrder(purchaseOrder))
                .verifyErrorMatches(x -> {
                    assertInstanceOf(BadRequest.class, x);
                    assertEquals("Stock is not enough", x.getMessage());
                    return true;
                });

    }

    @Test
    @DisplayName("Should update purchase order status successfully")
    void updatePurchaseOrderStatusTest() {
        UUID id = UUID.randomUUID();
        PurchaseOrderLineDomain patchOrderLine = PurchaseOrderLineDomain.builder()
                .id(UUID.randomUUID())
                .product(ProductDomain.builder().id(product.getId()).build())
                .quantity(100)
                .build();
        PurchaseOrderDomain patchOrder = PurchaseOrderDomain.builder()
                .id(UUID.randomUUID())
                .user(AppUserDomain.builder().id(appUser.getId()).build())
                .line(patchOrderLine)
                .status("PENDING")
                .total(BigDecimal.valueOf(100000))
                .build();
        PurchaseOrderPatchDTO purchaseOrderDTO = new PurchaseOrderPatchDTO();
        purchaseOrderDTO.setStatus(PurchaseOrderStatus.DELIVERED);
        ArgumentCaptor<PurchaseOrderDomain> argumentCaptor = ArgumentCaptor.forClass(PurchaseOrderDomain.class);
        when(purchaseOrderDAO.update(argumentCaptor.capture())).thenReturn(Mono.just(patchOrder));
        when(purchaseOrderDAO.getById(id)).thenReturn(Mono.just(patchOrder));
        purchaseOrderService.patchPurchaseOrder(id.toString(), PurchaseOrderDomain.builder().status(purchaseOrderDTO.getStatus().name()).build()).block();

        assertEquals(patchOrder, argumentCaptor.getValue());
        verify(purchaseOrderDAO, times(1)).update(any(PurchaseOrderDomain.class));
    }

    @Test
    @DisplayName("Should return a page of purchase orders")
    void getPurchaseOrderPageTest() {
        PageDomain<PurchaseOrderDomain> pageDomain = PageDomain.<PurchaseOrderDomain>builder()
                .content(Arrays.asList(purchaseOrder, purchaseOrder))
                .totalPages(2)
                .totalElements(2)
                .number(0)
                .build();
        when(purchaseOrderDAO.getPage(eq(0), eq(1), any())).thenReturn(Mono.just(pageDomain));

        PageRequestDTO pageRequest = PageRequestDTO.builder().page(0).size(1).build();
        PageDomain<PurchaseOrderDomain> page = purchaseOrderService.getPurchaseOrderPage(pageRequest).block();

        assertNotNull(page);
        assertEquals(2, page.getTotalPages().intValue());
        assertEquals(0, page.getNumber().intValue());
        assertEquals(2, page.getTotalElements().intValue());
        assertNotNull(page.getContent());
        assertEquals(2, page.getContent().size());
    }

    @Test
    @DisplayName("Should return a purchase order by its ID")
    void testGetById() {
        when(purchaseOrderDAO.getById(any(UUID.class))).thenReturn(Mono.just(purchaseOrder));

        StepVerifier.create(purchaseOrderService.getById(UUID.randomUUID()))
                .expectNext(purchaseOrder)
                .verifyComplete();

    }
}
