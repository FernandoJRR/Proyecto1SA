package com.sa.finances_service.promotions.application.usecases;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.sa.application.annotations.UseCase;
import com.sa.finances_service.promotions.application.dtos.FindOrderEligibilityDTO;
import com.sa.finances_service.promotions.application.inputports.FindEligiblePromotionOrderInputPort;
import com.sa.finances_service.promotions.application.outputports.FindPromotionsByDateAndRestaurantOutputPort;
import com.sa.finances_service.promotions.application.outputports.MostFrequentClientsOutputPort;
import com.sa.finances_service.promotions.application.outputports.MostPopularDishesOutputPort;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.domain.PromotionType;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindEligiblePromotionOrderUseCase implements FindEligiblePromotionOrderInputPort {

    private final FindPromotionsByDateAndRestaurantOutputPort findPromotionsByDateAndRestaurantOutputPort;
    private final MostFrequentClientsOutputPort mostFrequentClientsOutputPort;
    private final MostPopularDishesOutputPort mostPopularDishesOutputPort;

    @Override
    public Promotion handle(FindOrderEligibilityDTO dto) throws NotFoundException {
        List<Promotion> foundPromotions = findPromotionsByDateAndRestaurantOutputPort
            .findByStartDateAndRestaurant(dto.getOrderedAt(), dto.getRestaurantId());

        //TODO: validate room and hotel exist
        //TODO: validate client exist

        if (foundPromotions.isEmpty()) {
            throw new NotFoundException("No hay promociones aplicables para esta orden");
        }

        List<Promotion> eligiblePromotions = new ArrayList<>();

        for (Promotion foundPromotion : foundPromotions) {
            if (foundPromotion.getPromotionType().equals(PromotionType.CLIENT_MOST_FREQUENT)) {
                //Tests for promotion based on frequent client
                List<UUID> mostFrequentClients = mostFrequentClientsOutputPort.findMostFrequent(foundPromotion.getTopCount());
                if (mostFrequentClients.contains(UUID.fromString(dto.getClientId()))) {
                    //Is eligible for this promotion
                    eligiblePromotions.add(foundPromotion);
                }
            } else if (foundPromotion.getPromotionType().equals(PromotionType.DISH_MOST_POPULAR)) {
                //Tests for promotion based on popular dish
                List<UUID> mostPopularDishes = mostPopularDishesOutputPort.findMostPopular(dto.getRestaurantId(), foundPromotion.getTopCount());

                //If any of the dishes on the order are on the promotion, the order is eligible
                if (dto.getDishesIds().stream().map(UUID::fromString).anyMatch(mostPopularDishes::contains)) {
                    //Is eligible for this promotion
                    eligiblePromotions.add(foundPromotion);
                }
            }
        }

        if (eligiblePromotions.isEmpty()) {
            throw new NotFoundException("No hay promociones aplicables para esta orden");
        }

        return eligiblePromotions.stream()
                .max(Comparator.comparing(Promotion::getPercentage))
                .orElseThrow(() -> new NotFoundException("No hay promociones aplicables para esta orden"));
    }

}
