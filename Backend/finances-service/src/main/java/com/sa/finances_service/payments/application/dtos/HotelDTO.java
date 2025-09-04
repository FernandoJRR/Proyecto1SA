package com.sa.finances_service.payments.application.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HotelDTO {
    private String id;
    private String name;
    private String address;
    private BigDecimal maintenanceCostPerWeek;
}
