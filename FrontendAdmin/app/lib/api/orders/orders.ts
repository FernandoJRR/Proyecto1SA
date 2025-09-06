import type { Hotel, Room } from "../establishments/hotels";
import type { Restaurant } from "../establishments/restaurants";
import type { Entity } from "../utils/entity";
import type { PromotionApplied } from "../utils/promotion";

const CURRENT_ORDERS_URI = "/v1/orders";

export interface Order extends Entity {
    clientCui: string,
    restaurantId: string,
    restaurant: Restaurant,
    items: OrderItem[],
    total: number,
    subtotal: number,
    promotionApplied?: PromotionApplied
}

export interface OrderItem {
    dishId: string,
    name: string,
    quantity: number,
    price: number
}

/**
 * Manda a traer todos los hoteles disponibles en el sistema.
 * @param params
 * @returns
 */
export async function getAllOrders(params?: {}) {
  return await $api<Order[]>(`${CURRENT_ORDERS_URI}`, {
    params
  })
}

export async function getOrderById(reservation_id: string) {
  return await $api<Order>(`${CURRENT_ORDERS_URI}/${reservation_id}`);
}
export interface CreateOrderItemRequest {
    dishId: string;
    quantity: number;
  }

  export interface CreateOrderPayload {
    clientCui: string;
    restaurantId: string;
    promotionId?: string;
    createOrderItemRequests: CreateOrderItemRequest[];
    orderedAt: Date;
  }

  export async function createOrder(payload: CreateOrderPayload) {
    return await $api<Order>(`${CURRENT_ORDERS_URI}`, {
      method: "POST",
      body: payload,
    });
  }