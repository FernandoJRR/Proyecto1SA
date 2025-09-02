package com.sa.finances_service.promotions.infrastructure.restadapter.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PromotionTypeResponse {
    private String name;
    private String code;
}
