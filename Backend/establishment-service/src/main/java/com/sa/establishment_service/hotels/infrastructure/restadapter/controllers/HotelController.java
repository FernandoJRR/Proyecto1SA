package com.sa.establishment_service.hotels.infrastructure.restadapter.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.print.attribute.standard.Media;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sa.establishment_service.hotels.application.dtos.CreateHotelDTO;
import com.sa.establishment_service.hotels.application.dtos.CreateRoomDTO;
import com.sa.establishment_service.hotels.application.inputports.CreateHotelInputPort;
import com.sa.establishment_service.hotels.application.inputports.CreateRoomInputPort;
import com.sa.establishment_service.hotels.application.inputports.ExistsHotelByIdInputPort;
import com.sa.establishment_service.hotels.application.inputports.ExistsRoomByIdInputPort;
import com.sa.establishment_service.hotels.application.inputports.ExistsRoomInHotelByIdInputPort;
import com.sa.establishment_service.hotels.application.inputports.FindAllHotelsInputPort;
import com.sa.establishment_service.hotels.application.inputports.FindAllRoomsByHotelIdInputPort;
import com.sa.establishment_service.hotels.application.inputports.FindHotelByIdInputPort;
import com.sa.establishment_service.hotels.application.inputports.FindRoomByHotelAndIdInputPort;
import com.sa.establishment_service.hotels.domain.Hotel;
import com.sa.establishment_service.hotels.domain.Room;
import com.sa.establishment_service.hotels.infrastructure.restadapter.dtos.CreateHotelRequest;
import com.sa.establishment_service.hotels.infrastructure.restadapter.dtos.CreateRoomRequest;
import com.sa.establishment_service.hotels.infrastructure.restadapter.dtos.HotelResponse;
import com.sa.establishment_service.hotels.infrastructure.restadapter.dtos.RoomResponse;
import com.sa.establishment_service.hotels.infrastructure.restadapter.mappers.HotelRestMapper;
import com.sa.establishment_service.hotels.infrastructure.restadapter.mappers.RoomRestMapper;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/hotels")

@RequiredArgsConstructor
public class HotelController {
    private final CreateHotelInputPort createHotelInputPort;
    private final CreateRoomInputPort createRoomInputPort;
    private final FindAllHotelsInputPort findAllHotelsInputPort;
    private final ExistsRoomByIdInputPort existsRoomByIdInputPort;
    private final FindAllRoomsByHotelIdInputPort findAllRoomsByHotelIdInputPort;
    private final ExistsRoomInHotelByIdInputPort existsRoomInHotelByIdInputPort;
    private final FindRoomByHotelAndIdInputPort findRoomByHotelAndIdInputPort;
    private final ExistsHotelByIdInputPort existsHotelByIdInputPort;
    private final FindHotelByIdInputPort findHotelByIdInputPort;

    private final HotelRestMapper hotelRestMapper;
    private final RoomRestMapper roomRestMapper;

    @Operation(summary = "Crear un nuevo hotel", description = "Este endpoint permite la creación de un nuevo hotel en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hotel creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HotelResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_HOTEL')")
    public ResponseEntity<HotelResponse> createHotel(
            @RequestBody CreateHotelRequest request)
            throws DuplicatedEntryException {

        CreateHotelDTO createHotelDTO = hotelRestMapper.toDTO(request);

        Hotel result = createHotelInputPort.handle(createHotelDTO);

        HotelResponse response = hotelRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
    summary = "Obtener todos los hoteles",
    description = "Obtener una lista de todos los hoteles registrados."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de hoteles",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(
                    schema = @Schema(implementation = HotelResponse.class)
                )
            )
        ),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping({"","/"})
    public ResponseEntity<List<HotelResponse>> getHotels() {
        List<Hotel> result = findAllHotelsInputPort.handle();

        List<HotelResponse> response = hotelRestMapper.toResponseList(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Crear una nueva habitacion de un hotel", description = "Este endpoint permite la creación de una nueva habitacion en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hotel creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/{hotelId}")
    @PreAuthorize("hasAuthority('CREATE_ROOM')")
    public ResponseEntity<RoomResponse> createRoom(
            @PathVariable("hotelId") String hotelId,
            @RequestBody CreateRoomRequest request)
            throws DuplicatedEntryException, NotFoundException {

        CreateRoomDTO createRoomDTO = roomRestMapper.toDTO(request);
        createRoomDTO.setHotelId(UUID.fromString(hotelId));

        Room result = createRoomInputPort.handle(createRoomDTO);

        RoomResponse response = roomRestMapper.toResponse(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener un hotel", description = "Este endpoint permite obtener un hotel del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "El hotel no fue encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelResponse> getHotel(
            @PathVariable("hotelId") String hotelId)
            throws NotFoundException {

        Hotel foundHotel = findHotelByIdInputPort.handle(hotelId);

        HotelResponse response = hotelRestMapper.toResponse(foundHotel);

        return ResponseEntity.status(HttpStatus.OK).body(response);
}


    @Operation(summary = "Detecta si un hotel existe", description = "Este endpoint permite detectar si un hotel existe por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Detectado exitosamente"),
            @ApiResponse(responseCode = "404", description = "El hotel no fue encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{hotelId}/exists")
    public ResponseEntity<Void> existsHotel(
            @PathVariable("hotelId") String hotelId)
            throws NotFoundException {

        existsHotelByIdInputPort.handle(hotelId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Detecta si una habitacion existe por su ID", description = "Este endpoint permite conocer si una habitacion existe.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Habitacion existe", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "404", description = "Habitacion no existe", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(value="/rooms/{roomId}/exists",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Boolean>> existsRoom(
            @PathVariable("roomId") String roomId) throws NotFoundException {
        existsRoomByIdInputPort.handle(roomId);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("exists", true));

    }

    @Operation(summary = "Detecta si una habitacion existe dentro de un hotel por su ID", description = "Este endpoint permite conocer si una habitacion existe dentro de un hotel.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Habitacion existe", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "404", description = "Habitacion no existe", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(value="/{hotelId}/rooms/{roomId}/exists",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> existsRoom(
            @PathVariable("hotelId") String hotelId,
            @PathVariable("roomId") String roomId
            ) throws NotFoundException {
        existsRoomInHotelByIdInputPort.handle(hotelId,roomId);

        return ResponseEntity.noContent().build();

    }

    @Operation(summary = "Devuelve todas las habitaciones de un hotel", description = "Este endpoint permite conocer las habitaciones de un hotel.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Habitaciones devueltas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{hotelId}/rooms")
    public ResponseEntity<List<RoomResponse>> getRoomsByHotel(
            @PathVariable("hotelId") String hotelId) throws NotFoundException {
        List<Room> result = findAllRoomsByHotelIdInputPort.handle(hotelId);
        List<RoomResponse> response = roomRestMapper.toResponse(result);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Devuelve una habitacion de un hotel especificado.", description = "Este endpoint permite conocer una habitacion de un hotel encontrado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Habitaciones devueltas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{hotelId}/rooms/{roomId}")
    public ResponseEntity<RoomResponse> getRoomByHotelAndId(
            @PathVariable("hotelId") String hotelId,
            @PathVariable("roomId") String roomId) throws NotFoundException {
        Room result = findRoomByHotelAndIdInputPort.handle(hotelId, roomId);
        RoomResponse response = roomRestMapper.toResponse(result);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
