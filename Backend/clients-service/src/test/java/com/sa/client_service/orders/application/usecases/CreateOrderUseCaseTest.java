package com.sa.client_service.orders.application.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sa.client_service.clients.domain.Client;
import com.sa.client_service.orders.application.dtos.CreateOrderDTO;
import com.sa.client_service.orders.application.dtos.CreateOrderItemDTO;
import com.sa.client_service.orders.application.dtos.ExistantDishesRestaurantDTO;
import com.sa.client_service.orders.application.dtos.DishDTO;
import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.application.outputports.CreateOrderOutputPort;
import com.sa.client_service.orders.application.outputports.ExistsRestaurantByIdOutputPort;
import com.sa.client_service.orders.application.outputports.ExistDishesByRestaurantOutputPort;
import com.sa.client_service.orders.application.inputports.CreateOrderInputPort;
import com.sa.client_service.reservations.application.outputports.FindPromotionByIdOutputPort;
import com.sa.client_service.shared.application.outputports.CreatePaymentOutputPort;
import com.sa.shared.exceptions.InvalidParameterException;
import com.sa.shared.exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CreateOrderUseCaseTest {

    @Mock
    private CreateOrderOutputPort createOrderOutputPort;
    @Mock
    private ExistsRestaurantByIdOutputPort existsRestaurantByIdOutputPort;
    @Mock
    private ExistDishesByRestaurantOutputPort existDishesByRestaurantOutputPort;
    @Mock
    private FindPromotionByIdOutputPort findPromotionByIdOutputPort;
    @Mock
    private CreatePaymentOutputPort createPaymentOutputPort;
    @Mock
    private com.sa.client_service.clients.application.outputports.FindClientByCuiOutputPort findClientByCuiOutputPort;

    private CreateOrderInputPort useCase;

    private UUID restaurantId = UUID.randomUUID();
    private Client client = Client.register("Alice", "Smith", "alice@example.com", "CUI123");

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        useCase = new CreateOrderUseCase(
            createOrderOutputPort,
            existsRestaurantByIdOutputPort,
            existDishesByRestaurantOutputPort,
            findClientByCuiOutputPort,
            findPromotionByIdOutputPort,
            createPaymentOutputPort
        );
    }

    @Test
    void givenValidOrderWithoutPromotion_whenHandle_thenReturnsOrder() throws Exception {
        // Arrange
        when(existsRestaurantByIdOutputPort.existsById(restaurantId.toString()))
            .thenReturn(true);
        when(findClientByCuiOutputPort.findByCui("CUI123"))
            .thenReturn(Optional.of(client));

        UUID dishId = UUID.randomUUID();
        DishDTO dishDto = new DishDTO();
        dishDto.setId(dishId);
        dishDto.setName("Pizza");
        dishDto.setPrice(BigDecimal.TEN);

        ExistantDishesRestaurantDTO existDto = new ExistantDishesRestaurantDTO();
        existDto.setAllPresent(true);
        existDto.setPresentDishes(List.of(dishDto));

        when(existDishesByRestaurantOutputPort.existantDishesRestaurant(
            restaurantId.toString(), List.of(dishId)))
            .thenReturn(existDto);

        when(createPaymentOutputPort.createPayment(any()))
            .thenReturn(true);
        when(createOrderOutputPort.createOrder(any())
        ).thenAnswer(inv -> inv.getArgument(0));

        CreateOrderDTO dto = new CreateOrderDTO();
        dto.setClientCui("CUI123");
        dto.setRestaurantId(restaurantId);
        dto.setOrderedAt(LocalDate.now());
        CreateOrderItemDTO itemDto = new CreateOrderItemDTO();
        itemDto.setDishId(dishId);
        itemDto.setQuantity(2);
        dto.setCreateOrderItemDTOs(List.of(itemDto));

        // Act
        Order result = useCase.handle(dto);

        // Assert
        // Check non-null and fields
        assert result != null;
        // Total should be 10 * 2 = 20
        assert result.getTotal().compareTo(BigDecimal.valueOf(20)) == 0;
        verify(createOrderOutputPort).createOrder(any());
        verify(createPaymentOutputPort).createPayment(any());
    }

    @Test
    void givenNonexistentRestaurant_whenHandle_thenThrowsNotFoundException() {
        // Arrange
        when(existsRestaurantByIdOutputPort.existsById(restaurantId.toString()))
            .thenReturn(false);

        CreateOrderDTO dto = new CreateOrderDTO();
        dto.setRestaurantId(restaurantId);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> useCase.handle(dto));
    }

    @Test
    void givenMissingDishItems_whenHandle_thenThrowsInvalidParameterException() {
        // Arrange
        when(existsRestaurantByIdOutputPort.existsById(restaurantId.toString()))
            .thenReturn(true);
        when(findClientByCuiOutputPort.findByCui("CUI123"))
            .thenReturn(Optional.of(client));

        ExistantDishesRestaurantDTO existDto = new ExistantDishesRestaurantDTO();
        existDto.setAllPresent(false);

        when(existDishesByRestaurantOutputPort.existantDishesRestaurant(any(), any()))
            .thenReturn(existDto);

        CreateOrderDTO dto = new CreateOrderDTO();
        dto.setClientCui("CUI123");
        dto.setRestaurantId(restaurantId);
        dto.setOrderedAt(LocalDate.now());
        CreateOrderItemDTO itemDto = new CreateOrderItemDTO();
        dto.setCreateOrderItemDTOs(List.of(itemDto));

        assertThrows(InvalidParameterException.class, () -> useCase.handle(dto));
    }

    @Test
    void givenInvalidPayment_whenHandle_thenThrowsInvalidParameterException() {
        // full happy path until payment fails
        when(existsRestaurantByIdOutputPort.existsById(restaurantId.toString()))
            .thenReturn(true);
        when(findClientByCuiOutputPort.findByCui("CUI123"))
            .thenReturn(Optional.of(client));

        UUID dishId = UUID.randomUUID();
        DishDTO dishDto = new DishDTO();
        dishDto.setId(dishId);
        dishDto.setName("Sandwich");
        dishDto.setPrice(BigDecimal.ONE);

        ExistantDishesRestaurantDTO existDto = new ExistantDishesRestaurantDTO();
        existDto.setAllPresent(true);
        existDto.setPresentDishes(List.of(dishDto));

        when(existDishesByRestaurantOutputPort.existantDishesRestaurant(any(), any()))
            .thenReturn(existDto);

        when(createPaymentOutputPort.createPayment(any()))
            .thenReturn(false);

        CreateOrderDTO dto = new CreateOrderDTO();
        dto.setClientCui("CUI123");
        dto.setRestaurantId(restaurantId);
        dto.setOrderedAt(LocalDate.now());
        CreateOrderItemDTO itemDto = new CreateOrderItemDTO();
        itemDto.setDishId(dishId);
        itemDto.setQuantity(1);
        dto.setCreateOrderItemDTOs(List.of(itemDto));

        assertThrows(InvalidParameterException.class, () -> useCase.handle(dto));
    }

    @Test
    void givenPromotionNotFound_whenPromotionIdProvided_thenThrowsNotFoundException() {
        // Arrange a promotion but return empty
        when(existsRestaurantByIdOutputPort.existsById(restaurantId.toString()))
            .thenReturn(true);
        when(findClientByCuiOutputPort.findByCui("CUI123"))
            .thenReturn(Optional.of(client));

        UUID dishId = UUID.randomUUID();
        DishDTO dishDto = new DishDTO();
        dishDto.setId(dishId);
        dishDto.setName("Coffee");
        dishDto.setPrice(BigDecimal.TEN);

        ExistantDishesRestaurantDTO existDto = new ExistantDishesRestaurantDTO();
        existDto.setAllPresent(true);
        existDto.setPresentDishes(List.of(dishDto));

        when(existDishesByRestaurantOutputPort.existantDishesRestaurant(any(), any()))
            .thenReturn(existDto);

        when(findPromotionByIdOutputPort.findPromotionById(any()))
            .thenReturn(Optional.empty());

        when(createPaymentOutputPort.createPayment(any()))
            .thenReturn(true);

        CreateOrderDTO dto = new CreateOrderDTO();
        dto.setClientCui("CUI123");
        dto.setRestaurantId(restaurantId);
        dto.setOrderedAt(LocalDate.now());
        dto.setPromotionId(UUID.randomUUID());
        CreateOrderItemDTO itemDto = new CreateOrderItemDTO();
        itemDto.setDishId(dishId);
        itemDto.setQuantity(1);
        dto.setCreateOrderItemDTOs(List.of(itemDto));

        assertThrows(NotFoundException.class, () -> useCase.handle(dto));
    }
}