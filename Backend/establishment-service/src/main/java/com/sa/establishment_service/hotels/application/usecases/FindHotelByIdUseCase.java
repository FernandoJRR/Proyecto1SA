package com.sa.establishment_service.hotels.application.usecases;

import java.util.UUID;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.hotels.application.inputports.FindHotelByIdInputPort;
import com.sa.establishment_service.hotels.application.outputports.FindHotelByIdOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindHotelByIdUseCase implements FindHotelByIdInputPort {

    private final FindHotelByIdOutputPort findHotelByIdOutputPort;

    @Override
    public Hotel handle(String id) throws NotFoundException {
        return findHotelByIdOutputPort.findHotelById(UUID.fromString(id))
            .orElseThrow(() -> new NotFoundException("El hotel buscado no existe"));
    }

}
