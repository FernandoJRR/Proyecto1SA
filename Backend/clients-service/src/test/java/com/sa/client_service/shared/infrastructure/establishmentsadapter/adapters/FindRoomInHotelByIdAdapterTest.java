package com.sa.client_service.shared.infrastructure.establishmentsadapter.adapters;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import com.sa.client_service.reservations.application.dtos.RoomDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

class FindRoomInHotelByIdAdapterTest {

    @Mock
    private RestClient restClient;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Mock
    private RestClient.RequestHeadersUriSpec uriSpec;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Mock
    private RestClient.RequestHeadersSpec headersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private FindRoomInHotelByIdAdapter adapter;
    private String establishmentsServiceUrl = "http://dummy-establishments";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new FindRoomInHotelByIdAdapter(restClient);
        TestUtils.setField(adapter, "ESTABLISHMENT_SERVICE_URL", establishmentsServiceUrl);

        when(restClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) uriSpec);
        when(uriSpec.uri(
            eq(establishmentsServiceUrl + "/api/v1/hotels/{hotelId}/rooms/{roomId}"),
            anyString(), anyString()
        )).thenReturn((RestClient.RequestHeadersUriSpec) uriSpec);
        when(uriSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void findByHotelAndId_whenRoomExists_returnsOptionalWithRoomDTO() {
        String hotelId = "hotel123";
        String roomId = "room456";

        // Create dummy RoomDTO
        RoomDTO dto = new RoomDTO();
        // set fields
        dto.setId(roomId);
        dto.setHotelId(hotelId);
        dto.setNumber("Room 1");
        dto.setCapacity(2);  // assuming it has capacity
        // (you may need to set more fields if RoomDTO has more)

        when(responseSpec.toEntity(RoomDTO.class)).thenReturn(ResponseEntity.ok(dto));

        Optional<RoomDTO> result = adapter.findByHotelAndId(hotelId, roomId);

        assertTrue(result.isPresent());
        assertEquals(roomId, result.get().getId());
        assertEquals(hotelId, result.get().getHotelId());
        assertEquals("Room 1", result.get().getNumber());
    }

    @Test
    void findByHotelAndId_whenNotFound_returnsEmptyOptional() {
        String hotelId = "hotel123";
        String roomId = "roomNotFound";

        doThrow(HttpClientErrorException.NotFound.create(
            HttpStatus.NOT_FOUND, "Not Found", null, null, null
        )).when(responseSpec).toEntity(RoomDTO.class);

        Optional<RoomDTO> result = adapter.findByHotelAndId(hotelId, roomId);

        assertFalse(result.isPresent());
    }
}