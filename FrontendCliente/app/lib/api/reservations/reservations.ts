import type { string } from "zod";
import type { Hotel, Room } from "../establishments/hotels";
import type { Entity } from "../utils/entity";
import type { PromotionApplied } from "../utils/promotion";

const CURRENT_RESERVATIONS_URI = "/v1/reservations/public";

export interface Reservation extends Entity {
  clientCui: string,
  hotelId: string,
  roomId: string,
  startDate: Date,
  endDate: Date,
  totalCost: number,
  subtotal: number,
  promotionApplied?: PromotionApplied
  hotel: Hotel,
  room: Room
}

/**
 * Manda a traer todos los hoteles disponibles en el sistema.
 * @param params
 * @returns
 */
export async function getAllReservationsClient(clientCui: string, params?: {}) {
  return await $api<Reservation[]>(`${CURRENT_RESERVATIONS_URI}/by-cui/${clientCui}`, {
    params
  })
}

export async function getReservationById(reservation_id: string) {
  return await $api<Reservation>(`${CURRENT_RESERVATIONS_URI}/${reservation_id}`);
}

function formatDateYMD(d: Date | null): string {
    if (!d) return '';
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${y}-${m}-${day}`;
  }

export async function getAvailableRooms(start_date: Date, end_date: Date, hotel_id?: string) {
  return await $api<Room[]>(`${CURRENT_RESERVATIONS_URI}/availability`, {
    params: {
        hotelId: hotel_id,
        startDate: formatDateYMD(start_date),
        endDate: formatDateYMD(end_date)
    }
  });
}


export interface CreateReservationPayload {
    clientCui: string;
    hotelId: string;
    roomId: string;
    startDate: string;
    endDate: string;
    promotionId?: string;
  }

  export async function createReservation(payload: CreateReservationPayload) {
    return await $api<Reservation>(`${CURRENT_RESERVATIONS_URI}`, {
      method: 'POST',
      body: payload,
    });
  }

// ===== PDF Exports (client-only) =====
type PdfMakeType = typeof import('pdfmake/build/pdfmake') & { vfs?: any }

async function getPdfMake(): Promise<PdfMakeType | null> {
  try {
    if (typeof window === 'undefined') return null
    const pdfMakeMod = await import('pdfmake/build/pdfmake')
    const pdfFonts = await import('pdfmake/build/vfs_fonts')
    // @ts-ignore - pdfmake typings can be loose here
    ;(pdfMakeMod as any).vfs = (pdfFonts as any).pdfMake?.vfs || (pdfFonts as any).vfs || (pdfMakeMod as any).vfs
    return pdfMakeMod as PdfMakeType
  } catch (e) {
    console.error('Failed to load pdfmake', e)
    return null
  }
}

function formatCurrencyGTQ(n: number) {
  try {
    return new Intl.NumberFormat('es-GT', { style: 'currency', currency: 'GTQ' }).format(n)
  } catch {
    return `Q ${Number(n || 0).toFixed(2)}`
  }
}

function diffNights(start: Date, end: Date): number {
  const ms = (new Date(end).getTime() - new Date(start).getTime())
  const nights = Math.max(0, Math.round(ms / (1000 * 60 * 60 * 24)))
  return nights
}

function formatDateDMY(d: Date | string) {
  const dt = new Date(d)
  try {
    return new Intl.DateTimeFormat('es-GT', { year: 'numeric', month: '2-digit', day: '2-digit' }).format(dt)
  } catch {
    const y = dt.getFullYear()
    const m = String(dt.getMonth() + 1).padStart(2, '0')
    const day = String(dt.getDate()).padStart(2, '0')
    return `${day}/${m}/${y}`
  }
}

export async function exportReservationProofPdf(r: Reservation) {
  const pdfMake = await getPdfMake()
  if (!pdfMake) return

  const nights = diffNights(r.startDate, r.endDate)

  const docDefinition: any = {
    content: [
      { text: 'Comprobante de Reserva', style: 'header' },
      { text: `Reserva #${r.id}`, style: 'subheader', margin: [0, 0, 0, 8] },
      {
        columns: [
          [
            { text: 'Hotel', style: 'label' },
            { text: r.hotel?.name || r.hotelId || '—' },
            r.hotel?.address ? { text: r.hotel.address, style: 'muted' } : {},
          ],
          [
            { text: 'Cliente (CUI)', style: 'label' },
            { text: r.clientCui || '—' },
          ],
        ],
        columnGap: 20,
        margin: [0, 0, 0, 10],
      },
      {
        style: 'table',
        table: {
          widths: ['*', '*', '*'],
          body: [
            [
              { text: 'Fecha de entrada', style: 'th' },
              { text: 'Fecha de salida', style: 'th' },
              { text: 'Habitación', style: 'th' },
            ],
            [
              formatDateDMY(r.startDate as any),
              formatDateDMY(r.endDate as any),
              r.room?.number || r.room?.name || r.roomId || '—',
            ],
          ],
        },
        layout: 'lightHorizontalLines',
        margin: [0, 0, 0, 6],
      },
      { text: `Noches: ${nights}`, margin: [0, 0, 0, 2] },
      { text: `Subtotal: ${formatCurrencyGTQ(r.subtotal)}` },
      r.promotionApplied
        ? { text: `Promoción: ${r.promotionApplied.name} (${r.promotionApplied.percentOff}%)`, color: '#047857' }
        : {},
      { text: `Total: ${formatCurrencyGTQ(r.totalCost)}`, style: 'total', margin: [0, 6, 0, 0] },
      { text: 'Gracias por su reserva.', style: 'muted', margin: [0, 12, 0, 0] },
    ],
    styles: {
      header: { fontSize: 18, bold: true, margin: [0, 0, 0, 6] },
      subheader: { fontSize: 12, color: '#334155' },
      label: { bold: true, color: '#475569' },
      muted: { color: '#64748b' },
      table: { margin: [0, 6, 0, 0] },
      th: { bold: true, fillColor: '#f1f5f9' },
      total: { bold: true }
    },
    defaultStyle: { fontSize: 10 }
  }

  pdfMake.createPdf(docDefinition).download(`Comprobante_Reserva_${r.id}.pdf`)
}

