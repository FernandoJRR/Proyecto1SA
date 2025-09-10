package com.sa.establishment_service.hotels.application.usecases;

import java.util.List;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.hotels.application.dtos.RoomWithHotelDTO;
import com.sa.establishment_service.hotels.application.inputports.FindAllRoomsInputPort;
import com.sa.establishment_service.hotels.application.outputports.FindAllRoomsOutputPort;
import com.sa.establishment_service.hotels.domain.Room;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindAllRoomsUseCase implements FindAllRoomsInputPort {

    private final FindAllRoomsOutputPort findAllRoomsOutputPort;

    @Override
    public List<RoomWithHotelDTO> handle() {
        return findAllRoomsOutputPort.findAll();
    }

}
