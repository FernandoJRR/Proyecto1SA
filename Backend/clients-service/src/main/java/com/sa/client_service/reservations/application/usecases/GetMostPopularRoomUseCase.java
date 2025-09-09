package com.sa.client_service.reservations.application.usecases;

import java.util.List;
import java.util.UUID;

import com.sa.application.annotations.UseCase;
import com.sa.client_service.reservations.application.dtos.HotelDTO;
import com.sa.client_service.reservations.application.dtos.MostPopularRoomDTO;
import com.sa.client_service.reservations.application.dtos.RoomDTO;
import com.sa.client_service.reservations.application.inputports.GetMostPopularRoomInputPort;
import com.sa.client_service.reservations.application.outputports.FindHotelByIdOutputPort;
import com.sa.client_service.reservations.application.outputports.FindReservationsOutputPort;
import com.sa.client_service.reservations.application.outputports.FindRoomByHotelAndIdOutputPort;
import com.sa.client_service.reservations.application.outputports.GetMostPopularRoomOutputPort;
import com.sa.client_service.reservations.domain.Reservation;
import com.sa.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetMostPopularRoomUseCase implements GetMostPopularRoomInputPort {

    private final GetMostPopularRoomOutputPort getMostPopularRoomOutputPort;
    private final FindReservationsOutputPort findReservationsOutputPort;
    private final FindRoomByHotelAndIdOutputPort findRoomByHotelAndIdOutputPort;
    private final FindHotelByIdOutputPort findHotelByIdOutputPort;

    @Override
    public MostPopularRoomDTO handle(String hotelId) throws NotFoundException {
        Reservation reservation = getMostPopularRoomOutputPort.getMostPopular(hotelId)
            .orElseThrow(() -> new NotFoundException("No se ha encontrado ninguna habitacion"));

        HotelDTO foundHotel = findHotelByIdOutputPort.findById(reservation.getHotelId().toString())
            .orElseThrow(() -> new NotFoundException("El hotel no ha sido encontrado"));

        List<Reservation> reservations = findReservationsOutputPort.findReservations(null, reservation.getRoomId(), null, null, null);

        RoomDTO room = findRoomByHotelAndIdOutputPort.findByHotelAndId(
            reservation.getHotelId().toString(), reservation.getRoomId().toString())
            .orElseThrow(() -> new NotFoundException("No se han encontrado resultados"));

        return new MostPopularRoomDTO(foundHotel.getName(), room.getNumber(), reservations);
    }

}
