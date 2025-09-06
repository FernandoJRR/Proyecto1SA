package com.sa.client_service.reservations.application.usecases;

import java.util.List;
import java.util.UUID;

import com.sa.application.annotations.UseCase;
import com.sa.client_service.reservations.application.inputports.MostPopularRoomsInputPort;
import com.sa.client_service.reservations.application.outputports.MostPopularRoomsOutputPort;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class MostPopularRoomsUseCase implements MostPopularRoomsInputPort {

    private final MostPopularRoomsOutputPort mostPopularRoomsOutputPort;

    @Override
    public List<UUID> handle(UUID hotelId) {
        return mostPopularRoomsOutputPort.mostPopularRooms(hotelId);
    }

}
