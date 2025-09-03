package com.sa.client_service.reservations.infrastructure.restadapter.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sa.client_service.reservations.application.outputports.ReservationHydrationOutputPort;
import com.sa.client_service.reservations.domain.Reservation;
import com.sa.client_service.reservations.infrastructure.restadapter.dtos.ReservationHydratedResponse;
import com.sa.client_service.reservations.infrastructure.restadapter.dtos.ReservationResponse;
import com.sa.client_service.shared.infrastructure.restadapter.mappers.PromotionRestMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationHydrationAssembler {
    private final ReservationHydrationOutputPort reservationHydrationOutputPort;
    private final PromotionRestMapper promotionRestMapper;

    public ReservationHydratedResponse toResponse(Reservation r) {
        var hotel = reservationHydrationOutputPort.getHotel(r.getHotelId().toString());
        var room = reservationHydrationOutputPort.getRoom(r.getHotelId().toString(), r.getRoomId().toString());
        return new ReservationHydratedResponse(
                r.getId().toString(),
                r.getClient().getCui(),
                r.getHotelId(),
                r.getRoomId(),
                r.getStartDate(),
                r.getEndDate(),
                r.getTotalCost(),
                r.getSubtotal(),
                promotionRestMapper.toResponse(r.getPromotionApplied()),
                hotel,
                room);
    }

    public List<ReservationHydratedResponse> toResponseList(List<Reservation> list) {
        return list.stream().map(this::toResponse).toList();
    }
}
