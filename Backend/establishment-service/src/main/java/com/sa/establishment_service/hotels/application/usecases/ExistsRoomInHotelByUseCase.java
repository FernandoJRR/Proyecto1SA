package com.sa.establishment_service.hotels.application.usecases;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.hotels.application.inputports.ExistsRoomInHotelByIdInputPort;
import com.sa.establishment_service.hotels.application.outputports.ExistsRoomInHotelByIdOutputPort;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ExistsRoomInHotelByUseCase implements ExistsRoomInHotelByIdInputPort {

    private final ExistsRoomInHotelByIdOutputPort existsRoomInHotelByIdOutputPort;

    @Override
    public void handle(String hotelId, String roomId) throws NotFoundException {
        if (!existsRoomInHotelByIdOutputPort.existsRoomInHotel(hotelId, roomId)) {
            throw new NotFoundException("La habitacion ingresada no existe o no pertenece al hotel ingresado");
        }
    }

}
