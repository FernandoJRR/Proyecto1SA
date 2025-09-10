package com.sa.finances_service.promotions.infrastructure.restadapter.controllers;

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
import org.springframework.web.reactive.function.client.ClientResponse;

import com.sa.finances_service.promotions.application.dtos.CreatePromotionDTO;
import com.sa.finances_service.promotions.application.dtos.FindOrderEligibilityDTO;
import com.sa.finances_service.promotions.application.dtos.FindReservationEligibilityDTO;
import com.sa.finances_service.promotions.application.inputports.CreatePromotionInputPort;
import com.sa.finances_service.promotions.application.inputports.FindAllPromotionTypesInputPort;
import com.sa.finances_service.promotions.application.inputports.FindEligiblePromotionOrderInputPort;
import com.sa.finances_service.promotions.application.inputports.FindEligiblePromotionReservationInputPort;
import com.sa.finances_service.promotions.application.inputports.FindPromotionByIdInputPort;
import com.sa.finances_service.promotions.application.inputports.FindPromotionsInputPort;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.domain.PromotionType;
import com.sa.finances_service.promotions.domain.PromotionType.PromotionTypeInfo;
import com.sa.finances_service.promotions.infrastructure.restadapter.dtos.CreatePromotionRequest;
import com.sa.finances_service.promotions.infrastructure.restadapter.dtos.FindOrderEligibilityRequest;
import com.sa.finances_service.promotions.infrastructure.restadapter.dtos.FindReservationEligibilityRequest;
import com.sa.finances_service.promotions.infrastructure.restadapter.dtos.PromotionResponse;
import com.sa.finances_service.promotions.infrastructure.restadapter.dtos.PromotionTypeResponse;
import com.sa.finances_service.promotions.infrastructure.restadapter.mappers.PromotionsRestMapper;
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
@RequestMapping("/api/v1/promotions")

@RequiredArgsConstructor
public class PromotionController {

    private final CreatePromotionInputPort createPromotionInputPort;
    private final FindAllPromotionTypesInputPort findAllPromotionTypesInputPort;
    private final FindPromotionByIdInputPort findPromotionByIdInputPort;
    private final FindPromotionsInputPort findPromotionsInputPort;
    private final FindEligiblePromotionReservationInputPort findEligiblePromotionReservationInputPort;
    private final FindEligiblePromotionOrderInputPort findEligiblePromotionOrderInputPort;
    private final PromotionsRestMapper promotionsRestMapper;

    @Operation(summary = "Crear una nueva promocion", description = "Este endpoint permite la creación de una nueva promocion en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Promocion creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PromotionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_PROMOTION')")
    public ResponseEntity<PromotionResponse> createClient(
            @RequestBody CreatePromotionRequest request)
            throws InvalidParameterException {

        CreatePromotionDTO createPromotionDTO = promotionsRestMapper.toDTO(request);

        Promotion result = createPromotionInputPort.handle(createPromotionDTO);

        PromotionResponse response = promotionsRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener una promocion", description = "Este endpoint permite la obtencion de una promocion usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promocion obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PromotionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{promotionId}")
    public ResponseEntity<PromotionResponse> getPromotionById(
        @PathVariable("promotionId") UUID promotionId
    ) throws NotFoundException {

        Promotion result = findPromotionByIdInputPort.handle(promotionId);

        PromotionResponse response = promotionsRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Obtener todas las promociones", description = "Este endpoint permite la obtencion de todas las promociones en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promociones obtenidas exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PromotionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping({"","/"})
    public ResponseEntity<List<PromotionResponse>> getPromotionById() {

        List<Promotion> result = findPromotionsInputPort.handle();

        List<PromotionResponse> response = promotionsRestMapper.toResponseList(result);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Obtener tipos validos de promocion", description = "Este endpoint permite la obtencion de los tipos de promocion en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipos obtenidos exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PromotionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/types")
    public ResponseEntity<List<PromotionTypeResponse>> getTypes() {

        List<PromotionTypeInfo> result = findAllPromotionTypesInputPort.handle();

        List<PromotionTypeResponse> response = promotionsRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtiene si una reservacion es elegible para alguna promocion", description = "Este endpoint permite la obtencion de una promocion para una reservacion si alguna fuera aplicable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promocion obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PromotionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Promocion aplicable no encontrada", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/eligibility/reservation")
    public ResponseEntity<PromotionResponse> getEligibilityReservation(
            @RequestBody FindReservationEligibilityRequest request)
            throws NotFoundException {

        FindReservationEligibilityDTO dto = promotionsRestMapper.toDTO(request);

        Promotion result = findEligiblePromotionReservationInputPort.handle(dto);

        PromotionResponse response = promotionsRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtiene si una reservacion es elegible para alguna promocion", description = "Este endpoint permite la obtencion de una promocion para una reservacion si alguna fuera aplicable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promocion obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PromotionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Promocion aplicable no encontrada", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/public/eligibility/reservation")
    public ResponseEntity<PromotionResponse> getEligibilityReservationPublic(
            @RequestBody FindReservationEligibilityRequest request)
            throws NotFoundException {

        FindReservationEligibilityDTO dto = promotionsRestMapper.toDTO(request);

        Promotion result = findEligiblePromotionReservationInputPort.handle(dto);

        PromotionResponse response = promotionsRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtiene si una orden es elegible para alguna promocion", description = "Este endpoint permite la obtencion de una promocion para una orden si alguna fuera aplicable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promocion obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PromotionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Promocion aplicable no encontrada", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/eligibility/order")
    public ResponseEntity<PromotionResponse> getEligibilityOrder(
            @RequestBody FindOrderEligibilityRequest request)
            throws NotFoundException {

        FindOrderEligibilityDTO dto = promotionsRestMapper.toDTO(request);

        Promotion result = findEligiblePromotionOrderInputPort.handle(dto);

        PromotionResponse response = promotionsRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
