import type { Entity } from "../utils/entity";
import type { Restaurant } from "./restaurants";

const CURRENT_HOTELS_URI = "/v1/hotels";

export interface Hotel extends Entity {
  name: string;
  address: string;
  maintenanceCostPerWeek: number;
}

export interface Room extends Entity {
  name: string;
  capacity: number;
  pricePerNight: number;
  status?: 'AVAILABLE' | 'OCCUPIED' | 'MAINTENANCE' | string;
}

/**
 * Manda a traer todas las habitaciones de un hotel por su id.
 */
export async function getHotelRooms(hotel_id: string) {
  return await $api<Room[]>(`${CURRENT_HOTELS_URI}/${hotel_id}/rooms`);
}

/**
 * Manda a traer todos los hoteles disponibles en el sistema.
 * @param params
 * @returns
 */
export async function getAllHotels(params?: {}) {
  return await $api<Hotel[]>(`${CURRENT_HOTELS_URI}`, {
    params
  })
}

export async function getHotelById(hotel_id: string) {
  return await $api<Hotel>(`${CURRENT_HOTELS_URI}/${hotel_id}`);
}

/**
 * Manda a traer todos los restaurantes pertenecientes a un hotel específico.
 */
export async function getRestaurantsByHotelId(hotel_id: string) {
  return await $api<Restaurant[]>(`/v1/hotels/${hotel_id}/restaurants`)
}

export interface CreateHotelPayload {
  name: string;
  address: string;
  maintenanceCostPerWeek: number;
}

/**
 * Crea un hotel nuevo.
 */
export async function createHotel(payload: CreateHotelPayload) {
  return await $api<Hotel>(`${CURRENT_HOTELS_URI}`, {
    method: 'POST',
    body: payload,
  })
}

export interface CreateRoomPayload {
  number: string;
  capacity: number;
  pricePerNight: number;
  status?: 'AVAILABLE' | 'OCCUPIED' | 'MAINTENANCE' | string;
}

/**
 * Crea una habitación para un hotel específico.
 */
export async function createHotelRoom(hotel_id: string, payload: CreateRoomPayload) {
  return await $api<Room>(`${CURRENT_HOTELS_URI}/${hotel_id}`, {
    method: 'POST',
    body: payload,
  })
}

export async function getHotelRoom(hotel_id: string, room_id: string) {
  return await $api<Room>(`${CURRENT_HOTELS_URI}/${hotel_id}/rooms/${room_id}`, {
    method: 'GET',
  })
}