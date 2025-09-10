import type { Entity } from "../utils/entity";

const CURRENT_CLIENT_URI = "/v1/clients/public";

export interface Client extends Entity {
  firstName: string;
  lastName: string;
  email: string;
  cui: string;
}

export interface CreateClientPayload {
  firstName: string;
  lastName: string;
  email: string;
  cui: string;
}

export async function getClientByCui(client_cui: string) {
  return await $api<Client>(`${CURRENT_CLIENT_URI}/by-cui/${client_cui}`);
}

export async function createClient(payload: CreateClientPayload) {
  return await $api<Client>(`${CURRENT_CLIENT_URI}`, {
    method: 'POST',
    body: payload,
  })
}