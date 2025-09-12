package com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateRestaurantRequest {
    private String name;
    private String address;
}

