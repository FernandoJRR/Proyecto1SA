import type { string } from "zod";
import type { Hotel, Room } from "../establishments/hotels";
import type { Entity } from "../utils/entity";
import type { PromotionApplied } from "../utils/promotion";

const CURRENT_RESERVATIONS_URI = "/v1/reservations/public";

export interface Reservation extends Entity {
  clientCui: string,
  hotelId: string,
  roomId: string,
  startDate: Date,
  endDate: Date,
  totalCost: number,
  subtotal: number,
  promotionApplied?: PromotionApplied
  hotel: Hotel,
  room: Room
}

/**
 * Manda a traer todos los hoteles disponibles en el sistema.
 * @param params
 * @returns
 */
export async function getAllReservationsClient(clientCui: string, params?: {}) {
  return await $api<Reservation[]>(`${CURRENT_RESERVATIONS_URI}/by-cui/${clientCui}`, {
    params
  })
}

export async function getReservationById(reservation_id: string) {
  return await $api<Reservation>(`${CURRENT_RESERVATIONS_URI}/${reservation_id}`);
}

function formatDateYMD(d: Date | null): string {
    if (!d) return '';
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${y}-${m}-${day}`;
  }

export async function getAvailableRooms(start_date: Date, end_date: Date, hotel_id?: string) {
  return await $api<Room[]>(`${CURRENT_RESERVATIONS_URI}/availability`, {
    params: {
        hotelId: hotel_id,
        startDate: formatDateYMD(start_date),
        endDate: formatDateYMD(end_date)
    }
  });
}


export interface CreateReservationPayload {
    clientCui: string;
    hotelId: string;
    roomId: string;
    startDate: string;
    endDate: string;
    promotionId?: string;
  }

  export async function createReservation(payload: CreateReservationPayload) {
    return await $api<Reservation>(`${CURRENT_RESERVATIONS_URI}`, {
      method: 'POST',
      body: payload,
    });
  }