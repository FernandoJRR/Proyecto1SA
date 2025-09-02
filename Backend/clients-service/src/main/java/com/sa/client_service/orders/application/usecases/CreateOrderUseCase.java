package com.sa.client_service.orders.application.usecases;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.sa.application.annotations.UseCase;
import com.sa.client_service.clients.application.outputports.FindClientByCuiOutputPort;
import com.sa.client_service.clients.domain.Client;
import com.sa.client_service.orders.application.dtos.CreateOrderDTO;
import com.sa.client_service.orders.application.dtos.CreateOrderItemDTO;
import com.sa.client_service.orders.application.dtos.DishDTO;
import com.sa.client_service.orders.application.dtos.ExistantDishesRestaurantDTO;
import com.sa.client_service.orders.application.inputports.CreateOrderInputPort;
import com.sa.client_service.orders.application.outputports.CreateOrderOutputPort;
import com.sa.client_service.orders.application.outputports.ExistDishesByRestaurantOutputPort;
import com.sa.client_service.orders.application.outputports.ExistsRestaurantByIdOutputPort;
import com.sa.client_service.orders.domain.Order;
import com.sa.client_service.orders.domain.OrderItem;
import com.sa.client_service.reservations.application.dtos.PromotionDTO;
import com.sa.client_service.reservations.application.outputports.FindPromotionByIdOutputPort;
import com.sa.client_service.shared.application.dtos.CreatePaymentDTO;
import com.sa.client_service.shared.application.outputports.CreatePaymentOutputPort;
import com.sa.shared.exceptions.InvalidParameterException;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreateOrderUseCase implements CreateOrderInputPort {

    private final CreateOrderOutputPort createOrderOutputPort;
    private final ExistsRestaurantByIdOutputPort existsRestaurantByIdOutputPort;
    private final ExistDishesByRestaurantOutputPort existDishesByRestaurantOutputPort;
    private final FindClientByCuiOutputPort findClientByCuiOutputPort;
    private final FindPromotionByIdOutputPort findPromotionByIdOutputPort;
    private final CreatePaymentOutputPort createPaymentOutputPort;

    @Override
    public Order handle(CreateOrderDTO createOrderDTO) throws NotFoundException, InvalidParameterException {

        final String clientCui = createOrderDTO.getClientCui();
        final UUID restaurantId = createOrderDTO.getRestaurantId();
        final List<CreateOrderItemDTO> itemsDto = createOrderDTO.getCreateOrderItemDTOs();

        boolean restaurantExists = existsRestaurantByIdOutputPort.existsById(restaurantId.toString());
        if (!restaurantExists) {
            throw new NotFoundException("El restaurante buscado no existe");
        }

        Client foundClient = findClientByCuiOutputPort.findByCui(clientCui)
                .orElseThrow(() -> new NotFoundException("El cliente buscado no existe"));

        List<OrderItem> domainItems = new ArrayList<>(itemsDto.size());
        BigDecimal total = BigDecimal.ZERO;

        List<UUID> dishIds = itemsDto.stream()
                .map(CreateOrderItemDTO::getDishId)
                .collect(Collectors.toList());

        ExistantDishesRestaurantDTO existResult = existDishesByRestaurantOutputPort
                .existantDishesRestaurant(restaurantId.toString(), dishIds);

        if (!existResult.isAllPresent()) {
            throw new InvalidParameterException("Algunos de los platillos en la orden no existen.");
        }

        final Map<UUID, DishDTO> dishesById = existResult.getPresentDishes()
                .stream()
                .collect(Collectors.toMap(DishDTO::getId, Function.identity()));

        for (CreateOrderItemDTO createItem : itemsDto) {
            DishDTO dish = dishesById.get(createItem.getDishId());

            BigDecimal lineTotal = dish.getPrice().multiply(BigDecimal.valueOf(createItem.getQuantity()));
            total = total.add(lineTotal);

            OrderItem item = OrderItem.create(
                    dish.getId(),
                    dish.getName(),
                    createItem.getQuantity(),
                    dish.getPrice());
            domainItems.add(item);
        }

        Order createdOrder = Order.create(foundClient, restaurantId, domainItems, total);

        String promotionIdPayment = null;
        if (createOrderDTO.getPromotionId() != null) {
            PromotionDTO promotion = findPromotionByIdOutputPort
                    .findPromotionById(createOrderDTO.getPromotionId().toString())
                    .orElseThrow(() -> new NotFoundException("La promocion ingresada no existe."));

            createdOrder.applyPromotion(promotion.getId().toString(), promotion.getPromotionType().getName(),
                    promotion.getPercentage());

            promotionIdPayment = createOrderDTO.getPromotionId().toString();
        }

        boolean resultPayment = createPaymentOutputPort.createPayment(new CreatePaymentDTO(restaurantId, foundClient.getId(), "ORDER",
                createdOrder.getId(), "CARD", createdOrder.getSubtotal(), "1234", promotionIdPayment));
        if (!resultPayment) {
            throw new InvalidParameterException("Error creando el pago");
        }

        return createOrderOutputPort.createOrder(createdOrder);

    }
}
