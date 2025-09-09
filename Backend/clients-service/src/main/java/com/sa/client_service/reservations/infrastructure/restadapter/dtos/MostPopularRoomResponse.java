package com.sa.client_service.reservations.infrastructure.restadapter.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MostPopularRoomResponse {
    private String hotelName;
    private String roomNumber;
    private List<ReservationResponse> reservations;
}
