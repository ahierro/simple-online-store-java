package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.PurchaseOrderDAO;
import com.iron.tec.labs.ecommercejava.db.entities.*;
import com.iron.tec.labs.ecommercejava.dto.*;
import com.iron.tec.labs.ecommercejava.enums.PurchaseOrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    PurchaseOrder purchaseOrder = PurchaseOrder.builder()
            .id(UUID.randomUUID())
            .idUser(appUser.getId())
            .idProduct(product.getId())
            .status("PENDING")
            .quantity(10)
            .total(BigDecimal.valueOf(1000))
            .build();

    @Test
    void createPurchaseOrderTest() {

        PurchaseOrderCreationDTO purchaseOrderCreationDTO = PurchaseOrderCreationDTO.builder()
                .id(ObjectUtils.nullSafeToString(purchaseOrder.getId()))
                .idProduct(ObjectUtils.nullSafeToString(product.getId()))
                .quantity(purchaseOrder.getQuantity())
                .total(purchaseOrder.getTotal())
                .build();
        PurchaseOrderDTO purchaseOrderDTO = PurchaseOrderDTO.builder()
                .id(purchaseOrder.getId().toString())
                .idProduct(product.getId().toString())
                .quantity(purchaseOrder.getQuantity())
                .total(purchaseOrder.getTotal())
                .build();
        when(conversionService.convert(any(PurchaseOrderCreationDTO.class), eq(PurchaseOrder.class)))
                .thenReturn(purchaseOrder);
        when(conversionService.convert(any(PurchaseOrder.class), eq(PurchaseOrderDTO.class)))
                .thenReturn(purchaseOrderDTO);
        ArgumentCaptor<PurchaseOrder> argumentCaptor = ArgumentCaptor.forClass(PurchaseOrder.class);
        when(purchaseOrderDAO.create(argumentCaptor.capture())).thenReturn(Mono.just(purchaseOrder));
        when(authentication.getName()).thenReturn(UUID.randomUUID().toString());
        PurchaseOrderDTO createdPurchaseOrder = purchaseOrderService.createPurchaseOrder(purchaseOrderCreationDTO, authentication).block();

        assertNotNull(createdPurchaseOrder);
        assertNotNull(purchaseOrder.getId());
        assertEquals(ObjectUtils.nullSafeToString(purchaseOrder.getId()), createdPurchaseOrder.getId());
        assertNotNull(argumentCaptor.getValue());
        assertEquals(purchaseOrder, argumentCaptor.getValue());
        verify(purchaseOrderDAO).create(any(PurchaseOrder.class));
    }

    @Test
    void testPatchExisting() {
        UUID id = UUID.randomUUID();
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .id(UUID.randomUUID())
                .idUser(appUser.getId())
                .idProduct(product.getId())
                .status("PENDING")
                .quantity(100)
                .total(BigDecimal.valueOf(100000))
                .build();
        PurchaseOrderPatchDTO purchaseOrderDTO = new PurchaseOrderPatchDTO(PurchaseOrderStatus.DELIVERED);
        ArgumentCaptor<PurchaseOrder> argumentCaptor = ArgumentCaptor.forClass(PurchaseOrder.class);
        when(purchaseOrderDAO.update(argumentCaptor.capture())).thenReturn(Mono.just(PurchaseOrder.builder()
                .id(UUID.randomUUID())
                .idUser(appUser.getId())
                .idProduct(product.getId())
                .status("DELIVERED")
                .quantity(100)
                .total(BigDecimal.valueOf(100000))
                .build()));
        when(purchaseOrderDAO.getById(id)).thenReturn(Mono.just(purchaseOrder));
        purchaseOrderService.patchPurchaseOrder(id.toString(), purchaseOrderDTO).block();

        assertEquals(purchaseOrder, argumentCaptor.getValue());
        verify(purchaseOrderDAO, times(1)).update(any(PurchaseOrder.class));
    }

    @Test
    void getPurchaseOrderPage() {
        when(purchaseOrderDAO.getPage(0, 1, PurchaseOrderView.builder().build())).thenReturn(Mono.just(new PageImpl<>(
                Arrays.asList(PurchaseOrderView.builder()
                                .id(UUID.randomUUID())
                                .idUser(appUser.getId())
                                .idProduct(product.getId())
                                .status("PENDING")
                                .quantity(100)
                                .total(BigDecimal.valueOf(100000))
                                .build(),
                        PurchaseOrderView.builder()
                                .id(UUID.randomUUID())
                                .idUser(appUser.getId())
                                .idProduct(product.getId())
                                .status("PENDING")
                                .quantity(100)
                                .total(BigDecimal.valueOf(100000))
                                .build()), PageRequest.of(0, 1), 2)));
        when(conversionService.convert(any(PurchaseOrderView.class), eq(PurchaseOrderViewDTO.class)))
                .thenAnswer(x -> PurchaseOrderViewDTO.builder()
                        .id(ObjectUtils.nullSafeToString(purchaseOrder.getId()))
                        .idProduct(ObjectUtils.nullSafeToString(product.getId()))
                        .quantity(purchaseOrder.getQuantity())
                        .total(purchaseOrder.getTotal())
                        .build());

        PageResponseDTO<PurchaseOrderViewDTO> page =
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
        when(purchaseOrderDAO.getById(any(UUID.class))).thenReturn(Mono.just(PurchaseOrder.builder()
                .id(UUID.randomUUID())
                .idUser(appUser.getId())
                        .line(PurchaseOrderLine.builder()
                                .id(UUID.randomUUID())
                                .idPurchaseOrder(UUID.randomUUID())
                                .idProduct(UUID.randomUUID())
                                .quantity(100)
                                .build())
                .status("PENDING")
                .total(BigDecimal.valueOf(100000))
                .build()));

        when(conversionService.convert(any(PurchaseOrder.class), eq(PurchaseOrderDTO.class)))
                .thenAnswer(x -> PurchaseOrderDTO.builder()
                        .id(ObjectUtils.nullSafeToString(purchaseOrder.getId()))
                        .line(PurchaseOrderLineDTO.builder()
                                .idProduct(ObjectUtils.nullSafeToString(product.getId()))
                                .quantity(1)
                                .build())
                        .total(purchaseOrder.getTotal())
                        .build());

        StepVerifier.create(purchaseOrderService.getById(UUID.randomUUID()))
                .expectNextCount(1)
                .verifyComplete();

    }
}
