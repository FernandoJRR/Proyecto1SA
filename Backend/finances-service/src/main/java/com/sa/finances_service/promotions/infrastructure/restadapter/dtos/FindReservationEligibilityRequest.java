package com.sa.finances_service.promotions.infrastructure.restadapter.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FindReservationEligibilityRequest {
    private String clientId;
    private String hotelId;
    private String roomId;
    private LocalDate startDate;
    private LocalDate endDate;
}
