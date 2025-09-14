package com.sa.employee_service.employees.infrastructure.establishmentsadapter.adapters;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;

public class ExistsHotelByIdAdapterTest {

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

    private ExistsHotelByIdAdapter adapter;
    private String establishmentsUrl = "http://establishments";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new ExistsHotelByIdAdapter(restClient);
        // set the private field ESTABLISHMENT_SERVICE_URL
        org.springframework.test.util.ReflectionTestUtils.setField(adapter, "ESTABLISHMENT_SERVICE_URL", establishmentsUrl);

        when(restClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) uriSpec);
        when(uriSpec.uri(eq(establishmentsUrl + "/api/v1/hotels/{hotelId}/exists"), anyString()))
            .thenReturn((RestClient.RequestHeadersUriSpec) uriSpec);
        when(uriSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void existsById_whenHotelExists_returnsTrue() {
        String hotelId = "HOTEL-1";
        // stub toBodilessEntity to succeed
        doReturn(null).when(responseSpec).toBodilessEntity();

        boolean result = adapter.existsById(hotelId);

        assertTrue(result);
    }

    @Test
    void existsById_whenHotelNotFound_returnsFalse() {
        String hotelId = "HOTEL-NOT-FOUND";
        // throw NotFound
        doThrow(HttpClientErrorException.NotFound.create(
            HttpStatus.NOT_FOUND, "Not Found", null, null, null
        )).when(responseSpec).toBodilessEntity();

        boolean result = adapter.existsById(hotelId);

        assertFalse(result);
    }

    @Test
    void existsById_whenOtherHttpError_throwsException() {
        String hotelId = "HOTEL-ERROR";
        // throw other HTTP error
        doThrow(HttpClientErrorException.create(
            HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", null, null, null
        )).when(responseSpec).toBodilessEntity();

        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, () -> {
            adapter.existsById(hotelId);
        });

        assertTrue(ex.getMessage().contains("500"));
    }
}