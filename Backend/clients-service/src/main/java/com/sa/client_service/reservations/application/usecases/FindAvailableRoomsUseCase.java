package com.sa.client_service.reservations.application.usecases;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.sa.application.annotations.UseCase;
import com.sa.client_service.reservations.application.dtos.RoomDTO;
import com.sa.client_service.reservations.application.inputports.FindAvailableRoomsInputPort;
import com.sa.client_service.reservations.application.outputports.CheckRoomAvailabilityOutputPort;
import com.sa.client_service.reservations.application.outputports.FindAllRoomsOutputPort;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindAvailableRoomsUseCase implements FindAvailableRoomsInputPort {

    private final FindAllRoomsOutputPort findAllRoomsOutputPort;
    private final CheckRoomAvailabilityOutputPort checkRoomAvailabilityOutputPort;

    @Override
    public List<RoomDTO> handle(String hotelId, LocalDate startDate, LocalDate endDate) {
        List<RoomDTO> allRooms = findAllRoomsOutputPort.findAll();

        List<RoomDTO> availableRooms = allRooms.stream()
                .filter(room -> checkRoomAvailabilityOutputPort.isAvailable(UUID.fromString(room.getHotelId()),
                        UUID.fromString(room.getId()),
                        startDate, endDate))
                .toList();

        if (hotelId !=null) {
            availableRooms = allRooms.stream()
                .filter(room -> room.getHotelId().equals(hotelId))
                .toList();
        }

        return availableRooms;
    }

}
