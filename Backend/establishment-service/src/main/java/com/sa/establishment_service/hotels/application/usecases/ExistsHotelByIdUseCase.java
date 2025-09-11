package com.sa.establishment_service.hotels.application.usecases;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.hotels.application.inputports.ExistsHotelByIdInputPort;
import com.sa.establishment_service.hotels.application.outputports.ExistsHotelByIdOutputPort;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ExistsHotelByIdUseCase implements ExistsHotelByIdInputPort {

    private final ExistsHotelByIdOutputPort existsHotelByIdOutputPort;

    @Override
    public void handle(String id) throws NotFoundException {
        if (!existsHotelByIdOutputPort.existsById(id)) {
            throw new NotFoundException("El hotel buscado no existe");
        }
    }

}
