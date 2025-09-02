package com.sa.establishment_service.hotels.infrastructure.restadapter.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateHotelRequest {
    private String name;

    private String address;

    private BigDecimal maintenanceCostPerWeek;
}
