package com.sa.client_service.reviews.infrastructure.restadapter.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sa.client_service.reviews.application.dtos.CreateReviewDTO;
import com.sa.client_service.reviews.application.dtos.FindReviewsDTO;
import com.sa.client_service.reviews.application.inputports.CreateReviewInputPort;
import com.sa.client_service.reviews.application.inputports.FindReviewsInputPort;
import com.sa.client_service.reviews.domain.Review;
import com.sa.client_service.reviews.infrastructure.restadapter.dtos.CreateReviewRequest;
import com.sa.client_service.reviews.infrastructure.restadapter.dtos.FindReviewsRequest;
import com.sa.client_service.reviews.infrastructure.restadapter.dtos.ReviewResponse;
import com.sa.client_service.reviews.infrastructure.restadapter.mapper.ReviewRestMapper;
import com.sa.shared.exceptions.InvalidParameterException;
import com.sa.shared.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reviews")

@RequiredArgsConstructor
public class ReviewController {

    private final CreateReviewInputPort createReviewInputPort;
    private final FindReviewsInputPort findReviewsInputPort;
    private final ReviewRestMapper reviewRestMapper;

    @Operation(summary = "Crear una nueva review", description = "Este endpoint permite la creación de una nueva review en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/public")
    public ResponseEntity<ReviewResponse> createClient(
            @RequestBody CreateReviewRequest request)
            throws NotFoundException, InvalidParameterException {

        CreateReviewDTO createReviewDTO = reviewRestMapper.toDTO(request);

        Review result = createReviewInputPort.handle(createReviewDTO);

        ReviewResponse response = reviewRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @Operation(summary = "Buscar reviews filtradas", description = "Obtiene reviews que coinciden con los criterios de búsqueda proporcionados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reviews encontradas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error en los parámetros de filtro"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping({ "", "/" })
    public ResponseEntity<List<ReviewResponse>> searchReviews(
            @RequestParam(required = false) String clientCui,
            @RequestParam(required = false) String establishmentId,
            @RequestParam(required = false) String establishmentType,
            @RequestParam(required = false) String sourceId) {
        FindReviewsDTO dto = FindReviewsDTO
                .builder().establishmentId(establishmentId)
                .establishmentType(establishmentType)
                .sourceId(sourceId).clientCui(clientCui).build();

        List<Review> result = findReviewsInputPort.handle(dto);

        List<ReviewResponse> response = result.stream()
                .map(reviewRestMapper::toResponse)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Buscar reviews filtradas", description = "Obtiene reviews que coinciden con los criterios de búsqueda proporcionados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reviews encontradas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "400", description = "Error en los parámetros de filtro"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping({"/public/search" })
    public ResponseEntity<List<ReviewResponse>> searchReviewsPublic(
            @RequestParam(required = false) String clientCui,
            @RequestParam(required = false) String establishmentId,
            @RequestParam(required = false) String establishmentType,
            @RequestParam(required = false) String sourceId) {
        FindReviewsDTO dto = FindReviewsDTO
                .builder().establishmentId(establishmentId)
                .establishmentType(establishmentType)
                .sourceId(sourceId).clientCui(clientCui).build();

        List<Review> result = findReviewsInputPort.handle(dto);

        List<ReviewResponse> response = result.stream()
                .map(reviewRestMapper::toResponse)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
