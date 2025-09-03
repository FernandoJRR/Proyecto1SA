import type { Entity } from "../utils/entity";
import type { Permission } from "./permission";

const CURRENT_PAYMENT_TYPE_URI = "/v1/payment-types";

export interface PaymentType extends Entity {
  type: string;
}

export async function getAllPaymentTypes() {
  return await $api<PaymentType[]>(`${CURRENT_PAYMENT_TYPE_URI}`);
}
