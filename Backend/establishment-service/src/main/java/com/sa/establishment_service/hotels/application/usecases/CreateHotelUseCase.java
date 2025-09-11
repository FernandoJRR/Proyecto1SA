package com.sa.establishment_service.hotels.application.usecases;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.hotels.application.dtos.CreateHotelDTO;
import com.sa.establishment_service.hotels.application.inputports.CreateHotelInputPort;
import com.sa.establishment_service.hotels.application.outputports.CreateHotelOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
@Validated
public class CreateHotelUseCase implements CreateHotelInputPort {

    private final CreateHotelOutputPort createHotelOutputPort;

    @Override
    @Transactional
    public Hotel handle(@Valid CreateHotelDTO createHotelDTO) {
        Hotel createdHotel = Hotel.create(createHotelDTO.getName(),createHotelDTO.getAddress(),createHotelDTO.getMaintenanceCostPerWeek());
        return createHotelOutputPort.createHotel(createdHotel);
    }

}
