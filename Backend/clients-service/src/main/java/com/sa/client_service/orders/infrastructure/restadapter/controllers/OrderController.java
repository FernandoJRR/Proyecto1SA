package com.sa.client_service.orders.infrastructure.restadapter.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sa.client_service.orders.application.dtos.CreateOrderDTO;
import com.sa.client_service.orders.application.inputports.CreateOrderInputPort;
import com.sa.client_service.orders.application.inputports.FindAllOrdersInputPort;
import com.sa.client_service.orders.application.inputports.FindOrderByIdInputPort;
import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.infrastructure.restadapter.dtos.CreateOrderRequest;
import com.sa.client_service.orders.infrastructure.restadapter.dtos.OrderHydratedResponse;
import com.sa.client_service.orders.infrastructure.restadapter.dtos.OrderResponse;
import com.sa.client_service.orders.infrastructure.restadapter.mappers.OrderHydrationAssembler;
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
    private final FindOrderByIdInputPort findOrderByIdInputPort;
    private final FindAllOrdersInputPort findAllOrdersInputPort;
    private final OrderRestMapper orderRestMapper;
    private final OrderHydrationAssembler orderHydrationAssembler;

    @Operation(summary = "Crear una nueva orden", description = "Este endpoint permite la creación de una nueva orden en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ORDER')")
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody CreateOrderRequest request)
            throws NotFoundException, InvalidParameterException {

        CreateOrderDTO createOrderDTO = orderRestMapper.toDTO(request);

        Order result = createOrderInputPort.handle(createOrderDTO);

        OrderResponse response = orderRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener una orden con su ID", description = "Este endpoint permite la obtencion de una orden en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderHydratedResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderHydratedResponse> getOrderById(
            @PathVariable("orderId") UUID orderId)
            throws NotFoundException {

        Order result = findOrderByIdInputPort.handle(orderId.toString());

        OrderHydratedResponse response = orderHydrationAssembler.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener todas las ordenes", description = "Este endpoint permite la obtencion de todas las ordenes en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ordenes obtenidas exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderHydratedResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping({"","/"})
    public ResponseEntity<List<OrderHydratedResponse>> getOrders() {

        List<Order> result = findAllOrdersInputPort.handle();

        List<OrderHydratedResponse> response = orderHydrationAssembler.toResponseList(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
