package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.dto.*;
import com.iron.tec.labs.ecommercejava.exceptions.Conflict;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import com.iron.tec.labs.ecommercejava.services.PurchaseOrderService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(PurchaseOrderController.class)
class PurchaseOrderControllerTest {
    @Autowired
    WebTestClient testClient;

    @MockBean
    PurchaseOrderService purchaseOrderService;

    @Value("classpath:json/purchaseOrder/responses/getPageResponse.json")
    Resource getPurchaseOrderPageResponse;

    @Value("classpath:json/purchaseOrder/responses/getByIdResponse.json")
    Resource getPurchaseOrderByIdResponse;

    @Value("classpath:json/purchaseOrder/requests/createRequest.json")
    Resource createPurchaseOrderRequest;

    @Value("classpath:json/purchaseOrder/requests/updateRequest.json")
    Resource updatePurchaseOrderRequest;

    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void getPurchaseOrderPage() {
        when(purchaseOrderService.getPurchaseOrderPage(any(ProductPageRequestDTO.class))).thenReturn(
                Mono.just(new PageResponseDTO<>(
                        Collections.singletonList(PurchaseOrderViewDTO.builder()
                                .id("e5ade578-6108-4c68-af78-0ca6c42c85ad")
                                .idUser("130b1b88-5850-4d25-b81f-786925d09ab7")
                                .email("admin@gmail.com")
                                .firstName("Alejandro")
                                .lastName("Admin")
                                .username("admin")
                                .status("PENDING")
                                .createdAt(LocalDateTime.parse("2023-01-21T21:37:51.647479", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")))
                                .status("PENDING")
                                .total(BigDecimal.valueOf(1000))
                                .build()), PageRequest.of(0, 1), 1)));
        testClient.get().uri("/v1/purchase-order/page?page=0&size=1")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json(ResourceUtils.readFile(getPurchaseOrderPageResponse));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void getById() {
        when(purchaseOrderService.getById(any(UUID.class))).thenReturn(Mono.just(PurchaseOrderDTO.builder()
                .id("e5ade578-6108-4c68-af78-0ca6c42c85ad")
                .idUser("130b1b88-5850-4d25-b81f-786925d09ab7")
                .line(PurchaseOrderLineDTO.builder()
                        .idProduct("f558f20e-aa01-41a3-bba0-45f0177e5344")
                        .quantity(10)
                        .stock(2)
                        .price(BigDecimal.valueOf(4))
                        .categoryName("Motherboards")
                        .smallImageUrl("https://www.youtube.com/small.jpg")
                        .productName("Mother ASUS ROG STRIX B550-XE Gaming Wifi AM4")
                        .build())
                .status("PENDING")
                .total(BigDecimal.valueOf(1000))
                .createdAt(LocalDateTime.parse("2023-01-21T21:37:51.647479", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")))
                .user(UserDTO.builder().username("admin").email("admin@gmail.com").firstName("Alejandro").lastName("Admin").build())
                .build()));
        testClient.get().uri("/v1/purchase-order/baffc3a4-5447-48ab-b9c0-7604e448371d")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json(ResourceUtils.readFile(getPurchaseOrderByIdResponse));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void createPurchaseOrder() {
        when(purchaseOrderService.createPurchaseOrder(any(), any())).thenReturn(Mono.just(PurchaseOrderDTO.builder()
                .id("63466fc5-dccd-43c2-a3c7-4028bd9684bb")
                .idUser(UUID.randomUUID().toString())
                .line(PurchaseOrderLineDTO.builder()
                        .idProduct("f558f20e-aa01-41a3-bba0-45f0177e5344")
                        .quantity(10)
                        .build())
                .status("PENDING")
                .total(BigDecimal.valueOf(1000))
                .build()));

        testClient.post().uri("/v1/purchase-order")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.readFile(createPurchaseOrderRequest))
                .exchange()
                .expectStatus()
                .isCreated();

    }

    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void createExistingPurchaseOrder() {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("test", "test", Collections.emptyList());
        when(purchaseOrderService.createPurchaseOrder(any(), any())).thenThrow(Conflict.class);

        testClient.post().uri("/v1/purchase-order")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.readFile(createPurchaseOrderRequest))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void updatePurchaseOrder() {

        when(purchaseOrderService.patchPurchaseOrder(anyString(), any())).thenReturn(Mono.empty());

        testClient.patch().uri("/v1/purchase-order/f833d126-6cb1-4759-af63-9972f106a51d")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.readFile(updatePurchaseOrderRequest))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

    }

    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void patchNonExistingPurchaseOrder() {

        when(purchaseOrderService.patchPurchaseOrder(anyString(), any())).thenThrow(NotFound.class);

        testClient.patch().uri("/v1/purchase-order/f833d126-6cb1-4759-af63-9972f106a51d")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.readFile(updatePurchaseOrderRequest))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

}
