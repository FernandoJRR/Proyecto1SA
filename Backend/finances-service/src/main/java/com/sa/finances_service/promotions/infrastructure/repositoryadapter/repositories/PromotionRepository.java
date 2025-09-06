package com.sa.finances_service.promotions.infrastructure.repositoryadapter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.sa.finances_service.promotions.domain.EstablishmentTypeEnum;
import com.sa.finances_service.promotions.infrastructure.repositoryadapter.models.PromotionEntity;

public interface PromotionRepository extends JpaRepository<PromotionEntity, String> {

    @Query("select p from PromotionEntity p \n" +
           "where p.establishmentType = :type \n" +
           "  and p.establishmentId = :establishmentId \n" +
           "  and :atDate between p.startDate and p.endDate")
    List<PromotionEntity> findActiveByEstablishment(
            @Param("type") EstablishmentTypeEnum type,
            @Param("establishmentId") String establishmentId,
            @Param("atDate") LocalDate atDate);

    default List<PromotionEntity> findActiveForHotel(String hotelId, LocalDate atDate) {
        return findActiveByEstablishment(EstablishmentTypeEnum.HOTEL, hotelId, atDate);
    }

    default List<PromotionEntity> findActiveForRestaurant(String restaurantId, LocalDate atDate) {
        return findActiveByEstablishment(EstablishmentTypeEnum.RESTAURANT, restaurantId, atDate);
    }
}
