package com.sa.finances_service.payments.infrastructure.restadapter.controller;

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

import com.sa.finances_service.payments.application.dtos.CreatePaymentDTO;
import com.sa.finances_service.payments.application.inputports.CreatePaymentInputPort;
import com.sa.finances_service.payments.application.inputports.FindPaymentByIdInputPort;
import com.sa.finances_service.payments.application.inputports.FindPaymentsInputPort;
import com.sa.finances_service.payments.domain.Payment;
import com.sa.finances_service.payments.infrastructure.restadapter.dtos.CreatePaymentRequest;
import com.sa.finances_service.payments.infrastructure.restadapter.dtos.PaymentHydratedResponse;
import com.sa.finances_service.payments.infrastructure.restadapter.dtos.PaymentResponse;
import com.sa.finances_service.payments.infrastructure.restadapter.mappers.PaymentResponseAssembler;
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
    private final FindPaymentsInputPort findPaymentsInputPort;
    private final FindPaymentByIdInputPort findPaymentByIdInputPort;
    private final PaymentRestMapper paymentRestMapper;
    private final PaymentResponseAssembler paymentResponseAssembler;

    @Operation(summary = "Crear un nuevo pago", description = "Este endpoint permite la creación de un nuevo pago en el sistema.")
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

    @Operation(summary = "Obtener un pago por ID", description = "Este endpoint permite la obtencion de un pago en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentHydratedResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentHydratedResponse> getPayment(
        @PathVariable("paymentId") UUID paymentId)
        throws NotFoundException {

        Payment result = findPaymentByIdInputPort.handle(paymentId.toString());

        PaymentHydratedResponse response = paymentResponseAssembler.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener todos los pagos", description = "Este endpoint permite la obtencion de todos los pagos en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagos obtenidos exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentHydratedResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping({"","/"})
    public ResponseEntity<List<PaymentHydratedResponse>> getPayments() {

        List<Payment> result = findPaymentsInputPort.handle();

        List<PaymentHydratedResponse> response = paymentResponseAssembler.toResponseList(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
