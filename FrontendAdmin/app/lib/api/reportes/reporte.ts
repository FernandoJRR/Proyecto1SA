import type { Payment } from "../payments/payments";

const CURRENT_REPORTS_ESTABLISHMENTS_URI = "/v1/establishments/reports";
const CURRENT_REPORTS_CLIENTS_URI = "/v1/clients/reports";

function formatISODate(date?: Date | null): string | null {
    if (!date) return null;
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

export async function getIncomeReport(
    establishmentId: string,
    establishmentType: string,
  startDate?: Date | null,
  endDate?: Date | null
) {
  return await $api<any>(
    `${CURRENT_REPORTS_ESTABLISHMENTS_URI}/income/${establishmentType}/${establishmentId}`, {
    query: {
        fromDate: formatISODate(startDate),
        toDate: formatISODate(endDate)
    },
    method: "GET",
  });
}

export async function getIncomeClientReport(
    clientCui: string,
    establishmentId: string,
  startDate?: Date | null,
  endDate?: Date | null
) {
  return await $api<any>(
    `${CURRENT_REPORTS_CLIENTS_URI}/income/${clientCui}`, {
    query: {
        establishmentId,
        fromDate: formatISODate(startDate),
        toDate: formatISODate(endDate)
    },
    method: "GET",
  });
}