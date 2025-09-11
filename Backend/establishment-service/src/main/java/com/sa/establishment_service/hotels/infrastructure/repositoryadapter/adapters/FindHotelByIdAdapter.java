package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.sa.establishment_service.hotels.application.outputports.FindHotelByIdOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers.HotelRepositoryMapper;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.HotelRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindHotelByIdAdapter implements FindHotelByIdOutputPort {

    private final HotelRepository hotelRepository;

    private final HotelRepositoryMapper hotelRepositoryMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Hotel> findHotelById(UUID id) {
        return hotelRepository.findById(id.toString())
            .map(hotelRepositoryMapper::toDomain);
    }
}
