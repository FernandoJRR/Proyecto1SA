package com.sa.client_service.reviews.application.usecases;

import org.springframework.validation.annotation.Validated;

import com.sa.application.annotations.UseCase;
import com.sa.client_service.clients.application.outputports.FindClientByCuiOutputPort;
import com.sa.client_service.reviews.application.dtos.CreateReviewDTO;
import com.sa.client_service.reviews.application.inputports.CreateReviewInputPort;
import com.sa.client_service.reviews.application.outputports.CreateReviewOutputPort;
import com.sa.client_service.reviews.application.outputports.ExistsRoomByIdOutputPort;
import com.sa.client_service.reviews.domain.Review;
import com.sa.client_service.shared.application.outputports.ExistsRoomInHotelByIdOutputPort;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
@Validated
public class CreateReviewUseCase implements CreateReviewInputPort {

    private final CreateReviewOutputPort createReviewOutputPort;

    private final ExistsRoomInHotelByIdOutputPort existsRoomInHotelByIdOutputPort;
    private final FindClientByCuiOutputPort findClientByCuiOutputPort;

    @Override
    public Review handle(@Valid CreateReviewDTO createReviewDTO) throws NotFoundException {
        if (!existsRoomInHotelByIdOutputPort.existsById(createReviewDTO.getHotelId(), createReviewDTO.getRoomId())) {
            throw new NotFoundException("La habitacion buscada no existe o no pertenece al hotel ingresado");
        }

        if (findClientByCuiOutputPort.findByCui(createReviewDTO.getClientCui()).isEmpty()) {
            throw new NotFoundException("El cliente ingresado no existe");
        }

        Review createdReview = Review.create(createReviewDTO.getHotelId(), createReviewDTO.getRoomId(), createReviewDTO.getRating(), createReviewDTO.getComment());

        return createReviewOutputPort.createReview(createReviewDTO.getClientCui(), createdReview);
    }

}
