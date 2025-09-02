package com.sa.establishment_service.hotels.application.usecases;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.hotels.application.inputports.ExistsRoomByIdInputPort;
import com.sa.establishment_service.hotels.application.outputports.ExistsRoomByIdOutputPort;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ExistsRoomByIdUseCase implements ExistsRoomByIdInputPort {

    private final ExistsRoomByIdOutputPort existsRoomByIdOutputPort;

    @Override
    public void handle(String roomId) throws NotFoundException {
        if (!existsRoomByIdOutputPort.existsById(roomId)) {
            throw new NotFoundException("La habitacion buscada no existe");
        }

    }

}
