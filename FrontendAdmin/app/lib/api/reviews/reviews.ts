import type { Entity } from "../utils/entity";

const CURRENT_REVIEWS_URI = "/v1/reviews";

export interface Review extends Entity {
    establishmentId: string,
    sourceId: string,
    comment: string,
    rating: number
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
export async function getReviews(params?: {}) {
  return await $api<Review[]>(`${CURRENT_REVIEWS_URI}`, {
    params
  })
}