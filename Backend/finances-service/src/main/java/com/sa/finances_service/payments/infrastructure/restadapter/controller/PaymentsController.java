package com.sa.finances_service.payments.infrastructure.restadapter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sa.finances_service.payments.application.dtos.CreatePaymentDTO;
import com.sa.finances_service.payments.application.inputports.CreatePaymentInputPort;
import com.sa.finances_service.payments.domain.Payment;
import com.sa.finances_service.payments.infrastructure.restadapter.dtos.CreatePaymentRequest;
import com.sa.finances_service.payments.infrastructure.restadapter.dtos.PaymentResponse;
import com.sa.finances_service.payments.infrastructure.restadapter.mappers.PaymentRestMapper;
import com.sa.shared.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments")

@RequiredArgsConstructor
public class PaymentsController {

    private final CreatePaymentInputPort createPaymentInputPort;
    private final PaymentRestMapper paymentRestMapper;

    @Operation(summary = "Crear una nueva orden", description = "Este endpoint permite la creación de una nueva orden en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_PAYMENT')")
    public ResponseEntity<PaymentResponse> createPayment(
            @RequestBody CreatePaymentRequest request)
            throws NotFoundException {

        CreatePaymentDTO createPaymentDTO = paymentRestMapper.toDTO(request);

        Payment result = createPaymentInputPort.handle(createPaymentDTO);

        PaymentResponse response = paymentRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
