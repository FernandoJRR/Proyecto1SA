package com.sa.establishment_service.hotels.infrastructure.restadapter.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.sa.establishment_service.hotels.application.dtos.CreateHotelDTO;
import com.sa.establishment_service.hotels.application.dtos.UpdateHotelDTO;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.hotels.infrastructure.restadapter.dtos.CreateHotelRequest;
import com.sa.establishment_service.hotels.infrastructure.restadapter.dtos.UpdateHotelRequest;
import com.sa.establishment_service.hotels.infrastructure.restadapter.dtos.HotelResponse;

@Mapper(componentModel = "spring")
public interface HotelRestMapper {
    public CreateHotelDTO toDTO(CreateHotelRequest createHotelRequest);
    public UpdateHotelDTO toDTO(UpdateHotelRequest updateHotelRequest);
    public HotelResponse toResponse(Hotel hotel);
    public List<HotelResponse> toResponseList(List<Hotel> hotels);
}
