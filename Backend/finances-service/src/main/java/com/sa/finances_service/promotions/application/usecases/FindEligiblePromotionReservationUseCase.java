package com.sa.finances_service.promotions.application.usecases;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.sa.application.annotations.UseCase;
import com.sa.finances_service.promotions.application.dtos.FindReservationEligibilityDTO;
import com.sa.finances_service.promotions.application.inputports.FindEligiblePromotionReservationInputPort;
import com.sa.finances_service.promotions.application.outputports.FindPromotionsByDateAndHotelOutputPort;
import com.sa.finances_service.promotions.application.outputports.MostFrequentClientsOutputPort;
import com.sa.finances_service.promotions.domain.Promotion;
import com.sa.finances_service.promotions.domain.PromotionType;
import com.sa.finances_service.promotions.infrastructure.clientsadapter.adapters.MostPopularRoomsAdapter;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindEligiblePromotionReservationUseCase implements FindEligiblePromotionReservationInputPort {

    private final FindPromotionsByDateAndHotelOutputPort findPromotionsByDateAndHotelOutputPort;
    private final MostFrequentClientsOutputPort mostFrequentClientsOutputPort;
    private final MostPopularRoomsAdapter mostPopularRoomsAdapter;

    @Override
    public Promotion handle(FindReservationEligibilityDTO dto) throws NotFoundException {
        List<Promotion> foundPromotions = findPromotionsByDateAndHotelOutputPort
            .findByStartDateAndHotel(dto.getStartDate(), dto.getHotelId());

        //TODO: validate room and hotel exist
        //TODO: validate client exist

        if (foundPromotions.isEmpty()) {
            throw new NotFoundException("No hay promociones aplicables para esta reservacion");
        }

        List<Promotion> eligiblePromotions = new ArrayList<>();

        for (Promotion foundPromotion : foundPromotions) {
            if (foundPromotion.getPromotionType().equals(PromotionType.CLIENT_MOST_FREQUENT)) {
                //Tests for promotion based on frequent client
                List<UUID> mostFrequentClients = mostFrequentClientsOutputPort.findMostFrequent(foundPromotion.getTopCount());
                if (mostFrequentClients.contains(UUID.fromString(dto.getClientId()))) {
                    //Is eligible for this promotion
                    eligiblePromotions.add(foundPromotion);
                }
            } else if (foundPromotion.getPromotionType().equals(PromotionType.ROOM_MOST_POPULAR)) {
                //Tests for promotion based on popular room
                List<UUID> mostPopularRooms = mostPopularRoomsAdapter.findMostPopular(dto.getHotelId(), foundPromotion.getTopCount());

                if (mostPopularRooms.contains(UUID.fromString(dto.getRoomId()))) {
                    //Is eligible for this promotion
                    eligiblePromotions.add(foundPromotion);
                }
            }
        }

        if (eligiblePromotions.isEmpty()) {
            throw new NotFoundException("No hay promociones aplicables para esta reservacion");
        }

        return eligiblePromotions.stream()
                .max(Comparator.comparing(Promotion::getPercentage))
                .orElseThrow(() -> new NotFoundException("No hay promociones aplicables para esta reservación"));
    }

}
