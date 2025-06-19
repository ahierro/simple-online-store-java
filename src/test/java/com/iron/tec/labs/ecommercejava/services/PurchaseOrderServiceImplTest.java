package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.PurchaseOrderDAO;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import com.iron.tec.labs.ecommercejava.dto.PageRequestDTO;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderPatchDTO;
import com.iron.tec.labs.ecommercejava.enums.PurchaseOrderStatus;
import com.iron.tec.labs.ecommercejava.exceptions.BadRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
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
    ConversionService conversionService;
    @Mock
    Authentication authentication;
    @Mock
    StockValidator stockValidator;

    @InjectMocks
    PurchaseOrderServiceImpl purchaseOrderService;
    AppUser appUser = AppUser.builder()
            .id(UUID.randomUUID())
            .username("admin")
            .password("$2a$10$7EVF8hBxswNOWMPfpIImruKVkUbNcL51KK.TueUqUPjnfdAghhJmC")
            .firstName("John")
            .lastName("Smith")
            .email("admin@gmail.com")
            .active(true)
            .locked(false)
            .authority("ROLE_ADMIN")
            .build();

    Category category = Category.builder()
            .id(UUID.randomUUID())
            .name("Motherboards")
            .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
            .build();
    Product product = Product.builder()
            .id(UUID.randomUUID())
            .name("Laptop")
            .stock(16)
            .price(BigDecimal.valueOf(100))
            .description("Laptop 16gb RAM 500gb SDD CPU 8 cores")
            .smallImageUrl("https://github.com/1.jpg")
            .bigImageUrl("https://github.com/2.jpg")
            .idCategory(category.getId())
            .build();
    PurchaseOrderDomain purchaseOrder = PurchaseOrderDomain.builder()
            .id(UUID.randomUUID())
            .idUser(appUser.getId())
            .line(PurchaseOrderLineDomain.builder().idProduct(product.getId()).quantity(10).build())
            .status("PENDING")
            .total(BigDecimal.valueOf(1000))
            .build();

    @Test
    void createPurchaseOrderTest() {
        when(stockValidator.validateStock(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        ArgumentCaptor<PurchaseOrderDomain> argumentCaptor = ArgumentCaptor.forClass(PurchaseOrderDomain.class);
        when(purchaseOrderDAO.create(argumentCaptor.capture())).thenReturn(Mono.just(purchaseOrder));
        when(authentication.getName()).thenReturn(UUID.randomUUID().toString());

        PurchaseOrderDomain createdPurchaseOrder = purchaseOrderService.createPurchaseOrder(purchaseOrder, authentication).block();

        assertNotNull(createdPurchaseOrder);
        assertEquals(purchaseOrder.getId(), createdPurchaseOrder.getId());
        assertEquals(purchaseOrder, argumentCaptor.getValue());
        verify(purchaseOrderDAO).create(any(PurchaseOrderDomain.class));
    }

    @Test()
    void createPurchaseOrderTestWithoutStock() {

        when(stockValidator.validateStock(any())).thenAnswer(invocation -> Mono.error(new BadRequest("Stock is not enough")));
        when(authentication.getName()).thenReturn(UUID.randomUUID().toString());

        StepVerifier.create(purchaseOrderService.createPurchaseOrder(purchaseOrder, authentication))
                .verifyErrorMatches(x -> {
                    assertTrue(x instanceof BadRequest);
                    assertEquals("Stock is not enough", x.getMessage());
                    return true;
                });

    }

    @Test
    void testPatchExisting() {
        UUID id = UUID.randomUUID();
        PurchaseOrderDomain purchaseOrder = PurchaseOrderDomain.builder()
                .id(UUID.randomUUID())
                .idUser(appUser.getId())
                .line(PurchaseOrderLineDomain.builder().idProduct(product.getId()).quantity(100).build())
                .status("PENDING")
                .total(BigDecimal.valueOf(100000))
                .build();
        PurchaseOrderPatchDTO purchaseOrderDTO = new PurchaseOrderPatchDTO(PurchaseOrderStatus.DELIVERED);
        ArgumentCaptor<PurchaseOrderDomain> argumentCaptor = ArgumentCaptor.forClass(PurchaseOrderDomain.class);
        when(purchaseOrderDAO.update(argumentCaptor.capture())).thenReturn(Mono.just(purchaseOrder.toBuilder().status("DELIVERED").build()));
        when(purchaseOrderDAO.getById(id)).thenReturn(Mono.just(purchaseOrder));
        purchaseOrderService.patchPurchaseOrder(id.toString(), PurchaseOrderDomain.builder().status(purchaseOrderDTO.getStatus().name()).build()).block();

        assertEquals(purchaseOrder, argumentCaptor.getValue());
        verify(purchaseOrderDAO, times(1)).update(any(PurchaseOrderDomain.class));
    }

    @Test
    void getPurchaseOrderPage() {
        when(purchaseOrderDAO.getPage(eq(0), eq(1), any())).thenReturn(Mono.just(new PageDomain<>(
                Arrays.asList(purchaseOrder, purchaseOrder),2,2,0)));

        PageDomain<PurchaseOrderDomain> page =
                purchaseOrderService.getPurchaseOrderPage(PageRequestDTO.builder().page(0).size(1).build()).block();

        assertNotNull(page);
        assertEquals(2, page.getTotalPages());
        assertEquals(0, page.getNumber());
        assertEquals(2, page.getTotalElements());
        assertNotNull(page.getContent());
        assertEquals(2, page.getContent().size());
    }

    @Test
    void testGetById() {
        when(purchaseOrderDAO.getById(any(UUID.class))).thenReturn(Mono.just(purchaseOrder));

        StepVerifier.create(purchaseOrderService.getById(UUID.randomUUID()))
                .expectNext(purchaseOrder)
                .verifyComplete();

    }
}
