package com.sa.client_service.reservations.application.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.sa.client_service.clients.application.outputports.FindClientByCuiOutputPort;
import com.sa.client_service.clients.domain.Client;
import com.sa.client_service.reservations.application.dtos.CreateReservationDTO;
import com.sa.client_service.reservations.application.dtos.PromotionDTO;
import com.sa.client_service.reservations.application.dtos.RoomDTO;
import com.sa.client_service.reservations.domain.Reservation;
import com.sa.client_service.reservations.application.outputports.CheckRoomAvailabilityOutputPort;
import com.sa.client_service.reservations.application.outputports.CreateReservationOutputPort;
import com.sa.client_service.reservations.application.outputports.FindPromotionByIdOutputPort;
import com.sa.client_service.reservations.application.outputports.FindRoomByHotelAndIdOutputPort;
import com.sa.client_service.reservations.application.usecases.CreateReservationUseCase;
import com.sa.client_service.shared.application.outputports.CreatePaymentOutputPort;
import com.sa.shared.exceptions.InvalidParameterException;
import com.sa.shared.exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CreateReservationUseCaseTest {

    @Mock
    private FindClientByCuiOutputPort findClientByCuiOutputPort;

    @Mock
    private FindRoomByHotelAndIdOutputPort findRoomByHotelAndIdOutputPort;

    @Mock
    private FindPromotionByIdOutputPort findPromotionByIdOutputPort;

    @Mock
    private CreateReservationOutputPort createReservationOutputPort;

    @Mock
    private CheckRoomAvailabilityOutputPort checkRoomAvailabilityOutputPort;

    @Mock
    private CreatePaymentOutputPort createPaymentOutputPort;

    private CreateReservationUseCase useCase;

    // common test data
    private final UUID hotelId = UUID.randomUUID();
    private final UUID roomId = UUID.randomUUID();
    private final Client client = Client.register("Alice", "Smith", "alice@example.com", "CUI-1234");

    private LocalDate startDate = LocalDate.now().plusDays(1);
    private LocalDate endDate = LocalDate.now().plusDays(3);
    private BigDecimal pricePerNight = new BigDecimal("100");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CreateReservationUseCase(
            findClientByCuiOutputPort,
            findRoomByHotelAndIdOutputPort,
            findPromotionByIdOutputPort,
            createReservationOutputPort,
            checkRoomAvailabilityOutputPort,
            createPaymentOutputPort
        );
    }

    @Test
    void givenValidInputWithoutPromotion_whenHandle_thenCreatesReservation() throws Exception {
        // Arrange
        CreateReservationDTO dto = new CreateReservationDTO(
            client.getCui(),
            hotelId,
            roomId,
            startDate,
            endDate,
            null  // no promotion
        );

        when(findClientByCuiOutputPort.findByCui(dto.getClientCui())).thenReturn(Optional.of(client));
        RoomDTO room = new RoomDTO(roomId.toString(), "101", pricePerNight, 2, hotelId.toString(), "HotelName");
        when(findRoomByHotelAndIdOutputPort.findByHotelAndId(hotelId.toString(), roomId.toString()))
            .thenReturn(Optional.of(room));
        when(checkRoomAvailabilityOutputPort.isAvailable(hotelId, roomId, startDate, endDate))
            .thenReturn(true);
        // stub payment success
        when(createPaymentOutputPort.createPayment(any()))
            .thenReturn(true);
        // stub creation
        when(createReservationOutputPort.createReservation(any(Reservation.class)))
            .thenAnswer(inv -> inv.getArgument(0));

        // Act
        Reservation result = useCase.handle(dto);

        // Assert
        assertNotNull(result);
        // assert basic fields
        // E.g., totalCost equals pricePerNight * days
        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        BigDecimal expectedSubtotal = pricePerNight.multiply(BigDecimal.valueOf(days));
        // Since no promotion, totalCost == subtotal
        assert(result.getSubtotal().compareTo(expectedSubtotal) == 0);
        assert(result.getTotalCost().compareTo(expectedSubtotal) == 0);
        // verify output ports are called
        verify(findClientByCuiOutputPort).findByCui(dto.getClientCui());
        verify(findRoomByHotelAndIdOutputPort).findByHotelAndId(hotelId.toString(), roomId.toString());
        verify(checkRoomAvailabilityOutputPort).isAvailable(hotelId, roomId, startDate, endDate);
        verify(createPaymentOutputPort).createPayment(any());
        verify(createReservationOutputPort).createReservation(any(Reservation.class));
    }

    @Test
    void givenClientNotFound_whenHandle_thenThrowsNotFoundException() {
        // Arrange
        CreateReservationDTO dto = new CreateReservationDTO(
            "NonExistentCUI",
            hotelId,
            roomId,
            startDate,
            endDate,
            null
        );
        when(findClientByCuiOutputPort.findByCui(dto.getClientCui())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> useCase.handle(dto));
    }

    @Test
    void givenRoomNotFound_whenHandle_thenThrowsNotFoundException() {
        // Arrange
        CreateReservationDTO dto = new CreateReservationDTO(
            client.getCui(),
            hotelId,
            roomId,
            startDate,
            endDate,
            null
        );
        when(findClientByCuiOutputPort.findByCui(dto.getClientCui())).thenReturn(Optional.of(client));
        when(findRoomByHotelAndIdOutputPort.findByHotelAndId(hotelId.toString(), roomId.toString()))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> useCase.handle(dto));
    }

    @Test
    void givenStartAfterEndDate_whenHandle_thenThrowsInvalidParameterException() {
        // Arrange
        CreateReservationDTO dto = new CreateReservationDTO(
            client.getCui(),
            hotelId,
            roomId,
            endDate,   // swapped: startDate is after endDate
            startDate,
            null
        );
        when(findClientByCuiOutputPort.findByCui(dto.getClientCui())).thenReturn(Optional.of(client));
        when(findRoomByHotelAndIdOutputPort.findByHotelAndId(hotelId.toString(), roomId.toString()))
            .thenReturn(Optional.of(new RoomDTO(roomId.toString(), "101", pricePerNight, 2, hotelId.toString(), "HotelName")));
        // No need for availability check because parameter validation should occur first

        // Act & Assert
        assertThrows(InvalidParameterException.class, () -> useCase.handle(dto));
    }

    @Test
    void givenRoomNotAvailable_whenHandle_thenThrowsInvalidParameterException() {
        // Arrange
        CreateReservationDTO dto = new CreateReservationDTO(
            client.getCui(),
            hotelId,
            roomId,
            startDate,
            endDate,
            null
        );
        when(findClientByCuiOutputPort.findByCui(dto.getClientCui())).thenReturn(Optional.of(client));
        when(findRoomByHotelAndIdOutputPort.findByHotelAndId(hotelId.toString(), roomId.toString()))
            .thenReturn(Optional.of(new RoomDTO(roomId.toString(), "101", pricePerNight, 2, hotelId.toString(), "HotelName")));
        when(checkRoomAvailabilityOutputPort.isAvailable(hotelId, roomId, startDate, endDate))
            .thenReturn(false);

        // Act & Assert
        assertThrows(InvalidParameterException.class, () -> useCase.handle(dto));
    }

    @Test
    void givenPaymentFails_whenHandle_thenThrowsInvalidParameterException() {
        // Arrange
        CreateReservationDTO dto = new CreateReservationDTO(
            client.getCui(),
            hotelId,
            roomId,
            startDate,
            endDate,
            null
        );
        when(findClientByCuiOutputPort.findByCui(dto.getClientCui())).thenReturn(Optional.of(client));
        when(findRoomByHotelAndIdOutputPort.findByHotelAndId(hotelId.toString(), roomId.toString()))
            .thenReturn(Optional.of(new RoomDTO(roomId.toString(), "101", pricePerNight, 2, hotelId.toString(), "HotelName")));
        when(checkRoomAvailabilityOutputPort.isAvailable(hotelId, roomId, startDate, endDate))
            .thenReturn(true);
        when(createPaymentOutputPort.createPayment(any()))
            .thenReturn(false);

        // Act & Assert
        assertThrows(InvalidParameterException.class, () -> useCase.handle(dto));
    }

    @Test
    void givenPromotionProvided_whenExists_thenApplyPromotion() throws Exception {
        // Arrange
        UUID promoId = UUID.randomUUID();
        PromotionDTO promotion = new PromotionDTO(
            promoId,
            new BigDecimal("20"), // 20%
            LocalDate.now().minusDays(1),
            LocalDate.now().plusDays(10),
            "SUMMER_SALE",
            null // promotionType may not be used in logic
        );
        CreateReservationDTO dto = new CreateReservationDTO(
            client.getCui(),
            hotelId,
            roomId,
            startDate,
            endDate,
            promoId
        );
        when(findClientByCuiOutputPort.findByCui(dto.getClientCui())).thenReturn(Optional.of(client));
        RoomDTO room = new RoomDTO(roomId.toString(), "101", pricePerNight, 2, hotelId.toString(), "HotelName");
        when(findRoomByHotelAndIdOutputPort.findByHotelAndId(hotelId.toString(), roomId.toString()))
            .thenReturn(Optional.of(room));
        when(checkRoomAvailabilityOutputPort.isAvailable(hotelId, roomId, startDate, endDate))
            .thenReturn(true);
        when(findPromotionByIdOutputPort.findPromotionById(promoId.toString()))
            .thenReturn(Optional.of(promotion));
        when(createPaymentOutputPort.createPayment(any()))
            .thenReturn(true);
        when(createReservationOutputPort.createReservation(any(Reservation.class)))
            .thenAnswer(inv -> inv.getArgument(0));

        // Act
        Reservation result = useCase.handle(dto);

        // Assert
        assertNotNull(result);
        // total cost should be subtotal minus promotion amount
        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        BigDecimal expectedSubtotal = pricePerNight.multiply(BigDecimal.valueOf(days));
        BigDecimal expectedDiscount = expectedSubtotal.multiply(promotion.getPercentage().divide(BigDecimal.valueOf(100)));
        BigDecimal expectedTotal = expectedSubtotal.subtract(expectedDiscount);

        // verify
        assert(result.getSubtotal().compareTo(expectedSubtotal) == 0);
        assert(result.getTotalCost().compareTo(expectedTotal) == 0);

        verify(createPaymentOutputPort).createPayment(any());
        verify(createReservationOutputPort).createReservation(any(Reservation.class));
    }
}