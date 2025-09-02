package com.sa.client_service.reservations.infrastructure.repositoryadapter.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sa.client_service.reservations.domain.Reservation;
import com.sa.client_service.reservations.infrastructure.repositoryadapter.models.ReservationEntity;
import com.sa.client_service.shared.infrastructure.repositoryadapter.mappers.PromotionRepositoryMapper;

@Mapper(componentModel = "spring",
uses = { PromotionRepositoryMapper.class }
)
public interface ReservationRepositoryMapper {
    @Mapping(target = "promotionAppliedEntity", source = "promotionApplied")
    public ReservationEntity toEntity(Reservation reservation);
    @Mapping(target = "promotionApplied", source = "promotionAppliedEntity")
    public Reservation toDomain(ReservationEntity reservationEntity);
}
