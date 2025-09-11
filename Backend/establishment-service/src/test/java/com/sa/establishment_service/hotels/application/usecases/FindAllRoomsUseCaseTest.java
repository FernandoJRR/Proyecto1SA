package com.sa.establishment_service.hotels.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.hotels.application.dtos.RoomWithHotelDTO;
import com.sa.establishment_service.hotels.application.outputports.FindAllRoomsOutputPort;

class FindAllRoomsUseCaseTest {

    private static class FakeFindAllRoomsOutputPort implements FindAllRoomsOutputPort {
        List<RoomWithHotelDTO> toReturn = List.of();
        @Override
        public List<RoomWithHotelDTO> findAll() { return toReturn; }
    }

    private FakeFindAllRoomsOutputPort findAllRoomsOutputPort;
    private FindAllRoomsUseCase useCase;

    @BeforeEach
    void setUp() {
        findAllRoomsOutputPort = new FakeFindAllRoomsOutputPort();
        useCase = new FindAllRoomsUseCase(findAllRoomsOutputPort);
    }

    @Test
    void handle_returnsListFromOutputPort() {
        RoomWithHotelDTO dto1 = new RoomWithHotelDTO();
        dto1.setNumber("101");
        RoomWithHotelDTO dto2 = new RoomWithHotelDTO();
        dto2.setNumber("102");
        List<RoomWithHotelDTO> list = List.of(dto1, dto2);
        findAllRoomsOutputPort.toReturn = list;

        List<RoomWithHotelDTO> result = useCase.handle();

        assertEquals(list, result);
        assertEquals(2, result.size());
    }
}
