package com.sa.client_service.reservations.infrastructure.establishmentsadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.sa.client_service.reservations.application.dtos.RoomDTO;

@ExtendWith(MockitoExtension.class)
public class FindAllRoomsAdapterTest {

    @Mock private RestClient restClient;
    @Mock private RestClient.RequestHeadersUriSpec uriSpec; // use raw to avoid generic capture issues
    @Mock private RestClient.RequestHeadersSpec headersSpec; // use raw to avoid generic capture issues
    @Mock private RestClient.ResponseSpec responseSpec;

    private FindAllRoomsAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new FindAllRoomsAdapter(restClient);
        ReflectionTestUtils.setField(adapter, "ESTABLISHMENT_SERVICE_URL", "http://establishments");

        when(restClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void givenSuccess_whenFindAll_thenReturnsList() {
        RoomDTO r1 = new RoomDTO(); r1.setId("R-1");
        RoomDTO r2 = new RoomDTO(); r2.setId("R-2");
        when(responseSpec.body(any(ParameterizedTypeReference.class)))
            .thenReturn(List.of(r1, r2));

        List<RoomDTO> result = adapter.findAll();

        assertEquals(2, result.size());
        assertEquals("R-1", result.get(0).getId());
    }

    @Test
    void givenHttpError_whenFindAll_thenReturnsEmptyList() {
        when(responseSpec.body(any(ParameterizedTypeReference.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.SERVICE_UNAVAILABLE));

        List<RoomDTO> result = adapter.findAll();

        assertTrue(result.isEmpty());
    }
}
