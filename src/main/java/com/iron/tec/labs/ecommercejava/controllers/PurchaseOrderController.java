package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.db.entities.AppUser;
import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.dto.*;
import com.iron.tec.labs.ecommercejava.services.PurchaseOrderService;
import com.iron.tec.labs.ecommercejava.util.LoggingUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping("/api/purchase-order")
@Log4j2
@Tag(name = "Purchase Orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;
    private final ConversionService conversionService;

    @Operation(security = {@SecurityRequirement(name = "bearer-key")},
            parameters = {@Parameter(in = ParameterIn.QUERY, name = "page", description = "Page"),
                    @Parameter(in = ParameterIn.QUERY, name = "size", description = "Size")},
            summary = "Get page of purchase orders", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PurchaseOrderPage.class)))})
    @GetMapping("/page")
    public Mono<PageResponseDTO<PurchaseOrderViewDTO>> getPurchaseOrdersPaged(@Valid PageRequestDTO pageRequest,
                                                                              Authentication authentication) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before purchaseOrders obtained")))
                .then(purchaseOrderService.getPurchaseOrderPage(pageRequest)
                        .mapNotNull(page -> new PageResponseDTO<>(
                                page.getContent().stream()
                                        .map(po -> conversionService.convert(po, PurchaseOrderViewDTO.class))
                                        .toList(),
                                PageRequest.of(page.getNumber(), pageRequest.getSize()),
                                page.getTotalElements()
                        )))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("PurchaseOrders obtained")));
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-key")},
            summary = "Get Purchase Order Detail", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PurchaseOrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @GetMapping("/{id}")
    public Mono<PurchaseOrderDTO> getPurchaseOrder(@PathVariable UUID id,
                                                   Authentication authentication) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before purchaseOrders obtained")))
                .then(purchaseOrderService.getById(id)
                        .mapNotNull(po -> conversionService.convert(po, PurchaseOrderDTO.class)))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("PurchaseOrders obtained")));
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-key")},
            summary = "Create Purchase Order", responses = {
            @ApiResponse(responseCode = "201", description = "Successful Operation", content = @Content),
            @ApiResponse(responseCode = "409", description = "Resource already exists", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PostMapping()
    public Mono<ResponseEntity<Void>> createPurchaseOrder(@RequestBody @Valid PurchaseOrderCreationDTO purchaseOrderCreationDTO,
                                                          ServerHttpRequest serverHttpRequest,
                                                          Authentication authentication) {
        PurchaseOrderDomain domain = conversionService.convert(purchaseOrderCreationDTO, PurchaseOrderDomain.class);
        if (domain != null && authentication != null) {
            AppUserDomain user = AppUserDomain.builder()
                    .id(UUID.fromString(authentication.getName()))
                    .build();
            domain.setUser(user);
        }
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before creating purchaseOrder")))
                .then(purchaseOrderService.createPurchaseOrder(domain))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("PurchaseOrder created")))
                .map(purchaseOrderDTO -> ResponseEntity.created(
                        URI.create(serverHttpRequest.getPath().toString().concat("/")
                                .concat(purchaseOrderCreationDTO.getId()))).build());
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-key")},
            summary = "Patch Category", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @PatchMapping("/{id}")
    public Mono<ResponseEntity<Void>> patchPurchaseOrder(@PathVariable("id") String id,
                                                         @RequestBody @Valid PurchaseOrderPatchDTO purchaseOrder) {
        PurchaseOrderDomain domain = conversionService.convert(purchaseOrder, PurchaseOrderDomain.class);
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before updating purchaseOrder")))
                .then(purchaseOrderService.patchPurchaseOrder(id, domain))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("PurchaseOrder updated")))
                .map(x -> ok().build());
    }

}
