package com.sa.establishment_service.shared.infrastructure.restadapter.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sa.establishment_service.shared.application.dtos.OutcomeDTO;
import com.sa.establishment_service.shared.application.dtos.PaymentDTO;
import com.sa.establishment_service.shared.application.inputports.GetIncomeEstablishmentInputPort;
import com.sa.establishment_service.shared.application.inputports.GetOutcomeInputPort;
import com.sa.establishment_service.shared.infrastructure.restadapter.dtos.OutcomeResponse;
import com.sa.establishment_service.shared.infrastructure.restadapter.dtos.PaymentResponse;
import com.sa.establishment_service.shared.infrastructure.restadapter.mappers.OutcomeRestMapper;
import com.sa.establishment_service.shared.infrastructure.restadapter.mappers.PaymentMapper;
import com.sa.shared.exceptions.InvalidParameterException;
import com.sa.shared.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/establishments")
@RequiredArgsConstructor
public class EstablishmentController {

    private final GetIncomeEstablishmentInputPort getIncomeEstablishmentInputPort;
    private final GetOutcomeInputPort getOutcomeInputPort;
    private final PaymentMapper paymentMapper;
    private final OutcomeRestMapper outcomeRestMapper;

    @Operation(summary = "Obtener el reporte de ingresos por establecimiento", description = "Este endpoint permite la creación de un nuevo restaurante en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/reports/income/{establishmentType}/{establishmentId}")
    public ResponseEntity<List<PaymentResponse>> getIncome(
            @PathVariable("establishmentType") String establishmentType,
            @PathVariable("establishmentId") UUID establishmentId,
            @RequestParam(value = "fromDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
            )
            throws NotFoundException, InvalidParameterException {

        List<PaymentDTO> result = getIncomeEstablishmentInputPort.handle(establishmentId.toString(), establishmentType, fromDate, toDate);

        List<PaymentResponse> response = paymentMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener el reporte de egresos totales", description = "Este endpoint permite la creación de un nuevo restaurante en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/reports/outcome")
    public ResponseEntity<OutcomeResponse> getOutcome(
            @RequestParam(value = "fromDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
            )
            throws NotFoundException, InvalidParameterException {

        OutcomeDTO result = getOutcomeInputPort.handle(fromDate, toDate);

        OutcomeResponse response = outcomeRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}