package com.sa.establishment_service.hotels.application.usecases;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.hotels.application.dtos.UpdateRoomDTO;
import com.sa.establishment_service.hotels.application.inputports.UpdateRoomInputPort;
import com.sa.establishment_service.hotels.application.outputports.FindHotelByIdOutputPort;
import com.sa.establishment_service.hotels.application.outputports.FindRoomByHotelAndIdOutputPort;
import com.sa.establishment_service.hotels.application.outputports.UpdateRoomOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.hotels.domain.Room;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@UseCase
@Validated
@RequiredArgsConstructor
public class UpdateRoomUseCase implements UpdateRoomInputPort {

    private final FindHotelByIdOutputPort findHotelByIdOutputPort;
    private final FindRoomByHotelAndIdOutputPort findRoomByHotelAndIdOutputPort;
    private final UpdateRoomOutputPort updateRoomOutputPort;

    @Override
    @Transactional
    public Room handle(String hotelId, String roomId, @Valid UpdateRoomDTO updateRoomDTO) throws NotFoundException, DuplicatedEntryException {
        Hotel foundHotel = findHotelByIdOutputPort.findHotelById(java.util.UUID.fromString(hotelId))
            .orElseThrow(() -> new NotFoundException("El hotel buscado no existe"));

        Room existing = findRoomByHotelAndIdOutputPort.findByHotelAndId(hotelId, roomId)
            .orElseThrow(() -> new NotFoundException("La habitacion buscada no existe o no esta en el hotel indicado."));

        List<Room> rooms = foundHotel.getRooms();
        if (rooms.stream().anyMatch(room -> room.getNumber().equals(updateRoomDTO.getNumber()))) {
            throw new DuplicatedEntryException("El numero de habitacion ya existe");
        }

        existing.setNumber(updateRoomDTO.getNumber());
        existing.setPricePerNight(updateRoomDTO.getPricePerNight());
        existing.setCapacity(updateRoomDTO.getCapacity());

        return updateRoomOutputPort.updateRoom(existing);
    }
}
