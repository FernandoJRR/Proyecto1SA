package com.sa.establishment_service.shared.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sa.establishment_service.hotels.application.outputports.ExistsHotelByIdOutputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByHotelOutputPort;
import com.sa.establishment_service.restaurants.application.outputports.FindRestaurantByIdOutputPort;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.establishment_service.shared.application.dtos.PaymentDTO;
import com.sa.establishment_service.shared.application.outputports.FindPaymentsByEstablishmentOutputPort;
import com.sa.shared.exceptions.InvalidParameterException;
import com.sa.shared.exceptions.NotFoundException;

class GetIncomeEstablishmentUseCaseTest {

    private static class FakeFindPaymentsByEstablishmentOutputPort implements FindPaymentsByEstablishmentOutputPort {
        Map<String, List<PaymentDTO>> map = new HashMap<>();
        List<PaymentDTO> lastReturned;
        String lastId;
        @Override
        public List<PaymentDTO> findByEstablishment(String id, LocalDate fromDate, LocalDate toDate) {
            lastId = id;
            lastReturned = map.getOrDefault(id, List.of());
            // return copies to ensure we are mutating the ones in the usecase stream
            List<PaymentDTO> copies = new ArrayList<>();
            for (PaymentDTO p : lastReturned) {
                copies.add(new PaymentDTO(p.getEstablishmentId(), p.getClientId(), p.getSourceType(), p.getSourceId(), p.getSubtotal(), p.getDiscount(), p.getTotal(), p.getPaidAt(), p.getDescription()));
            }
            return copies;
        }
    }

    private static class FakeFindRestaurantByIdOutputPort implements FindRestaurantByIdOutputPort {
        String expectedId;
        Optional<Restaurant> toReturn = Optional.empty();
        @Override
        public Optional<Restaurant> findById(String id) {
            if (expectedId == null || expectedId.equals(id)) return toReturn;
            return Optional.empty();
        }
    }

    private static class FakeExistsHotelByIdOutputPort implements ExistsHotelByIdOutputPort {
        boolean exists;
        @Override
        public boolean existsById(String id) { return exists; }
    }

    private static class FakeFindRestaurantByHotelOutputPort implements FindRestaurantByHotelOutputPort {
        String lastHotelId;
        List<Restaurant> toReturn = List.of();
        @Override
        public List<Restaurant> findByHotel(String hotelId) {
            lastHotelId = hotelId;
            return toReturn;
        }
    }

    private FakeFindPaymentsByEstablishmentOutputPort findPaymentsByEstablishmentOutputPort;
    private FakeFindRestaurantByIdOutputPort findRestaurantByIdOutputPort;
    private FakeExistsHotelByIdOutputPort existsHotelByIdOutputPort;
    private FakeFindRestaurantByHotelOutputPort findRestaurantByHotelOutputPort;
    private GetIncomeEstablishmentUseCase useCase;

    @BeforeEach
    void setUp() {
        findPaymentsByEstablishmentOutputPort = new FakeFindPaymentsByEstablishmentOutputPort();
        findRestaurantByIdOutputPort = new FakeFindRestaurantByIdOutputPort();
        existsHotelByIdOutputPort = new FakeExistsHotelByIdOutputPort();
        findRestaurantByHotelOutputPort = new FakeFindRestaurantByHotelOutputPort();
        useCase = new GetIncomeEstablishmentUseCase(
            findPaymentsByEstablishmentOutputPort,
            findRestaurantByIdOutputPort,
            existsHotelByIdOutputPort,
            findRestaurantByHotelOutputPort
        );
    }

    @Test
    void handle_throwsInvalidParameter_whenUnknownType() {
        assertThrows(InvalidParameterException.class, () ->
            useCase.handle("id", "UNKNOWN", LocalDate.now(), LocalDate.now())
        );
    }

