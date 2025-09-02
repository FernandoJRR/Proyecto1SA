package com.sa.client_service.reservations.application.dtos;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindReservationsDTO {
    private String clientCui;
    private UUID hotelId;
    private UUID roomId;
    private LocalDate startDate;
    private LocalDate endDate;
}
