package com.sa.establishment_service.hotels.infrastructure.repositoryadapter.adapters;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.sa.establishment_service.hotels.application.outputports.FindAllHotelsOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.mappers.HotelRepositoryMapper;
import com.sa.establishment_service.hotels.infrastructure.repositoryadapter.repositories.HotelRepository;
import com.sa.infrastructure.annotations.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindAllHotelsAdapter implements FindAllHotelsOutputPort {

    private final HotelRepository hotelRepository;
    private final HotelRepositoryMapper hotelRepositoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Hotel> findAll() {
        return hotelRepositoryMapper.toDomainList(hotelRepository.findAll());
    }

}
