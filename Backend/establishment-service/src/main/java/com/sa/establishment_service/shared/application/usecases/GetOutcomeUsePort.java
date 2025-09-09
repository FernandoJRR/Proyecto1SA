package com.sa.establishment_service.shared.application.usecases;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.hotels.application.outputports.FindAllHotelsOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.restaurants.application.outputports.FindAllRestaurantsOutputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindDishesByRestaurantOutputPort;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.establishment_service.shared.application.dtos.OrderDTO;
import com.sa.establishment_service.shared.application.dtos.OutcomeDTO;
import com.sa.establishment_service.shared.application.inputports.GetOutcomeInputPort;
import com.sa.establishment_service.shared.application.outputports.FindOrdersByDateOutputPort;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetOutcomeUsePort implements GetOutcomeInputPort {

    private final FindAllHotelsOutputPort findAllHotelsOutputPort;
    private final FindOrdersByDateOutputPort findOrdersByDateOutputPort;

    @Override
    public OutcomeDTO handle(LocalDate fromDate, LocalDate toDate) {
        long exactWeeks = (ChronoUnit.DAYS.between(fromDate, toDate) + 6) / 7;

        BigDecimal totalOutcomeHotelsPerWeek = BigDecimal.valueOf(0);
        BigDecimal totalOutcomeRestaurantsPerWeek = BigDecimal.valueOf(0);

        List<Hotel> hotels = findAllHotelsOutputPort.findAll();

        for (Hotel hotel : hotels) {
            totalOutcomeHotelsPerWeek = totalOutcomeHotelsPerWeek.add(hotel.getMaintenanceCostPerWeek());
        }

        List<OrderDTO> orders = findOrdersByDateOutputPort.findOrdersByDate(fromDate, toDate);

        for (OrderDTO orderDTO : orders) {
            totalOutcomeRestaurantsPerWeek = totalOutcomeRestaurantsPerWeek.add(orderDTO.getTotal().multiply(BigDecimal.valueOf(0.20)));
        }

        return new OutcomeDTO(totalOutcomeHotelsPerWeek.multiply(BigDecimal.valueOf(exactWeeks)),
            totalOutcomeRestaurantsPerWeek.multiply(BigDecimal.valueOf(exactWeeks)));
    }

}
