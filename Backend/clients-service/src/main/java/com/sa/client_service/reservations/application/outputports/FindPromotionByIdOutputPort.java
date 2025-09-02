package com.sa.client_service.reservations.application.outputports;

import java.util.Optional;

import com.sa.client_service.reservations.application.dtos.PromotionDTO;

public interface FindPromotionByIdOutputPort {
    public Optional<PromotionDTO> findPromotionById(String id);
}
