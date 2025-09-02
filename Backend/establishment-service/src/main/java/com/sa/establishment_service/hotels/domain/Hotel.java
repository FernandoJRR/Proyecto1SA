package com.sa.establishment_service.hotels.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.sa.establishment_service.restaurants.domain.Restaurant;
import com.sa.establishment_service.shared.domain.Establishment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hotel extends Establishment {
  private List<Room> rooms;
  private List<Restaurant> restaurants;

  private BigDecimal maintenanceCostPerWeek;

  public Hotel(UUID id, String name, String address, BigDecimal maintenanceCostPerWeek) {
    super(id, name, address);
    this.maintenanceCostPerWeek = maintenanceCostPerWeek;
  }

  public static Hotel create(String name, String address, BigDecimal maintenanceCostPerWeek) {
    return new Hotel(UUID.randomUUID(), name, address, maintenanceCostPerWeek);
  }
}
