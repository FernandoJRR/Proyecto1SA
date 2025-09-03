package com.sa.client_service.reservations.infrastructure.restadapter.controllers;

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

import com.sa.client_service.reservations.application.dtos.CreateReservationDTO;
import com.sa.client_service.reservations.application.dtos.FindReservationsDTO;
import com.sa.client_service.reservations.application.inputports.CreateReservationInputPort;
import com.sa.client_service.reservations.application.inputports.FindReservationByIdInputPort;
import com.sa.client_service.reservations.application.inputports.FindReservationsInputPort;
import com.sa.client_service.reservations.domain.Reservation;
import com.sa.client_service.reservations.infrastructure.restadapter.dtos.CreateReservationRequest;
import com.sa.client_service.reservations.infrastructure.restadapter.dtos.ReservationHydratedResponse;
import com.sa.client_service.reservations.infrastructure.restadapter.dtos.ReservationResponse;
import com.sa.client_service.reservations.infrastructure.restadapter.mappers.ReservationHydrationAssembler;
import com.sa.client_service.reservations.infrastructure.restadapter.mappers.ReservationRestMapper;
import com.sa.shared.exceptions.InvalidParameterException;
import com.sa.shared.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final CreateReservationInputPort createReservationInputPort;
    private final FindReservationsInputPort findReservationsInputPort;
    private final FindReservationByIdInputPort findReservationByIdInputPort;
    private final ReservationRestMapper reservationRestMapper;
    private final ReservationHydrationAssembler reservationHydrationAssembler;

    @Operation(summary = "Crear una nueva reservacion", description = "Este endpoint permite la creación de una nueva reservacion en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_RESERVATION')")
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody CreateReservationRequest request)
            throws NotFoundException, InvalidParameterException {

        CreateReservationDTO createReservationDTO = reservationRestMapper.toDTO(request);

        Reservation result = createReservationInputPort.handle(createReservationDTO);

        ReservationResponse response = reservationRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar reservaciones", description = "Obtiene una lista de reservaciones. Puede filtrar por hotel, habitación, cliente y rango de fechas (startDate/endDate).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ReservationHydratedResponse>> getReservations(
            @RequestParam(required = false) UUID hotelId,
            @RequestParam(required = false) UUID roomId,
            @RequestParam(required = false) String clientCui,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate)
            throws InvalidParameterException {

        FindReservationsDTO query = FindReservationsDTO.builder()
                .hotelId(hotelId)
                .roomId(roomId)
                .clientCui(clientCui)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        List<Reservation> result = findReservationsInputPort.handle(query);

        List<ReservationHydratedResponse> response = reservationHydrationAssembler.toResponseList(result);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Obtener una reservacion por ID", description = "Obtiene una reservacion del sistema por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservacion obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationHydratedResponse> getReservation(
            @PathVariable("reservationId") UUID reservationId)
            throws NotFoundException {

        Reservation result = findReservationByIdInputPort.handle(reservationId.toString());

        ReservationHydratedResponse response = reservationHydrationAssembler.toResponse(result);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
