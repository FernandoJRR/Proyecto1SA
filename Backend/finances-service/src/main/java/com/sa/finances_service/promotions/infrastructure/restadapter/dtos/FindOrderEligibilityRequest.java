package com.sa.finances_service.promotions.infrastructure.restadapter.dtos;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FindOrderEligibilityRequest {
    private String clientId;

    private String restaurantId;

    private List<String> dishesIds;

    private LocalDate orderedAt;
}
