import type { Entity } from "../utils/entity";

const CURRENT_PROMOTIONS_URI = "/v1/promotions/public";

export interface Promotion extends Entity {
  percentage: number,
  startDate: Date,
  endDate: Date,
  promotionType: PromotionType
  name: string,
  establishmentId: string,
  establishmentType: string,
  topCount: number,
}

export interface PromotionType {
    code: string,
    name: string
}

/**
 * Manda a traer todos los hoteles disponibles en el sistema.
 * @param params
 * @returns
 */
export async function getAllPromotions(params?: {}) {
  return await $api<Promotion[]>(`${CURRENT_PROMOTIONS_URI}`, {
    params
  })
}

export async function getPromotionTypes() {
  return await $api<PromotionType[]>(`${CURRENT_PROMOTIONS_URI}/types`)
}

export async function getPromotionById(promotion_id: string) {
  return await $api<Promotion>(`${CURRENT_PROMOTIONS_URI}/${promotion_id}`);
}

export interface CreatePromotionPayload {
    percentage: number;
    startDate: string;
    endDate: string;
    promotionType: string;
    establishmentId: string;
    establishmentType: "HOTEL" | "RESTAURANT";
    name: string;
    topCount?: number;
  }

  export async function createPromotion(payload: CreatePromotionPayload) {
    return await $api<Promotion>(`${CURRENT_PROMOTIONS_URI}`, {
      method: "POST",
      body: payload,
    });
}

export interface ReservationPromotionEligibilityPayload {
  clientId: string;
  hotelId: string;
  roomId: string;
  startDate: string; // YYYY-MM-DD
  endDate: string;   // YYYY-MM-DD
}

export interface OrderPromotionEligibilityPayload {
  clientId: string;
  restaurantId: string;
  dishesIds: string[];
  orderedAt: string; // YYYY-MM-DD
}

export async function checkReservationPromotionEligibility(
  payload: ReservationPromotionEligibilityPayload
) {
  return await $api<Promotion>(`${CURRENT_PROMOTIONS_URI}/eligibility/reservation`, {
    method: 'POST',
    body: payload,
  });
}