export async function exportReservationBillPdf(r: Reservation) {
  const pdfMake = await getPdfMake()
  if (!pdfMake) return

  const nights = diffNights(r.startDate, r.endDate)
  const promotionText = r.promotionApplied ? `${r.promotionApplied.name} (${r.promotionApplied.percentOff}%)` : '—'

  const itemsBody = [
    [
      { text: 'Concepto', style: 'th' },
      { text: 'Detalle', style: 'th' },
      { text: 'Monto', style: 'th', alignment: 'right' },
    ],
    [
      'Alojamiento',
      `${nights} noche(s) - Habitación ${r.room?.number || r.room?.name || r.roomId || ''}`.trim(),
      { text: formatCurrencyGTQ(r.subtotal), alignment: 'right' },
    ],
  ]

  const totalsBody = [
    ['Subtotal', { text: formatCurrencyGTQ(r.subtotal), alignment: 'right' }],
    ['Descuento', { text: r.promotionApplied ? promotionText : '—', alignment: 'right' }],
    [{ text: 'Total', bold: true }, { text: formatCurrencyGTQ(r.totalCost), alignment: 'right', bold: true }],
  ]

  const docDefinition: any = {
    content: [
      { text: 'Factura', style: 'header' },
      { text: `Reserva #${r.id}`, style: 'subheader', margin: [0, 0, 0, 10] },
      {
        columns: [
          [
            { text: 'Emisor', style: 'label' },
            { text: r.hotel?.name || 'Hotel' },
            r.hotel?.address ? { text: r.hotel.address, style: 'muted' } : {},
          ],
          [
            { text: 'Cliente', style: 'label' },
            { text: `CUI: ${r.clientCui}` },
            { text: `Fecha: ${formatDateDMY(new Date())}`, style: 'muted' },
          ],
        ],
        columnGap: 20,
        margin: [0, 0, 0, 10],
      },
      { text: 'Detalle', style: 'section' },
      {
        style: 'table',
        table: { widths: ['*', '*', 100], body: itemsBody },
        layout: 'lightHorizontalLines',
        margin: [0, 6, 0, 10],
      },
      { text: 'Totales', style: 'section' },
      {
        style: 'table',
        table: { widths: ['*', 120], body: totalsBody },
        layout: 'noBorders',
      },
      { text: 'Gracias por su preferencia.', style: 'muted', margin: [0, 12, 0, 0] },
    ],
    styles: {
      header: { fontSize: 18, bold: true, margin: [0, 0, 0, 6] },
      subheader: { fontSize: 12, color: '#334155' },
      label: { bold: true, color: '#475569' },
      muted: { color: '#64748b' },
      section: { bold: true, margin: [0, 8, 0, 4] },
      th: { bold: true, fillColor: '#f1f5f9' },
    },
    defaultStyle: { fontSize: 10 }
  }

  pdfMake.createPdf(docDefinition).download(`Factura_Reserva_${r.id}.pdf`)
}
