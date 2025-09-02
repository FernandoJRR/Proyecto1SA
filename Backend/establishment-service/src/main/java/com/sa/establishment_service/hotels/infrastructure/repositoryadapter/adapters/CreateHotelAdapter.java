package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import com.sa.establishment_service.hotels.application.outputports.CreateHotelOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers.HotelRepositoryMapper;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.models.HotelEntity;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.HotelRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreateHotelAdapter implements CreateHotelOutputPort {

    private final HotelRepository hotelRepository;
    private final HotelRepositoryMapper hotelRepositoryMapper;

    @Override
    public Hotel createHotel(Hotel hotel) {
        HotelEntity hotelEntity = hotelRepositoryMapper.toEntity(hotel);
        HotelEntity createdHotel = hotelRepository.save(hotelEntity);
        return hotelRepositoryMapper.toDomain(createdHotel);
    }
}
