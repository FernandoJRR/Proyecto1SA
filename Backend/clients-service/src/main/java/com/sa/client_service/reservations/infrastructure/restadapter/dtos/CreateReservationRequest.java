package com.sa.client_service.reservations.infrastructure.restadapter.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateReservationRequest {

    private String clientCui;

    private String hotelId;

    private String roomId;

    private LocalDate startDate;

    private LocalDate endDate;

    private String promotionId;
}
