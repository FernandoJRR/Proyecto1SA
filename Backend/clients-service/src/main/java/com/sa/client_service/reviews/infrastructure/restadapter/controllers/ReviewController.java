package com.sa.client_service.reviews.infrastructure.restadapter.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sa.client_service.reviews.application.dtos.CreateReviewDTO;
import com.sa.client_service.reviews.application.inputports.CreateReviewInputPort;
import com.sa.client_service.reviews.domain.Review;
import com.sa.client_service.reviews.infrastructure.restadapter.dtos.CreateReviewRequest;
import com.sa.client_service.reviews.infrastructure.restadapter.dtos.ReviewResponse;
import com.sa.client_service.reviews.infrastructure.restadapter.mapper.ReviewRestMapper;
import com.sa.shared.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reviews")

@RequiredArgsConstructor
public class ReviewController {

    private final CreateReviewInputPort createReviewInputPort;
    private final ReviewRestMapper reviewRestMapper;

    @Operation(summary = "Crear una nueva review", description = "Este endpoint permite la creación de una nueva review en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_REVIEW')")
    public ResponseEntity<ReviewResponse> createClient(
            @RequestBody CreateReviewRequest request)
            throws NotFoundException {

        CreateReviewDTO createReviewDTO = reviewRestMapper.toDTO(request);

        Review result = createReviewInputPort.handle(createReviewDTO);

        ReviewResponse response = reviewRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response);
    }
}
