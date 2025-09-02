package com.sa.establishment_service.hotels.application.usecases;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.validation.annotation.Validated;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.hotels.application.dtos.CreateRoomDTO;
import com.sa.establishment_service.hotels.application.inputports.CreateRoomInputPort;
import com.sa.establishment_service.hotels.application.outputports.CreateRoomOutputPort;
import com.sa.establishment_service.hotels.application.outputports.FindHotelByIdOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.hotels.domain.Room;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
@Validated
public class CreateRoomUseCase implements CreateRoomInputPort {

    private final CreateRoomOutputPort createRoomOutputPort;
    private final FindHotelByIdOutputPort findHotelByIdOutputPort;

    @Override
    public Room handle(@Valid CreateRoomDTO createRoomDTO) throws NotFoundException, DuplicatedEntryException {
        Hotel foundHotel = findHotelByIdOutputPort.findHotelById(createRoomDTO.getHotelId())
            .orElseThrow(() -> new NotFoundException("Hotel no encontrado"));

        List<Room> rooms = foundHotel.getRooms();
        if (rooms.stream().anyMatch(room -> room.getNumber().equals(createRoomDTO.getNumber()))) {
            throw new DuplicatedEntryException("El numero de habitacion ya existe");
        }

        Room createdRoom = Room.create(createRoomDTO.getNumber(), createRoomDTO.getPricePerNight(), createRoomDTO.getCapacity());
        return createRoomOutputPort.createRoom(createRoomDTO.getHotelId().toString(), createdRoom);
    }
}
