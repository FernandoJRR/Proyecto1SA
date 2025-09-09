package com.sa.client_service.reservations.application.dtos;

import java.util.List;

import com.sa.client_service.reservations.domain.Reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MostPopularRoomDTO {
    private String hotelName;
    private String roomNumber;
    private List<Reservation> reservations;
}
