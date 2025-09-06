package com.sa.finances_service.promotions.application.inputports;

import com.sa.finances_service.promotions.application.dtos.FindReservationEligibilityDTO;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.shared.exceptions.NotFoundException;

public interface FindEligiblePromotionReservationInputPort {
    public Promotion handle(FindReservationEligibilityDTO dto) throws NotFoundException;
}
