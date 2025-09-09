package com.sa.client_service.clients.infrastructure.restadapter.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sa.client_service.clients.application.dtos.CreateClientDTO;
import com.sa.client_service.clients.application.dtos.PaymentDTO;
import com.sa.client_service.clients.application.inputports.CreateClientInputPort;
import com.sa.client_service.clients.application.inputports.FindClientByCuiInputPort;
import com.sa.client_service.clients.application.inputports.GetIncomeByClientInputPort;
import com.sa.client_service.clients.domain.Client;
import com.sa.client_service.clients.infrastructure.restadapter.dtos.ClientResponse;
import com.sa.client_service.clients.infrastructure.restadapter.dtos.CreateClientRequest;
import com.sa.client_service.clients.infrastructure.restadapter.dtos.PaymentResponse;
import com.sa.client_service.clients.infrastructure.restadapter.mappers.ClientRestMapper;
import com.sa.client_service.clients.infrastructure.restadapter.mappers.PaymentRestMapper;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.InvalidParameterException;
import com.sa.shared.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/clients")

@RequiredArgsConstructor
public class ClientController {

    private final CreateClientInputPort createClientInputPort;
    private final FindClientByCuiInputPort findClientByCuiInputPort;
    private final ClientRestMapper clientRestMapper;
    private final PaymentRestMapper paymentRestMapper;
    private final GetIncomeByClientInputPort getIncomeByClientInputPort;

    @Operation(summary = "Crear un nuevo cliente", description = "Este endpoint permite la creación de un nuevo cliente en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_CLIENT')")
    public ResponseEntity<ClientResponse> createClient(
            @RequestBody CreateClientRequest request)
            throws DuplicatedEntryException {

        CreateClientDTO createHotelDTO = clientRestMapper.toDTO(request);

        Client result = createClientInputPort.handle(createHotelDTO);

        ClientResponse response = clientRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/by-cui/{cui}")
    public ResponseEntity<ClientResponse> getClientByCui(
            @PathVariable("cui") String cui)
            throws NotFoundException {

        Client result = findClientByCuiInputPort.handle(cui);

        ClientResponse response = clientRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener el reporte de ingresos por cliente", description = "Este endpoint permite la creación de un nuevo restaurante en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/reports/income/{cui}")
    public ResponseEntity<List<PaymentResponse>> getIncome(
            @PathVariable("cui") String clientCui,
            @RequestParam(value = "establishmentId", required = false) String establishmentId,
            @RequestParam(value = "fromDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
            )
            throws NotFoundException, InvalidParameterException {

        List<PaymentDTO> result = getIncomeByClientInputPort.handle(clientCui, establishmentId.toString(), fromDate, toDate);

        List<PaymentResponse> response = paymentRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
