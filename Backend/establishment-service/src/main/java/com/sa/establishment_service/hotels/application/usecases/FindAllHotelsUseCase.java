package com.sa.establishment_service.hotels.application.usecases;

import java.util.List;

import com.sa.application.annotations.UseCase;
import com.sa.establishment_service.hotels.application.inputports.FindAllHotelsInputPort;
import com.sa.establishment_service.hotels.application.outputports.FindAllHotelsOutputPort;
import com.sa.establishment_service.hotels.domain.Hotel;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindAllHotelsUseCase implements FindAllHotelsInputPort {

    private final FindAllHotelsOutputPort findAllHotelsOutputPort;

    @Override
    public List<Hotel> handle() {
        return findAllHotelsOutputPort.findAll();
    }

}
