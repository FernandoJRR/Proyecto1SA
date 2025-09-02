package com.sa.client_service.reservations.application.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoomDTO {
    private String id;
    private String number;
    private BigDecimal pricePerNight;
    private Integer capacity;
}
