package com.sa.establishment_service.hotels.application.usecases;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.hotels.application.inputports.ExistsHotelByIdInputPort;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ExistsHotelByIdUseCase implements ExistsHotelByIdInputPort {

    @Override
    public void handle(String id) throws NotFoundException {

    }

}
