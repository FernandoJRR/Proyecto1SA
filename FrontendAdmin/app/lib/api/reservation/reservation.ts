import type { PlayersGamePayload } from "../games/games";
import type { Entity } from "../utils/entity";

const CURRENT_RESERVATION_URI = "/v1/reservations";

export interface Reservation extends Entity {
  id: string;
  userId: string;
  gameId: string;
  startTime: string;
  endTime: string;
  customerFullName: string;
  customerNIT: string;
  date: string;
  notShow: boolean;
  paid: boolean;
}

export interface SpecReservation {
  id: string | null;
  userId: string | null;
  gameId: string | null;
  startTime: string | null;
  endTime: string | null;
  customerFullName: string | null;
  customerNIT: string | null;
  date: string | null;
  notShow: boolean | null;
  paid: boolean | null;
}

export interface CreateReservation {
  startTime: string;
  endTime: string;
  date: string;
  customerNIT: string;
  customerFullName: string;
  createInvoiceRequestDTO: BillingDetailsRequest
}

export interface BillingDetailsRequest {
  paymentMethod: string
  clientDocument: string
  details: DetailBilling[]
}

export interface DetailBilling {
  itemId: string
  itemName: string
  itemType: string
  quantity: number
  unitPrice: number

}

export interface CreateReservationOnline {
  startTime: string;
  endTime: string;
  date: string;
  customerNIT: string;
  customerFullName: string;
  players: PlayersGamePayload[]
}

export interface PayReservationPayload {
  reservationId: string
  createInvoiceRequestDTO: BillingDetailsRequest
}

export const createReservation = async (data: CreateReservation) => {
  const response = await $api<Reservation>(`${CURRENT_RESERVATION_URI}/presential`, {
    method: "POST",
    body: data,
  });
  return response;
};

export const createReservationOnline = async (data: CreateReservationOnline) => {
  const response = await $api<Reservation>(`${CURRENT_RESERVATION_URI}/online`, {
    method: "POST",
    body: data,
  });
  return response;
};

export const cancelReservation = async (id: string) => {
  const response = await $api<Reservation>(
    `${CURRENT_RESERVATION_URI}/cancel/${id}`,
    {
      method: "PATCH",
    }
  );
  return response;
};

export const payReservation = async (data: PayReservationPayload) => {
  const response = await $api<Reservation>(
    `${CURRENT_RESERVATION_URI}/pay`,
    {
      method: "PATCH",
      body: data
    }
  );
  return response;
};

export const getReservationById = async (id: string) => {
  const response = await $api<Reservation>(`${CURRENT_RESERVATION_URI}/${id}`, {
    method: "GET",
  });
  return response;
};

export const getAllReservations = async (spec: SpecReservation) => {
  const response = await $api<Reservation[]>(
    `${CURRENT_RESERVATION_URI}/all${genParams(spec)}`,
    {
      method: "GET",
    }
  );
  return response;
};

export const deleteReservation = async (id: string) => {
  const response = await $api<Reservation>(`${CURRENT_RESERVATION_URI}/${id}`, {
    method: "DELETE",
  });
  return response;
};
