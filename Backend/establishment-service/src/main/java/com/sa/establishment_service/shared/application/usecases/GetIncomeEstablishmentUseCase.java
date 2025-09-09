package com.sa.establishment_service.shared.application.usecases;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.hotels.application.outputports.ExistsHotelByIdOutputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByHotelOutputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByIdOutputPort;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.establishment_service.shared.application.dtos.PaymentDTO;
import com.sa.establishment_service.shared.application.inputports.GetIncomeEstablishmentInputPort;
import com.sa.establishment_service.shared.application.outputports.FindPaymentsByEstablishmentOutputPort;
import com.sa.shared.exceptions.InvalidParameterException;

import com.sa.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetIncomeEstablishmentUseCase implements GetIncomeEstablishmentInputPort {

    private final FindPaymentsByEstablishmentOutputPort findPaymentsByEstablishmentOutputPort;
    private final FindRestaurantByIdOutputPort findRestaurantByIdOutputPort;
    private final ExistsHotelByIdOutputPort existsHotelByIdOutputPort;
    private final FindRestaurantByHotelOutputPort findRestaurantByHotelOutputPort;

    @Override
    public List<PaymentDTO> handle(String establishmentId, String establishmentType, LocalDate fromDate, LocalDate toDate) throws InvalidParameterException, NotFoundException {
        if (!establishmentType.equals("HOTEL") && !establishmentType.equals("RESTAURANT")) {
            throw new InvalidParameterException("El tipo de establecimiento ingresado no existe");
        }

        if (establishmentType.equals("RESTAURANT")) {
            findRestaurantByIdOutputPort.findById(establishmentId)
                .orElseThrow(() -> new NotFoundException("El restaurante ingresado no existe"));

            return findPaymentsByEstablishmentOutputPort.findByEstablishment(establishmentId, fromDate, toDate)
                .stream()
                .peek(p -> p.setDescription("Consumo"))
                .toList();
        } else {
            if (!existsHotelByIdOutputPort.existsById(establishmentId)) {
                throw new NotFoundException("El hotel ingresado no existe");
            }

            List<PaymentDTO> paymentsHotel = findPaymentsByEstablishmentOutputPort.findByEstablishment(establishmentId, fromDate, toDate)
                .stream()
                .peek(p -> p.setDescription("Alojamiento"))
                .collect(Collectors.toList());

            List<Restaurant> restaurants = findRestaurantByHotelOutputPort.findByHotel(establishmentId);

            for (Restaurant restaurant : restaurants) {
                List<PaymentDTO> resPayments = findPaymentsByEstablishmentOutputPort.findByEstablishment(restaurant.getId().toString(), fromDate, toDate)
                    .stream()
                    .peek(p -> p.setDescription("Consumo - "+restaurant.getName()))
                    .toList();

                paymentsHotel.addAll(resPayments);
            }

            return paymentsHotel;
        }
    }

}
