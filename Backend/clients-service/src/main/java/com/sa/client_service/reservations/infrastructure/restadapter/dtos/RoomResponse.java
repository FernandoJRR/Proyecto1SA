package com.sa.client_service.reservations.infrastructure.restadapter.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoomResponse {
    private String id;
    private String number;
    private BigDecimal pricePerNight;
    private Integer capacity;
    private String hotelId;
    private String hotelName;
}
