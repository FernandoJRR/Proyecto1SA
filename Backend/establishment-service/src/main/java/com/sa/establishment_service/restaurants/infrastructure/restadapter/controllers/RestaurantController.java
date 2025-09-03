package com.sa.establishment_service.restaurants.infrastructure.restadapter.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sa.establishment_service.restaurants.application.dtos.CreateDishDTO;
import com.sa.establishment_service.restaurants.application.dtos.CreateRestaurantDTO;
import com.sa.establishment_service.restaurants.application.dtos.ExistDishesDTO;
import com.sa.establishment_service.restaurants.application.dtos.ExistDishesResultDTO;
import com.sa.establishment_service.restaurants.application.inputports.CreateDishInputPort;
import com.sa.establishment_service.restaurants.application.inputports.CreateRestaurantInputPort;
import com.sa.establishment_service.restaurants.application.inputports.ExistDishesRestaurantInputPort;
import com.sa.establishment_service.restaurants.application.inputports.ExistsRestaurantByIdInputPort;
import com.sa.establishment_service.restaurants.application.inputports.FindAllRestaurantsInputPort;
import com.sa.establishment_service.restaurants.application.inputports.FindDishesByRestaurantInputPort;
import com.sa.establishment_service.restaurants.application.inputports.FindRestaurantByIdInputPort;
import com.sa.establishment_service.restaurants.application.inputports.FindRestaurantsByHotelInputPort;
import com.sa.establishment_service.restaurants.domain.Dish;
import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos.CreateDishRequest;
import com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos.CreateRestaurantRequest;
import com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos.DishResponse;
import com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos.ExistsDishesRequest;
import com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos.ExistsDishesResponse;
import com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos.FilterDishesRequest;
import com.sa.establishment_service.restaurants.infrastructure.restadapter.dtos.RestaurantResponse;
import com.sa.establishment_service.restaurants.infrastructure.restadapter.mappers.DishRestMapper;
import com.sa.establishment_service.restaurants.infrastructure.restadapter.mappers.RestaurantRestMapper;
import com.sa.shared.exceptions.DuplicatedEntryException;
import com.sa.shared.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantRestMapper restaurantRestMapper;
    private final DishRestMapper dishRestMapper;
    private final CreateRestaurantInputPort createRestaurantInputPort;
    private final ExistsRestaurantByIdInputPort existsRestaurantByIdInputPort;
    private final ExistDishesRestaurantInputPort existDishesRestaurantInputPort;
    private final CreateDishInputPort createDishInputPort;
    private final FindAllRestaurantsInputPort findAllRestaurantsInputPort;
    private final FindRestaurantByIdInputPort findRestaurantByIdInputPort;
    private final FindRestaurantsByHotelInputPort findRestaurantsByHotelInputPort;
    private final FindDishesByRestaurantInputPort findDishesByRestaurantInputPort;

    @Operation(summary = "Crear un nuevo restaurante", description = "Este endpoint permite la creación de un nuevo restaurante en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hotel creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_RESTAURANT')")
    public ResponseEntity<RestaurantResponse> createRestaurant(
            @RequestBody CreateRestaurantRequest request)
            throws NotFoundException {

        CreateRestaurantDTO createRestaurantDTO = restaurantRestMapper.toDTO(request);

        Restaurant result = createRestaurantInputPort.handle(createRestaurantDTO);

        RestaurantResponse response = restaurantRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
    summary = "Obtener todos los restaurantes",
    description = "Obtener una lista de todos los restaurantes registrados."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de restaurantes",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(
                    schema = @Schema(implementation = RestaurantResponse.class)
                )
            )
        ),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping({"","/"})
    public ResponseEntity<List<RestaurantResponse>> getHotels() {
        List<Restaurant> result = findAllRestaurantsInputPort.handle();

        List<RestaurantResponse> response = restaurantRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "Obtener un restaurante", description = "Este endpoint permite obtener un restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurante obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "El restaurante no fue encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> getRestaurant(
            @PathVariable("restaurantId") String restaurantId)
            throws NotFoundException {

        Restaurant result = findRestaurantByIdInputPort.handle(restaurantId);

        RestaurantResponse response = restaurantRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Obtener los restaurantes en un hotel", description = "Este endpoint permite obtener los restaurantes dentro de un hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurantes obtenido exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/by-hotel/{hotelId}")
    public ResponseEntity<List<RestaurantResponse>> getRestaurantByHotel(
            @PathVariable("hotelId") String hotelId)
            throws NotFoundException {

        List<Restaurant> result = findRestaurantsByHotelInputPort.handle(hotelId);

        List<RestaurantResponse> response = restaurantRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Detecta si un restaurante existe", description = "Este endpoint permite detectar si un restaurante existe por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Detectado exitosamente"),
            @ApiResponse(responseCode = "404", description = "El restaurante no fue encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{restaurantId}/exists")
    public ResponseEntity<Void> existsRestaurant(
            @PathVariable("restaurantId") String restaurantId)
            throws NotFoundException {

        existsRestaurantByIdInputPort.handle(restaurantId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Crear un nuevo platillo", description = "Este endpoint permite la creación de un nuevo platillo en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Platillo creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{restaurantId}/dishes")
    public ResponseEntity<List<DishResponse>> getDishesRestaurant(
            @PathVariable("restaurantId") String restaurantId)
            throws NotFoundException {

        List<Dish> result = findDishesByRestaurantInputPort.handle(restaurantId);

        List<DishResponse> response = dishRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Crear un nuevo platillo", description = "Este endpoint permite la creación de un nuevo platillo en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Platillo creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, usualmente por error en la validacion de parametros.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/{restaurantId}/dishes")
    @PreAuthorize("hasAuthority('CREATE_DISH')")
    public ResponseEntity<DishResponse> createDish(
            @PathVariable("restaurantId") String restaurantId,
            @RequestBody CreateDishRequest request)
            throws NotFoundException {

        CreateDishDTO createDishDTO = dishRestMapper.toDTO(request);

        Dish result = createDishInputPort.handle(restaurantId, createDishDTO);

        DishResponse response = dishRestMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{restaurantId}/dishes/query")
    public ResponseEntity<ExistsDishesResponse> verify(
            @PathVariable UUID restaurantId,
            @RequestBody ExistsDishesRequest request) {

        ExistDishesDTO dto = new ExistDishesDTO();
        dto.setDishIds(request.getDishIds());
        dto.setRestaurantId(restaurantId);

        ExistDishesResultDTO result = existDishesRestaurantInputPort.handle(dto);
        List<DishResponse> dishes = dishRestMapper.toResponse(result.getPresentDishes());
        return ResponseEntity.status(HttpStatus.OK).body(new ExistsDishesResponse(result.isAllPresent(), result.getMissingIds(), dishes));
    }
}
