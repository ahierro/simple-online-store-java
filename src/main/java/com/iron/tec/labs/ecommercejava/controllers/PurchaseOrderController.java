package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.dto.*;
import com.iron.tec.labs.ecommercejava.services.PurchaseOrderService;
import com.iron.tec.labs.ecommercejava.util.LoggingUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/purchase-order")
@Log4j2
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @GetMapping("/page")
    public Mono<PageResponseDTO<PurchaseOrderViewDTO>> getPurchaseOrdersPaged(@Valid ProductPageRequestDTO pageRequest,
                                                                          Authentication authentication) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before purchaseOrders obtained")))
                .then(purchaseOrderService.getPurchaseOrderPage(pageRequest))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("PurchaseOrders obtained")));
    }

    @GetMapping("/{id}")
    public Mono<PurchaseOrderDTO> getPurchaseOrder(@PathVariable UUID id,
                                                   Authentication authentication) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before purchaseOrders obtained")))
                .then(purchaseOrderService.getById(id))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("PurchaseOrders obtained")));
    }

    @PostMapping()
    public Mono<ResponseEntity<Void>> createPurchaseOrder(@RequestBody @Valid PurchaseOrderCreationDTO purchaseOrderCreationDTO,
                                                     ServerHttpRequest serverHttpRequest,
                                                          Authentication authentication) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before creating purchaseOrder")))
                .then(purchaseOrderService.createPurchaseOrder(purchaseOrderCreationDTO,authentication))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("PurchaseOrder created")))
                .map(purchaseOrderDTO -> ResponseEntity.created(
                        URI.create(serverHttpRequest.getPath().toString().concat("/")
                                .concat(purchaseOrderCreationDTO.getId().toString()))).build());
    }

    @PatchMapping("/{id}")
    public Mono<ResponseEntity<Void>> patchPurchaseOrder(@PathVariable("id") String id,
                                                         @RequestBody @Valid PurchaseOrderPatchDTO purchaseOrder) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before updating purchaseOrder")))
                .then(purchaseOrderService.patchPurchaseOrder(id, purchaseOrder))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("PurchaseOrder updated")))
                .map(x -> ok().build());
    }

}
