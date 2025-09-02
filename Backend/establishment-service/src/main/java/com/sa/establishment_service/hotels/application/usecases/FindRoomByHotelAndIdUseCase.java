package com.sa.establishment_service.hotels.application.usecases;

import java.util.Optional;
import java.util.UUID;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.hotels.application.inputports.FindRoomByHotelAndIdInputPort;
import com.sa.establishment_service.hotels.application.outputports.FindHotelByIdOutputPort;
import com.sa.establishment_service.hotels.application.outputports.FindRoomByHotelAndIdOutputPort;
import com.sa.establishment_service.hotels.domain.Room;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindRoomByHotelAndIdUseCase implements FindRoomByHotelAndIdInputPort {

    private final FindRoomByHotelAndIdOutputPort findRoomByHotelAndIdOutputPort;
    private final FindHotelByIdOutputPort findHotelByIdOutputPort;

    @Override
    public Room handle(String hotelId, String roomId) throws NotFoundException {
        findHotelByIdOutputPort.findHotelById(UUID.fromString(hotelId))
            .orElseThrow(() -> new NotFoundException("El hotel buscado no existe"));

        return findRoomByHotelAndIdOutputPort.findByHotelAndId(hotelId, roomId)
            .orElseThrow(() -> new NotFoundException("La habitacion buscada no existe o no esta en el hotel indicado."));
    }

}
