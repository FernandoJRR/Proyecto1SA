package com.sa.client_service.reservations.application.outputports;

import java.time.LocalDate;
import java.util.UUID;

public interface CheckRoomAvailabilityOutputPort {
  public boolean isAvailable(UUID hotelId, UUID roomId, LocalDate startDate, LocalDate endDate);
}
