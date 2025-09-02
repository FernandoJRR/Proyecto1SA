package com.sa.client_service.reservations.infrastructure.repositoryadapter.repositories;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sa.client_service.reservations.infrastructure.repositoryadapter.models.ReservationEntity;

public interface ReservationRepository extends JpaRepository<ReservationEntity, String>,
        JpaSpecificationExecutor<ReservationEntity> {
    public boolean existsByHotelIdAndRoomIdAndStartDateLessThanAndEndDateGreaterThan(
            UUID hotelId,
            UUID roomId,
            LocalDate requestedEnd,
            LocalDate requestedStart
    );
}
