import type { Entity } from "../utils/entity";

const CURRENT_INVOICE_URI = "/v1/invoices";

export enum InvoiceItemType {
  GOOD = "GOOD",
  SERVICE = "SERVICE",
}

export enum InvoicePaymentMethod {
  CASH = "CASH",
  CARD = "CARD",
  ONLINE = "ONLINE",
}

export interface InvoiceDetail extends Entity {
  itemId: string;
  itemName: string;
  itemType: string;
  quantity: number;
  unitPrice: number;
  total: number;
}

export interface Invoice extends Entity {
  paymentMethod: InvoicePaymentMethod;
  subtotal: number;
  tax: number;
  total: number;
  clientDocument: string;
  details: InvoiceDetail[];
}

export interface CreateInvoiceDetail {
  itemId: string;
  itemName: string;
  itemType: InvoiceItemType;
  quantity: number;
  unitPrice: number;
}

export interface ModelCreateInvoice {
  paymentMethod: string;
  clientDocument: string;
  details: InvoiceDetail[];
}
export interface CreateInvoice {
  paymentMethod: InvoicePaymentMethod;
  clientDocument: string;
  details: CreateInvoiceDetail[];
}

export interface SpecInvoice {
  paymentMethod: string | null;
  clientDocument: string | null;
}

export interface ItemType {
  itemType: string;
  name: string;
}

export interface PaymentMethod {
  paymentMethod: string | null;
  name: string;
}

export const mapModelCreateInvoiceToCreateInvoice = (
  model: ModelCreateInvoice
): CreateInvoice => {
  return {
    paymentMethod: model.paymentMethod as InvoicePaymentMethod,
    clientDocument: model.clientDocument,
    details: model.details.map((detail) => ({
      itemId: detail.itemId,
      itemName: detail.itemName,
      itemType: detail.itemType as InvoiceItemType,
      quantity: detail.quantity,
      unitPrice: detail.unitPrice,
    })),
  };
};

/**
 * Este método se encarga de crear una factura y reconoce el warehouse del usuario que la crea
 * @param data
 * @returns
 */
export const createInvoice = async (data: CreateInvoice) => {
  const response = await $api<Invoice>(`${CURRENT_INVOICE_URI}`, {
    method: "POST",
    body: data,
  });
  return response;
};

export const createInvoiceByWarehouseId = async (
  data: CreateInvoice,
  warehouseId: string
) => {
  const response = await $api<Invoice>(`${CURRENT_INVOICE_URI}/warehouse/${warehouseId}`, {
    method: "POST",
    body: data,
  });
  return response;
};

export const getInvoice = async (id: string) => {
  const response = await $api<Invoice>(`${CURRENT_INVOICE_URI}/${id}`, {
    method: "GET",
  });
  return response;
};

export const getInvoiceByClientDocument = async (clientDocument: string) => {
  const response = await $api<Invoice[]>(
    `${CURRENT_INVOICE_URI}/client/${clientDocument}`,
    {
      method: "GET",
    }
  );
  return response;
};

export const getAllInvoices = async (data: SpecInvoice) => {
  const response = await $api<Invoice[]>(
    `${CURRENT_INVOICE_URI}/all${genParams(data)}`,
    {
      method: "GET",
    }
  );
  return response;
};

export const getItemTypes = async () => {
  const response = await $api<ItemType[]>(`${CURRENT_INVOICE_URI}/item-types`, {
    method: "GET",
  });
  return response;
};

export const getPaymentMethods = async () => {
  const response = await $api<PaymentMethod[]>(
    `${CURRENT_INVOICE_URI}/payment-methods`,
    {
      method: "GET",
    }
  );
  return response;
};
