import type { Entity } from "../utils/entity";

const CURRENT_REVIEWS_URI = "/v1/reviews/public";

export interface Review extends Entity {
    establishmentId: string,
    establishmentType?: string,
    sourceId: string,
    comment: string,
    rating: number,
    clientCui?: string,
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
  return await $api<Review[]>(`${CURRENT_REVIEWS_URI}/search`, {
    params
  })
}

export interface CreateReviewPayload {
  clientCui: string;
  establishmentId: string;
  establishmentType: "HOTEL" | "RESTAURANT";
  sourceId: string;
  comment: string;
  rating: number; // 1..5
}

export async function createReview(payload: CreateReviewPayload) {
  return await $api<Review>(`${CURRENT_REVIEWS_URI}` , {
    method: 'POST',
    body: payload,
  });
}
