package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import org.springframework.transaction.annotation.Transactional;

import com.sa.establishment_service.hotels.application.outputports.ExistsHotelByIdOutputPort;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.HotelRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class ExistsHotelByIdAdapter implements ExistsHotelByIdOutputPort {

    private final HotelRepository hotelRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        return hotelRepository.existsById(id);
    }

}
