package com.sa.establishment_service.hotels.application.usecases;

import java.util.List;
import java.util.UUID;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.hotels.application.inputports.FindAllRoomsByHotelIdInputPort;
import com.sa.establishment_service.hotels.application.outputports.FindAllRoomdByHotelIdOutputPort;
import com.sa.establishment_service.hotels.application.outputports.FindHotelByIdOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.hotels.domain.Room;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindAllRoomsByHotelId implements FindAllRoomsByHotelIdInputPort {

    private final FindAllRoomdByHotelIdOutputPort findAllRoomdByHotelIdOutputPort;
    private final FindHotelByIdOutputPort findHotelByIdOutputPort;

    @Override
    public List<Room> handle(String hotelId) throws NotFoundException {
        if (findHotelByIdOutputPort.findHotelById(UUID.fromString(hotelId)).isEmpty()) {
            throw new NotFoundException("El hotel buscado no existe");
        }

        return findAllRoomdByHotelIdOutputPort.findByHotelId(UUID.fromString(hotelId));
    }


}
