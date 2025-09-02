package com.sa.client_service.reservations.infrastructure.restadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sa.client_service.reservations.application.dtos.CreateReservationDTO;
import com.sa.client_service.reservations.domain.Reservation;
import com.sa.client_service.reservations.infrastructure.restadapter.dtos.CreateReservationRequest;
import com.sa.client_service.reservations.infrastructure.restadapter.dtos.ReservationResponse;

@Mapper(componentModel = "spring")
public interface ReservationRestMapper {
    public CreateReservationDTO toDTO(CreateReservationRequest createReservationRequest);
    @Mapping(source = "client.cui", target = "clientCui")
    public ReservationResponse toResponse(Reservation reservation);
    public List<ReservationResponse> toResponse(List<Reservation> reservations);
}
