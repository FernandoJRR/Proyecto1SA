package com.sa.client_service.reviews.application.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sa.client_service.clients.application.outputports.FindClientByCuiOutputPort;
import com.sa.client_service.clients.domain.Client;
import com.sa.client_service.orders.application.dtos.ExistantDishesRestaurantDTO;
import com.sa.client_service.orders.application.outputports.ExistDishesByRestaurantOutputPort;
import com.sa.client_service.reviews.application.dtos.CreateReviewDTO;
import com.sa.client_service.reviews.application.dtos.FindReviewsDTO;
import com.sa.client_service.reviews.application.outputports.CreateReviewOutputPort;
import com.sa.client_service.reviews.application.outputports.FindReviewsOutputPort;
import com.sa.client_service.reviews.domain.EstablishmentType;
import com.sa.client_service.reviews.domain.Review;
import com.sa.client_service.shared.application.outputports.ExistsRoomInHotelByIdOutputPort;
import com.sa.shared.exceptions.InvalidParameterException;
import com.sa.shared.exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CreateReviewUseCaseTest {

    @Mock
    private CreateReviewOutputPort createReviewOutputPort;

    @Mock
    private ExistsRoomInHotelByIdOutputPort existsRoomInHotelByIdOutputPort;

    @Mock
    private ExistDishesByRestaurantOutputPort existDishesByRestaurantOutputPort;

    @Mock
    private FindClientByCuiOutputPort findClientByCuiOutputPort;

    @Mock
    private FindReviewsOutputPort findReviewsOutputPort;

    private CreateReviewUseCase useCase;

    private final String validClientCui = "CUI-12345";
    private Client client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = Client.register("Alice", "Smith", "alice@example.com", validClientCui);
        useCase = new CreateReviewUseCase(
            createReviewOutputPort,
            existsRoomInHotelByIdOutputPort,
            existDishesByRestaurantOutputPort,
            findClientByCuiOutputPort,
            findReviewsOutputPort
        );
    }

    @Test
    void givenValidHotelReview_whenHandle_thenCreatesReview() throws Exception {
        // Arrange
        CreateReviewDTO dto = new CreateReviewDTO(
            validClientCui,
            "hotel123",
            EstablishmentType.HOTEL.toString(),
            "roomABC",    // sourceId (room id)
            4,
            "Nice stay"
        );

        when(findClientByCuiOutputPort.findByCui(validClientCui))
            .thenReturn(Optional.of(client));
        when(findReviewsOutputPort.findReviews(any(FindReviewsDTO.class)))
            .thenReturn(List.of());  // no existing reviews
        when(existsRoomInHotelByIdOutputPort.existsById(dto.getEstablishmentId(), dto.getSourceId()))
            .thenReturn(true);
        // stub output port to echo the review
        when(createReviewOutputPort.createReview(any(String.class), any(Review.class)))
            .thenAnswer(inv -> inv.getArgument(1));

        // Act
        Review result = useCase.handle(dto);

        // Assert
        assertNotNull(result);
        // Check some fields
        assert result.getEstablishmentId().equals(dto.getEstablishmentId());
        assert result.getSourceId().equals(dto.getSourceId());
        assert result.getRating().getValue().intValue() == dto.getRating();
        assert result.getComment().equals(dto.getComment());

        verify(findClientByCuiOutputPort).findByCui(validClientCui);
        verify(findReviewsOutputPort).findReviews(any(FindReviewsDTO.class));
        verify(existsRoomInHotelByIdOutputPort).existsById(dto.getEstablishmentId(), dto.getSourceId());
        verify(createReviewOutputPort).createReview(validClientCui, result);
    }

    @Test
    void givenValidRestaurantReview_whenHandle_thenCreatesReview() throws Exception {
        // Arrange
        CreateReviewDTO dto = new CreateReviewDTO(
            validClientCui,
            "restaurantXYZ",
            EstablishmentType.RESTAURANT.toString(),
            "123e4567-e89b-12d3-a456-426614174000",   // sourceId (dish id)
            5,
            "Excellent food"
        );

        when(findClientByCuiOutputPort.findByCui(validClientCui))
            .thenReturn(Optional.of(client));
        when(findReviewsOutputPort.findReviews(any(FindReviewsDTO.class)))
            .thenReturn(List.of());  // no existing reviews
        when(existDishesByRestaurantOutputPort.existantDishesRestaurant(
            dto.getEstablishmentId(),
            List.of(UUID.fromString(dto.getSourceId()))
        )).thenReturn(new com.sa.client_service.orders.application.dtos.ExistantDishesRestaurantDTO(true, List.of(), List.of()));

        when(createReviewOutputPort.createReview(any(String.class), any(Review.class)))
            .thenAnswer(inv -> inv.getArgument(1));

        // Act
        Review result = useCase.handle(dto);

        // Assert
        assertNotNull(result);
        assert result.getEstablishmentType().equals(EstablishmentType.RESTAURANT);
        assert result.getRating().getValue().intValue() == dto.getRating();
        assert result.getSourceId().equals(dto.getSourceId());
        assert result.getComment().equals(dto.getComment());

        verify(existDishesByRestaurantOutputPort).existantDishesRestaurant(
            dto.getEstablishmentId(),
            List.of(UUID.fromString(dto.getSourceId()))
        );
        verify(createReviewOutputPort).createReview(validClientCui, result);
    }

    @Test
    void givenInvalidEstablishmentType_whenHandle_thenThrowsInvalidParameterException() {
        CreateReviewDTO dto = new CreateReviewDTO(
            validClientCui,
            "someId",
            "INVALID_TYPE",
            "sourceId",
            3,
            "Comment"
        );

        assertThrows(InvalidParameterException.class, () -> useCase.handle(dto));
    }

    @Test
    void givenClientNotFound_whenHandle_thenThrowsNotFoundException() {
        CreateReviewDTO dto = new CreateReviewDTO(
            "nonexistentCui",
            "hotel123",
            EstablishmentType.HOTEL.toString(),
            "roomABC",
            2,
            "Comment"
        );

        when(findClientByCuiOutputPort.findByCui(dto.getClientCui()))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.handle(dto));
    }

    @Test
    void givenRoomNotExists_whenHandle_thenThrowsNotFoundException() {
        CreateReviewDTO dto = new CreateReviewDTO(
            validClientCui,
            "hotelId1",
            EstablishmentType.HOTEL.toString(),
            "roomId1",
            1,
            "Good"
        );
        when(findClientByCuiOutputPort.findByCui(validClientCui))
            .thenReturn(Optional.of(client));
        when(findReviewsOutputPort.findReviews(any(FindReviewsDTO.class)))
            .thenReturn(List.of());
        when(existsRoomInHotelByIdOutputPort.existsById(dto.getEstablishmentId(), dto.getSourceId()))
            .thenReturn(false);

        assertThrows(NotFoundException.class, () -> useCase.handle(dto));
    }

    @Test
    void givenRestaurantDishNotExists_whenHandle_thenThrowsNotFoundException() {
        CreateReviewDTO dto = new CreateReviewDTO(
            validClientCui,
            "restaurant123",
            EstablishmentType.RESTAURANT.toString(),
            UUID.randomUUID().toString(),
            4,
            "Nice"
        );
        when(findClientByCuiOutputPort.findByCui(validClientCui))
            .thenReturn(Optional.of(client));
        when(findReviewsOutputPort.findReviews(any(FindReviewsDTO.class)))
            .thenReturn(List.of());
        when(existDishesByRestaurantOutputPort.existantDishesRestaurant(
            dto.getEstablishmentId(),
            List.of(UUID.fromString(dto.getSourceId()))
        )).thenReturn(new ExistantDishesRestaurantDTO(false, List.of(), List.of()));

        assertThrows(NotFoundException.class, () -> useCase.handle(dto));
    }

    @Test
    void givenAlreadyReviewed_whenHandle_thenThrowsInvalidParameterException() {
        CreateReviewDTO dto = new CreateReviewDTO(
            validClientCui,
            "hotel123",
            EstablishmentType.HOTEL.toString(),
            "roomID",
            5,
            "Nice stay again"
        );

        when(findClientByCuiOutputPort.findByCui(validClientCui))
            .thenReturn(Optional.of(client));
        // Simulate existing review
        Review existing = Review.create(dto.getEstablishmentId(),
            EstablishmentType.HOTEL,
            dto.getSourceId(),
            dto.getRating(),
            dto.getComment());
        when(findReviewsOutputPort.findReviews(any(FindReviewsDTO.class)))
            .thenReturn(List.of(existing));
        when(existsRoomInHotelByIdOutputPort.existsById(dto.getEstablishmentId(), dto.getSourceId()))
            .thenReturn(true);

        assertThrows(InvalidParameterException.class, () -> useCase.handle(dto));
    }
}