package com.sa.establishment_service.hotels.application.inputports;

import java.util.List;

import com.sa.establishment_service.hotels.domain.Hotel;

public interface FindAllHotelsInputPort {
    public List<Hotel> handle();
}
