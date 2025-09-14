package com.sa.client_service.orders.application.usecases;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sa.client_service.orders.application.dtos.MostPopularRestaurantDTO;
import com.sa.client_service.orders.application.dtos.RestaurantDTO;
import com.sa.client_service.orders.application.outputports.FindOrdersByRestaurantOutputPort;
import com.sa.client_service.orders.application.outputports.FindRestaurantByIdOutputPort;
import com.sa.client_service.orders.application.outputports.MostPopularRestaurantOutputPort;
import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.domain.OrderItem;
import com.sa.client_service.clients.domain.Client;
import com.sa.shared.exceptions.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MostPopularRestaurantUseCaseTest {

    @Mock
    private MostPopularRestaurantOutputPort mostPopularRestaurantOutputPort;

    @Mock
    private FindOrdersByRestaurantOutputPort findOrdersByRestaurantOutputPort;

    @Mock
    private FindRestaurantByIdOutputPort findRestaurantByIdOutputPort;

    @InjectMocks
    private MostPopularRestaurantUseCase useCase;

    private UUID restUuid;
    private UUID orderUuid;
    private Client dummyClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restUuid = UUID.randomUUID();
        orderUuid = UUID.randomUUID();
        // Create a dummy client with some UUID and maybe name
        dummyClient = new Client(UUID.randomUUID(), "Marcos", "Carlos", "email@mail.com", "1234567890000");
    }

    @Test
    void handle_whenNoMostPopular_thenThrowsNotFound() {
        when(mostPopularRestaurantOutputPort.getMostPopular())
            .thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            useCase.handle();
        });

        assertEquals("No se encontro ningun restaurante", ex.getMessage());
    }

    @Test
    void handle_whenMostPopularFoundButRestaurantNotFound_thenThrowsNotFound() {
        // Arrange — make sure restaurantId is not null
        Order mostPopularOrder = new Order(
            orderUuid,
            dummyClient,
            restUuid,
            List.of(
                new OrderItem(UUID.randomUUID(), "Dish A", UUID.randomUUID(), 2, new BigDecimal("12.50"))
            ),
            new BigDecimal("12.50"),
            LocalDate.now()
        );

        when(mostPopularRestaurantOutputPort.getMostPopular())
            .thenReturn(Optional.of(mostPopularOrder));
        when(findRestaurantByIdOutputPort.findById(restUuid.toString()))
            .thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            useCase.handle();
        });

        assertEquals("No se encontro ningun restaurante", ex.getMessage());
    }

    @Test
    void handle_whenMostPopularAndRestaurantFound_thenReturnsDTO() throws NotFoundException {
        // Arrange
        Order mostPopularOrder = new Order(
            orderUuid,
            dummyClient,
            restUuid,
            List.of(
                new OrderItem(UUID.randomUUID(), "Dish A", UUID.randomUUID(), 2, new BigDecimal("12.50")),
                new OrderItem(UUID.randomUUID(), "Dish B", UUID.randomUUID(), 1, new BigDecimal("8.75"))
            ),
            new BigDecimal("21.25"),
            LocalDate.now()
        );

        RestaurantDTO restaurantDto = new RestaurantDTO("Good Food", "1 Avenida");

        List<Order> ordersForRestaurant = List.of(
            new Order(
                UUID.randomUUID(),
                dummyClient,
                restUuid,
                List.of(new OrderItem(UUID.randomUUID(), "Dish C", UUID.randomUUID(), 3, new BigDecimal("5.00"))),
                new BigDecimal("15.00"),
                LocalDate.now()
            ),
            new Order(
                UUID.randomUUID(),
                dummyClient,
                restUuid,
                List.of(new OrderItem(UUID.randomUUID(), "Dish D", UUID.randomUUID(), 2, new BigDecimal("7.25"))),
                new BigDecimal("14.50"),
                LocalDate.now()
            )
        );

        when(mostPopularRestaurantOutputPort.getMostPopular())
            .thenReturn(Optional.of(mostPopularOrder));
        when(findRestaurantByIdOutputPort.findById(restUuid.toString()))
            .thenReturn(Optional.of(restaurantDto));
        when(findOrdersByRestaurantOutputPort.findByRestaurant(restUuid.toString()))
            .thenReturn(ordersForRestaurant);

        // Act
        MostPopularRestaurantDTO result = useCase.handle();

        // Assert
        assertNotNull(result);
        assertEquals("Good Food", result.getRestaurantName());
        assertEquals(ordersForRestaurant, result.getOrders());
    }
}