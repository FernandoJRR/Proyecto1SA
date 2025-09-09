package com.sa.client_service.orders.application.usecases;

import java.util.List;

import com.sa.application.annotations.UseCase;
import com.sa.client_service.orders.application.dtos.MostPopularRestaurantDTO;
import com.sa.client_service.orders.application.dtos.RestaurantDTO;
import com.sa.client_service.orders.application.inputports.MostPopularRestaurantInputPort;
import com.sa.client_service.orders.application.outputports.FindOrdersByRestaurantOutputPort;
import com.sa.client_service.orders.application.outputports.FindRestaurantByIdOutputPort;
import com.sa.client_service.orders.application.outputports.MostPopularRestaurantOutputPort;
import com.sa.client_service.orders.domain.Order;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class MostPopularRestaurantUseCase implements MostPopularRestaurantInputPort {

    private final MostPopularRestaurantOutputPort mostPopularRestaurantOutputPort;
    private final FindOrdersByRestaurantOutputPort findOrdersByRestaurantOutputPort;
    private final FindRestaurantByIdOutputPort findRestaurantByIdOutputPort;

    @Override
    public MostPopularRestaurantDTO handle() throws NotFoundException {
        Order mostPopular = mostPopularRestaurantOutputPort.getMostPopular()
            .orElseThrow(() -> new NotFoundException("No se encontro ningun restaurante"));

        RestaurantDTO restaurant = findRestaurantByIdOutputPort.findById(mostPopular.getRestaurantId().toString())
            .orElseThrow(() -> new NotFoundException("No se encontro ningun restaurante"));

        List<Order> orders =findOrdersByRestaurantOutputPort.findByRestaurant(mostPopular.getRestaurantId().toString());

        return new MostPopularRestaurantDTO(restaurant.getName(), orders);
    }

}
