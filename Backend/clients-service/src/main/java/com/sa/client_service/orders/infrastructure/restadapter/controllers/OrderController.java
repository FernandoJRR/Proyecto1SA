package com.sa.client_service.orders.infrastructure.restadapter.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sa.client_service.orders.application.dtos.CreateOrderDTO;
import com.sa.client_service.orders.application.inputports.CreateOrderInputPort;
import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.infrastructure.restadapter.dtos.CreateOrderRequest;
import com.sa.client_service.orders.infrastructure.restadapter.dtos.OrderResponse;
import com.sa.client_service.orders.infrastructure.restadapter.mappers.OrderRestMapper;
import com.sa.shared.exceptions.InvalidParameterException;
import com.sa.shared.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")

@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderInputPort createOrderInputPort;
    private final OrderRestMapper orderRestMapper;

    @Operation(summary = "Crear una nueva orden", description = "Este endpoint permite la creación de una nueva orden en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ORDER')")
    public ResponseEntity<OrderResponse> createClient(
            @RequestBody CreateOrderRequest request)
            throws NotFoundException, InvalidParameterException {

        CreateOrderDTO createOrderDTO = orderRestMapper.toDTO(request);

        Order result = createOrderInputPort.handle(createOrderDTO);

        OrderResponse response = orderRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