    @Test
    void handle_restaurantPath_setsDescriptionToConsumo() throws Exception {
        String restaurantId = UUID.randomUUID().toString();
        findRestaurantByIdOutputPort.expectedId = restaurantId;
        findRestaurantByIdOutputPort.toReturn = Optional.of(new Restaurant(UUID.randomUUID(), "R1", "A", null));

        PaymentDTO p1 = new PaymentDTO(null, null, null, null, new BigDecimal("10"), BigDecimal.ZERO, new BigDecimal("10"), LocalDate.now(), null);
        PaymentDTO p2 = new PaymentDTO(null, null, null, null, new BigDecimal("20"), BigDecimal.ZERO, new BigDecimal("20"), LocalDate.now(), null);
        findPaymentsByEstablishmentOutputPort.map.put(restaurantId, List.of(p1, p2));

        List<PaymentDTO> result = useCase.handle(restaurantId, "RESTAURANT", LocalDate.now().minusDays(5), LocalDate.now());

        assertEquals(2, result.size());
        assertEquals("Consumo", result.get(0).getDescription());
        assertEquals("Consumo", result.get(1).getDescription());
    }

    @Test
    void handle_restaurantPath_throwsNotFoundWhenMissing() {
        String restaurantId = UUID.randomUUID().toString();
        findRestaurantByIdOutputPort.expectedId = restaurantId;
        findRestaurantByIdOutputPort.toReturn = Optional.empty();

        assertThrows(NotFoundException.class, () ->
            useCase.handle(restaurantId, "RESTAURANT", LocalDate.now(), LocalDate.now())
        );
    }

    @Test
    void handle_hotelPath_throwsNotFoundWhenHotelMissing() {
        existsHotelByIdOutputPort.exists = false;
        assertThrows(NotFoundException.class, () ->
            useCase.handle("h1", "HOTEL", LocalDate.now(), LocalDate.now())
        );
    }

    @Test
    void handle_hotelPath_aggregatesHotelAndRestaurantPayments_withDescriptions() throws Exception {
        String hotelId = UUID.randomUUID().toString();
        existsHotelByIdOutputPort.exists = true;

        // hotel payments
        PaymentDTO ph1 = new PaymentDTO(null, null, null, null, new BigDecimal("100"), BigDecimal.ZERO, new BigDecimal("100"), LocalDate.now(), null);
        PaymentDTO ph2 = new PaymentDTO(null, null, null, null, new BigDecimal("200"), BigDecimal.ZERO, new BigDecimal("200"), LocalDate.now(), null);
        findPaymentsByEstablishmentOutputPort.map.put(hotelId, List.of(ph1, ph2));

        // restaurants under hotel
        UUID r1Id = UUID.randomUUID();
        UUID r2Id = UUID.randomUUID();
        Restaurant r1 = new Restaurant(r1Id, "R1", "A1", null);
        Restaurant r2 = new Restaurant(r2Id, "R2", "A2", null);
        findRestaurantByHotelOutputPort.toReturn = List.of(r1, r2);

        // payments for restaurants
        PaymentDTO pr1 = new PaymentDTO(null, null, null, null, new BigDecimal("15"), BigDecimal.ZERO, new BigDecimal("15"), LocalDate.now(), null);
        findPaymentsByEstablishmentOutputPort.map.put(r1Id.toString(), List.of(pr1));
        findPaymentsByEstablishmentOutputPort.map.put(r2Id.toString(), List.of());

        List<PaymentDTO> result = useCase.handle(hotelId, "HOTEL", LocalDate.now().minusDays(10), LocalDate.now());

        // expect 3 payments total (2 hotel + 1 restaurant)
        assertEquals(3, result.size());

        long alojamiento = result.stream().filter(p -> "Alojamiento".equals(p.getDescription())).count();
        long consumoR1 = result.stream().filter(p -> ("Consumo - "+r1.getName()).equals(p.getDescription())).count();
        long consumoR2 = result.stream().filter(p -> ("Consumo - "+r2.getName()).equals(p.getDescription())).count();

        assertEquals(2, alojamiento);
        assertEquals(1, consumoR1);
        assertEquals(0, consumoR2);
    }
}

