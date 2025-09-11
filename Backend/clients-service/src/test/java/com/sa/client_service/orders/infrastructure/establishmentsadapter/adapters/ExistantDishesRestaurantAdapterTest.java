package com.sa.client_service.orders.infrastructure.establishmentsadapter.adapters;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import com.sa.client_service.orders.application.dtos.ExistantDishesRestaurantDTO;

@ExtendWith(MockitoExtension.class)
public class ExistantDishesRestaurantAdapterTest {

    @Mock private RestClient restClient;
    @Mock private RestClient.RequestBodyUriSpec bodyUriSpec; // raw type for POST with body
    @Mock private RestClient.RequestBodySpec bodySpec; // returned by body()
    @Mock private RestClient.RequestHeadersSpec headersSpec; // after body()
    @Mock private RestClient.ResponseSpec responseSpec;

    private ExistantDishesRestaurantAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new ExistantDishesRestaurantAdapter(restClient);
        ReflectionTestUtils.setField(adapter, "ESTABLISHMENT_SERVICE_URL", "http://establishments");

        when(restClient.post()).thenReturn(bodyUriSpec);
        when(bodyUriSpec.uri(anyString(), any(Object[].class))).thenReturn(bodyUriSpec);
        when(bodyUriSpec.body(org.mockito.ArgumentMatchers.any(com.sa.client_service.orders.infrastructure.establishmentsadapter.dtos.ExistantDishesRestaurantRequest.class)))
            .thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void givenDishes_whenQuery_thenReturnsDtoFromBody() {
        ExistantDishesRestaurantDTO dto = new ExistantDishesRestaurantDTO();
        ResponseEntity<ExistantDishesRestaurantDTO> entity = ResponseEntity.ok(dto);
        when(responseSpec.toEntity(ExistantDishesRestaurantDTO.class)).thenReturn(entity);

        List<UUID> dishIds = List.of(UUID.randomUUID());
        ExistantDishesRestaurantDTO result = adapter.existantDishesRestaurant("RES-1", dishIds);

        assertSame(dto, result);
    }
}
