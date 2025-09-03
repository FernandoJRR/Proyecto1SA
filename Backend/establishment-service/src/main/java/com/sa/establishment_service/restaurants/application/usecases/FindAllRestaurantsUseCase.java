package com.sa.establishment_service.restaurants.application.usecases;

import java.util.List;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.restaurants.application.inputports.FindAllRestaurantsInputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindAllRestaurantsOutputPort;
import com.sa.establishment_service.restaurants.domain.Restaurant;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindAllRestaurantsUseCase implements FindAllRestaurantsInputPort {

    private final FindAllRestaurantsOutputPort findAllRestaurantsOutputPort;

    @Override
    public List<Restaurant> handle() {
        return findAllRestaurantsOutputPort.findAll();
    }

}
