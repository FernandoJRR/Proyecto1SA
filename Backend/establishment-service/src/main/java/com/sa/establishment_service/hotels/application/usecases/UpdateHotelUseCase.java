package com.sa.establishment_service.hotels.application.usecases;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.hotels.application.dtos.UpdateHotelDTO;
import com.sa.establishment_service.hotels.application.inputports.UpdateHotelInputPort;
import com.sa.establishment_service.hotels.application.outputports.FindHotelByIdOutputPort;
import com.sa.establishment_service.hotels.application.outputports.UpdateHotelOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.shared.exceptions.NotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
@Validated
public class UpdateHotelUseCase implements UpdateHotelInputPort {

    private final FindHotelByIdOutputPort findHotelByIdOutputPort;
    private final UpdateHotelOutputPort updateHotelOutputPort;

    @Override
    @Transactional
    public Hotel handle(String hotelId, @Valid UpdateHotelDTO updateHotelDTO) throws NotFoundException {
        Hotel existing = findHotelByIdOutputPort
                .findHotelById(UUID.fromString(hotelId))
                .orElseThrow(() -> new NotFoundException("El hotel buscado no existe"));

        existing.setName(updateHotelDTO.getName());
        existing.setAddress(updateHotelDTO.getAddress());
        existing.setMaintenanceCostPerWeek(updateHotelDTO.getMaintenanceCostPerWeek());

        return updateHotelOutputPort.updateHotel(existing);
    }
}
