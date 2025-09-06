import type { string } from "zod";
import type { Hotel, Room } from "../establishments/hotels";
import type { Entity } from "../utils/entity";
import type { PromotionApplied } from "../utils/promotion";

const CURRENT_RESERVATIONS_URI = "/v1/reservations";

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
export async function getAllReservations(params?: {}) {
  return await $api<Reservation[]>(`${CURRENT_RESERVATIONS_URI}`, {
    params
  })
}

export async function getReservationById(reservation_id: string) {
  return await $api<Reservation>(`${CURRENT_RESERVATIONS_URI}/${reservation_id}`);
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