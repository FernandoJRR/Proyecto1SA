package com.sa.client_service.reservations.infrastructure.restadapter.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.sa.client_service.reservations.application.dtos.HotelDTO;
import com.sa.client_service.reservations.application.dtos.RoomDTO;
import com.sa.client_service.shared.infrastructure.restadapter.dtos.PromotionAppliedResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReservationHydratedResponse {
    private String id;
    private String clientCui;
    private UUID hotelId;
    private UUID roomId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalCost;
    private BigDecimal subtotal;
    private PromotionAppliedResponse promotionApplied;

    private HotelDTO hotel;
    private RoomDTO room;
}
