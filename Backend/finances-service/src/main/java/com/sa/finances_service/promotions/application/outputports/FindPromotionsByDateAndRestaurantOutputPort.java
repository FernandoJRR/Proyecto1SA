package com.sa.finances_service.promotions.application.outputports;

import java.time.LocalDate;
import java.util.List;

import com.sa.finances_service.promotions.domain.Promotion;

public interface FindPromotionsByDateAndRestaurantOutputPort {
    public List<Promotion> findByStartDateAndRestaurant(LocalDate startDate, String restaurantId);
}
