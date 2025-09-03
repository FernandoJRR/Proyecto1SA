import type { Entity } from "../utils/entity";

const CURRENT_RESTAURANTS_URI = "/v1/restaurants";

export interface Restaurant extends Entity {
  name: string;
  address: string;
  hotelId?: string;
}

/**
 * Manda a traer todos los restaurantes disponibles en el sistema.
 * @param params
 * @returns
 */
export async function getAllRestaurants(params?: {}) {
  return await $api<Restaurant[]>(`${CURRENT_RESTAURANTS_URI}`, {
    params
  })
}

export async function getRestaurantById(restaurant_id: string) {
  return await $api<Restaurant>(`${CURRENT_RESTAURANTS_URI}/${restaurant_id}`);
}

/**
 * Manda a traer todos los restaurantes pertenecientes a un hotel específico.
 */
export async function getRestaurantsByHotelId(hotel_id: string) {
  return await $api<Restaurant[]>(`/v1/restaurants/by-hotel/${hotel_id}`)
}

export interface Dish extends Entity {
  name: string;
  price: number;
  description?: string;
  available?: boolean;
  category?: string;
}

/**
 * Manda a traer todos los platillos (dishes) de un restaurante por su id.
 */
export async function getRestaurantDishes(restaurant_id: string) {
  return await $api<Dish[]>(`${CURRENT_RESTAURANTS_URI}/${restaurant_id}/dishes`)
}