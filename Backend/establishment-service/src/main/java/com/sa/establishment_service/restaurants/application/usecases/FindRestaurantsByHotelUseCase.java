package com.sa.establishment_service.restaurants.application.usecases;

import java.util.List;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.hotels.application.outputports.ExistsHotelByIdOutputPort;
import com.sa.establishment_service.restaurants.application.inputports.FindRestaurantsByHotelInputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByHotelOutputPort;
import com.sa.establishment_service.restaurants.domain.Restaurant;

import com.sa.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindRestaurantsByHotelUseCase implements FindRestaurantsByHotelInputPort {

    private final ExistsHotelByIdOutputPort existsHotelByIdOutputPort;
    private final FindRestaurantByHotelOutputPort findRestaurantByHotelOutputPort;

    @Override
    public List<Restaurant> handle(String hotelId) throws NotFoundException {
        if (!existsHotelByIdOutputPort.existsById(hotelId)) {
            throw new NotFoundException("El hotel ingresado no existe");
        }

        return findRestaurantByHotelOutputPort.findByHotel(hotelId);
    }

}
