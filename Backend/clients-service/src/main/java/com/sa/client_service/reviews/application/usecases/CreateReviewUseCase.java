package com.sa.client_service.reviews.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import com.sa.application.annotations.UseCase;
import com.sa.client_service.clients.application.outputports.FindClientByCuiOutputPort;
import com.sa.client_service.clients.domain.Client;
import com.sa.client_service.orders.application.outputports.ExistDishesByRestaurantOutputPort;
import com.sa.client_service.reviews.application.dtos.CreateReviewDTO;
import com.sa.client_service.reviews.application.dtos.FindReviewsDTO;
import com.sa.client_service.reviews.application.inputports.CreateReviewInputPort;
import com.sa.client_service.reviews.application.outputports.CreateReviewOutputPort;
import com.sa.client_service.reviews.application.outputports.ExistsRoomByIdOutputPort;
import com.sa.client_service.reviews.application.outputports.FindReviewsOutputPort;
import com.sa.client_service.reviews.domain.EstablishmentType;
import com.sa.client_service.reviews.domain.Review;
import com.sa.client_service.shared.application.outputports.ExistsRoomInHotelByIdOutputPort;
import com.sa.shared.exceptions.InvalidParameterException;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
@Validated
public class CreateReviewUseCase implements CreateReviewInputPort {

    private final CreateReviewOutputPort createReviewOutputPort;

    private final ExistsRoomInHotelByIdOutputPort existsRoomInHotelByIdOutputPort;
    private final ExistDishesByRestaurantOutputPort existDishesByRestaurantOutputPort;
    private final FindClientByCuiOutputPort findClientByCuiOutputPort;
    private final FindReviewsOutputPort findReviewsOutputPort;

    @Override
    public Review handle(@Valid CreateReviewDTO createReviewDTO) throws NotFoundException, InvalidParameterException {
        if (!createReviewDTO.getEstablishmentType().equals(EstablishmentType.HOTEL.toString())
                && !createReviewDTO.getEstablishmentType().equals(EstablishmentType.RESTAURANT.toString())) {
            throw new InvalidParameterException("El tipo de establecimiento ingresado no es valido");
        }

        EstablishmentType establishmentType = EstablishmentType.valueOf(createReviewDTO.getEstablishmentType());

        Client foundClient = findClientByCuiOutputPort.findByCui(createReviewDTO.getClientCui())
                .orElseThrow(() -> new NotFoundException("El cliente ingresado no existe"));

        List<Review> foundReviews = findReviewsOutputPort.findReviews(FindReviewsDTO.builder()
                .sourceId(createReviewDTO.getSourceId())
                .clientId(foundClient.getId()).build());

        if (establishmentType.equals(EstablishmentType.HOTEL)) {
            if (!existsRoomInHotelByIdOutputPort.existsById(createReviewDTO.getEstablishmentId(),
                    createReviewDTO.getSourceId())) {
                throw new NotFoundException("La habitacion buscada no existe o no pertenece al hotel ingresado");
            }
            if (!foundReviews.isEmpty()) {
                throw new InvalidParameterException("No es posible ingresar dos reviews para una misma reservacion");
            }
        } else if (establishmentType.equals(EstablishmentType.RESTAURANT)) {
            if (!existDishesByRestaurantOutputPort
                    .existantDishesRestaurant(
                            createReviewDTO.getEstablishmentId(),
                            List.of(UUID.fromString(createReviewDTO.getSourceId())))
                    .isAllPresent()) {
                throw new NotFoundException("El platillo buscado no existe o no pertenece al restaurante ingresado");
            }
            if (!foundReviews.isEmpty()) {
                throw new InvalidParameterException("No es posible ingresar dos reviews para una misma orden");
            }
        }


        Review createdReview = Review.create(createReviewDTO.getEstablishmentId(),
                establishmentType,
                createReviewDTO.getSourceId(),
                createReviewDTO.getRating(),
                createReviewDTO.getComment());

        return createReviewOutputPort.createReview(createReviewDTO.getClientCui(), createdReview);
    }

}
