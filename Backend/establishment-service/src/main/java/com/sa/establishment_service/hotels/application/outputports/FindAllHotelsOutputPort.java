package com.sa.establishment_service.hotels.application.outputports;

import java.util.List;

import com.sa.establishment_service.hotels.domain.Hotel;

public interface FindAllHotelsOutputPort {
    public List<Hotel> findAll();
}
