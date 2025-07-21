package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.dto.*;
import com.iron.tec.labs.ecommercejava.services.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public PageResponseDTO<PurchaseOrderViewDTO> getPurchaseOrdersPaged(@Valid PageRequestDTO pageRequest,
                                                                        Authentication authentication) {
        log.info("Before purchaseOrders obtained");
        PageDomain<PurchaseOrderDomain> page = purchaseOrderService.getPurchaseOrderPage(pageRequest);
        PageResponseDTO<PurchaseOrderViewDTO> result = new PageResponseDTO<>(
                page.getContent().stream()
                        .map(po -> conversionService.convert(po, PurchaseOrderViewDTO.class))
                        .toList(),
                PageRequest.of(page.getNumber(), pageRequest.getSize()),
                page.getTotalElements()
        );
        log.info("PurchaseOrders obtained");
        return result;
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-key")},
            summary = "Get Purchase Order Detail", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PurchaseOrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @GetMapping("/{id}")
    public PurchaseOrderDTO getPurchaseOrder(@PathVariable UUID id,
                                             Authentication authentication) {
        log.info("Before purchaseOrder obtained");
        PurchaseOrderDomain po = purchaseOrderService.getById(id);
        PurchaseOrderDTO result = conversionService.convert(po, PurchaseOrderDTO.class);
        log.info("PurchaseOrder obtained");
        return result;
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-key")},
            summary = "Create Purchase Order", responses = {
            @ApiResponse(responseCode = "201", description = "Successful Operation", content = @Content),
            @ApiResponse(responseCode = "409", description = "Resource already exists", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PostMapping()
    public ResponseEntity<Void> createPurchaseOrder(@RequestBody @Valid PurchaseOrderCreationDTO purchaseOrderCreationDTO,
                                                    HttpServletRequest httpServletRequest,
                                                    Authentication authentication) {
        PurchaseOrderDomain domain = conversionService.convert(purchaseOrderCreationDTO, PurchaseOrderDomain.class);
        if (domain != null && authentication != null) {
            // Create a minimal user domain with just the ID for now
            // The full user details will be loaded when the entity is persisted
            AppUserDomain user = AppUserDomain.builder()
                    .id(UUID.fromString(authentication.getName()))
                    .build();
            domain.setUser(user);
        }
        log.info("Before creating purchaseOrder");
        PurchaseOrderDomain purchaseOrderDTO = purchaseOrderService.createPurchaseOrder(domain);
        log.info("PurchaseOrder created");
        return ResponseEntity.created(
                URI.create(httpServletRequest.getRequestURI().concat("/")
                        .concat(purchaseOrderCreationDTO.getId()))).build();
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-key")},
            summary = "Patch Purchase Order", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchPurchaseOrder(@PathVariable("id") String id,
                                                   @RequestBody @Valid PurchaseOrderPatchDTO purchaseOrder) {
        PurchaseOrderDomain domain = conversionService.convert(purchaseOrder, PurchaseOrderDomain.class);
        log.info("Before updating purchaseOrder");
        purchaseOrderService.patchPurchaseOrder(id, domain);
        log.info("PurchaseOrder updated");
        return ok().build();
    }

}
