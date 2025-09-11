package com.sa.finances_service.promotions.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.finances_service.promotions.application.dtos.FindReservationEligibilityDTO;
import com.sa.finances_service.promotions.application.outputports.FindPromotionsByDateAndHotelOutputPort;
import com.sa.finances_service.promotions.application.outputports.MostFrequentClientsOutputPort;
import com.sa.finances_service.promotions.domain.EstablishmentTypeEnum;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.domain.PromotionType;
import com.sa.finances_service.promotions.infrastructure.clientsadapter.adapters.MostPopularRoomsAdapter;
import com.sa.shared.exceptions.NotFoundException;

class FindEligiblePromotionReservationUseCaseTest {

    private static class FakeFindPromotionsByDateAndHotelOutputPort implements FindPromotionsByDateAndHotelOutputPort {
        List<Promotion> toReturn = List.of();
        @Override
        public List<Promotion> findByStartDateAndHotel(LocalDate startDate, String hotelId) { return toReturn; }
    }

    private static class FakeMostFrequentClientsOutputPort implements MostFrequentClientsOutputPort {
        List<UUID> toReturn = List.of();
        @Override
        public List<UUID> findMostFrequent(Integer limit) { return toReturn; }
    }

    private static class FakeMostPopularRoomsAdapter extends MostPopularRoomsAdapter {
        List<UUID> toReturn = List.of();
        public FakeMostPopularRoomsAdapter() { super(null); }
        @Override
        public List<UUID> findMostPopular(String hotelId, Integer limit) { return toReturn; }
    }

    private FakeFindPromotionsByDateAndHotelOutputPort findPromotionsByDateAndHotelOutputPort;
    private FakeMostFrequentClientsOutputPort mostFrequentClientsOutputPort;
    private FakeMostPopularRoomsAdapter mostPopularRoomsAdapter;
    private FindEligiblePromotionReservationUseCase useCase;

    @BeforeEach
    void setUp() {
        findPromotionsByDateAndHotelOutputPort = new FakeFindPromotionsByDateAndHotelOutputPort();
        mostFrequentClientsOutputPort = new FakeMostFrequentClientsOutputPort();
        mostPopularRoomsAdapter = new FakeMostPopularRoomsAdapter();
        useCase = new FindEligiblePromotionReservationUseCase(
            findPromotionsByDateAndHotelOutputPort,
            mostFrequentClientsOutputPort,
            mostPopularRoomsAdapter
        );
    }

    @Test
    void handle_throwsNotFound_whenNoPromosFound() {
        findPromotionsByDateAndHotelOutputPort.toReturn = List.of();
        FindReservationEligibilityDTO dto = new FindReservationEligibilityDTO("c1", "h1", "r1", LocalDate.now(), LocalDate.now().plusDays(1));
        assertThrows(NotFoundException.class, () -> useCase.handle(dto));
    }

    @Test
    void handle_selectsHighestPercentageAmongEligible_frequentClient() throws Exception {
        UUID clientId = UUID.randomUUID();
        Promotion p1 = Promotion.create(new BigDecimal("5"), LocalDate.now(), LocalDate.now().plusDays(1), "P1", UUID.randomUUID(), EstablishmentTypeEnum.HOTEL, 3, PromotionType.CLIENT_MOST_FREQUENT);
        Promotion p2 = Promotion.create(new BigDecimal("12"), LocalDate.now(), LocalDate.now().plusDays(1), "P2", UUID.randomUUID(), EstablishmentTypeEnum.HOTEL, 3, PromotionType.CLIENT_MOST_FREQUENT);
        findPromotionsByDateAndHotelOutputPort.toReturn = List.of(p1, p2);
        mostFrequentClientsOutputPort.toReturn = List.of(clientId);

        FindReservationEligibilityDTO dto = new FindReservationEligibilityDTO(clientId.toString(), "h1", "r1", LocalDate.now(), LocalDate.now().plusDays(1));
        Promotion result = useCase.handle(dto);

        assertEquals(p2, result);
    }

    @Test
    void handle_roomMostPopularEligible_whenRoomIsPopular() throws Exception {
        UUID roomId = UUID.randomUUID();
        Promotion pRoom = Promotion.create(new BigDecimal("8"), LocalDate.now(), LocalDate.now().plusDays(1), "PR", UUID.randomUUID(), EstablishmentTypeEnum.HOTEL, 5, PromotionType.ROOM_MOST_POPULAR);
        findPromotionsByDateAndHotelOutputPort.toReturn = List.of(pRoom);
        mostPopularRoomsAdapter.toReturn = List.of(roomId);

        FindReservationEligibilityDTO dto = new FindReservationEligibilityDTO(UUID.randomUUID().toString(), "h1", roomId.toString(), LocalDate.now(), LocalDate.now().plusDays(1));
        Promotion result = useCase.handle(dto);

        assertEquals(pRoom, result);
    }

    @Test
    void handle_throwsNotFound_whenNoneEligible() {
        Promotion pRoom = Promotion.create(new BigDecimal("8"), LocalDate.now(), LocalDate.now().plusDays(1), "PR", UUID.randomUUID(), EstablishmentTypeEnum.HOTEL, 5, PromotionType.ROOM_MOST_POPULAR);
        findPromotionsByDateAndHotelOutputPort.toReturn = List.of(pRoom);
        mostPopularRoomsAdapter.toReturn = List.of(UUID.randomUUID());

        FindReservationEligibilityDTO dto = new FindReservationEligibilityDTO(UUID.randomUUID().toString(), "h1", UUID.randomUUID().toString(), LocalDate.now(), LocalDate.now().plusDays(1));
        assertThrows(NotFoundException.class, () -> useCase.handle(dto));
    }
}

