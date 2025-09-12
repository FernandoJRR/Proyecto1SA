package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sa.establishment_service.hotels.application.outputports.UpdateHotelOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers.HotelRepositoryMapper;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.HotelEntity;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.HotelRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class UpdateHotelAdapter implements UpdateHotelOutputPort {

    private final HotelRepository hotelRepository;
    private final HotelRepositoryMapper hotelRepositoryMapper;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Hotel updateHotel(Hotel hotel) {
        HotelEntity toSave = hotelRepositoryMapper.toEntity(hotel);
        HotelEntity saved = hotelRepository.save(toSave);
        return hotelRepositoryMapper.toDomain(saved);
    }
}

