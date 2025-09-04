import type { Hotel } from "../establishments/hotels";
import type { Restaurant } from "../establishments/restaurants";
import type { Entity } from "../utils/entity";
import type { PromotionApplied } from "../utils/promotion";

const CURRENT_PAYMENTS_URI = "/v1/payments";

export interface Payment extends Entity {
  establishmentId: string,
  clientId: string,
  sourceType: string,
  sourceId: string,
  subtotal: number,
  discount: number,
  total: number,
  method: string,
  status: string,
  cardNumber: string,
  promotionApplied?: PromotionApplied,
  hotel?: Hotel,
  restaurant?: Restaurant
}

/**
 * Manda a traer todos los hoteles disponibles en el sistema.
 * @param params
 * @returns
 */
export async function getAllPayments(params?: {}) {
  return await $api<Payment[]>(`${CURRENT_PAYMENTS_URI}`, {
    params
  })
}

export async function getPaymentById(payment_id: string) {
  return await $api<Payment>(`${CURRENT_PAYMENTS_URI}/${payment_id}`);
}