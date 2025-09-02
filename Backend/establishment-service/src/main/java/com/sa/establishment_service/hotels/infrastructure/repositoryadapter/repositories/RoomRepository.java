package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.RoomEntity;

public interface RoomRepository extends JpaRepository<RoomEntity, String> {
    boolean existsByHotel_IdAndNumber(String hotelId, String number);
    Optional<RoomEntity> findOneByHotel_IdAndId(String hotelId, String id);
    List<RoomEntity> findAllByHotel_Id(String hotelId);
}
