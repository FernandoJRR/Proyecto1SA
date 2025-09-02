package com.sa.client_service.reservations.application.usecases;

import com.sa.shared.exceptions.InvalidParameterException;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

import com.sa.application.annotations.UseCase;
import com.sa.client_service.clients.application.outputports.FindClientByCuiOutputPort;
import com.sa.client_service.clients.domain.Client;
import com.sa.client_service.reservations.application.dtos.CreateReservationDTO;
import com.sa.client_service.reservations.application.dtos.PromotionDTO;
import com.sa.client_service.reservations.application.dtos.RoomDTO;
import com.sa.client_service.reservations.application.inputports.CreateReservationInputPort;
import com.sa.client_service.reservations.application.outputports.CheckRoomAvailabilityOutputPort;
import com.sa.client_service.reservations.application.outputports.CreateReservationOutputPort;
import com.sa.client_service.reservations.application.outputports.FindPromotionByIdOutputPort;
import com.sa.client_service.reservations.application.outputports.FindRoomByHotelAndIdOutputPort;
import com.sa.client_service.reservations.domain.Reservation;
import com.sa.client_service.reviews.application.outputports.ExistsRoomByIdOutputPort;
import com.sa.client_service.shared.application.dtos.CreatePaymentDTO;
import com.sa.client_service.shared.application.outputports.CreatePaymentOutputPort;
import com.sa.client_service.shared.application.outputports.ExistsRoomInHotelByIdOutputPort;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreateReservationUseCase implements CreateReservationInputPort {

    private final FindClientByCuiOutputPort findClientByCuiOutputPort;
    private final FindRoomByHotelAndIdOutputPort findRoomByHotelAndIdOutputPort;
    private final FindPromotionByIdOutputPort findPromotionByIdOutputPort;
    private final CreateReservationOutputPort createReservationOutputPort;
    private final CheckRoomAvailabilityOutputPort checkRoomAvailabilityOutputPort;
    private final CreatePaymentOutputPort createPaymentOutputPort;

    @Override
    public Reservation handle(@Valid CreateReservationDTO createReservationDTO)
            throws NotFoundException, InvalidParameterException {

        Client foundClient = findClientByCuiOutputPort.findByCui(createReservationDTO.getClientCui())
                .orElseThrow(() -> new NotFoundException("El cliente buscado no existe"));

        RoomDTO foundRoom = findRoomByHotelAndIdOutputPort.findByHotelAndId(
                createReservationDTO.getHotelId().toString(), createReservationDTO.getRoomId().toString())
                .orElseThrow(() -> new NotFoundException(
                        "La habitacion buscada no existe o no pertenece al hotel ingresado"));

        if (createReservationDTO.getStartDate().isAfter(createReservationDTO.getEndDate())) {
            throw new InvalidParameterException("La fecha de entrada es despues de la fecha de salida");
        }

        if (!checkRoomAvailabilityOutputPort.isAvailable(createReservationDTO.getHotelId(),
                createReservationDTO.getRoomId(), createReservationDTO.getStartDate(),
                createReservationDTO.getEndDate())) {
            throw new InvalidParameterException("La habitación no está disponible en ese rango de fechas");
        }

        long daysReservation = ChronoUnit.DAYS.between(createReservationDTO.getStartDate(),
                createReservationDTO.getEndDate());

        Reservation createdReservation = Reservation.create(foundClient,
                createReservationDTO.getHotelId(),
                createReservationDTO.getRoomId(), createReservationDTO.getStartDate(),
                createReservationDTO.getEndDate(), foundRoom.getPricePerNight(), (int) daysReservation);

        String promotionIdPayment = null;
        if (createReservationDTO.getPromotionId() != null) {
            PromotionDTO promotion = findPromotionByIdOutputPort
                    .findPromotionById(createReservationDTO.getPromotionId().toString())
                    .orElseThrow(() -> new NotFoundException("La promocion ingresada no existe."));

            createdReservation.applyPromotion(promotion.getId().toString(), promotion.getPromotionType().getName(),
                    promotion.getPercentage());

            promotionIdPayment = createReservationDTO.getPromotionId().toString();
        }

        boolean resultPayment = createPaymentOutputPort.createPayment(new CreatePaymentDTO(createReservationDTO.getHotelId(), foundClient.getId(), "RESERVATION",
                createdReservation.getId(), "CARD", createdReservation.getSubtotal(), "1234", promotionIdPayment));

        if (!resultPayment) {
            throw new InvalidParameterException("Error creando el pago");
        }

        return createReservationOutputPort.createReservation(createdReservation);
    }

}
