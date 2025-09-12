import type { string } from "zod";
import type { Hotel, Room } from "../establishments/hotels";
import type { Entity } from "../utils/entity";
import type { PromotionApplied } from "../utils/promotion";

const CURRENT_RESERVATIONS_URI = "/v1/reservations";

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
export async function getAllReservations(params?: {}) {
  return await $api<Reservation[]>(`${CURRENT_RESERVATIONS_URI}`, {
    params
  })
}

export async function getReservationById(reservation_id: string) {
  return await $api<Reservation>(`${CURRENT_RESERVATIONS_URI}/${reservation_id}`);
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

// --------------------
// PDF Export Utilities
// --------------------

function formatGTQ(v: number | string | null | undefined): string {
  const n = Number(v)
  if (!isFinite(n)) return '—'
  try {
    return new Intl.NumberFormat('es-GT', { style: 'currency', currency: 'GTQ', minimumFractionDigits: 2 }).format(n)
  } catch {
    return `Q ${n.toFixed(2)}`
  }
}

function formatDisplayDate(d?: string | Date | null): string {
  if (!d) return '—'
  try {
    const date = typeof d === 'string' ? new Date(d) : d
    if (isNaN(date.getTime())) return String(d)
    const dd = String(date.getDate()).padStart(2, '0')
    const mm = String(date.getMonth() + 1).padStart(2, '0')
    const yyyy = date.getFullYear()
    return `${dd}/${mm}/${yyyy}`
  } catch {
    return String(d)
  }
}

function diffNights(start?: string | Date | null, end?: string | Date | null): number {
  if (!start || !end) return 0
  const s = typeof start === 'string' ? new Date(start) : start
  const e = typeof end === 'string' ? new Date(end) : end
  const ms = e.getTime() - s.getTime()
  const nights = Math.ceil(ms / (1000 * 60 * 60 * 24))
  return Math.max(0, nights)
}

type MaybeHotel = Partial<Hotel> | null | undefined
type MaybeRoom = Partial<Room> | null | undefined

/**
 * Exporta un comprobante de reservación a PDF usando pdfmake.
 * Requiere ejecutarse en el navegador (CSR).
 */
export async function exportReservationProofPDF(
  reservation: Partial<Reservation> & { id?: string },
  opts?: { hotel?: MaybeHotel; room?: MaybeRoom }
): Promise<void> {
  if (typeof window === 'undefined') {
    throw new Error('La exportación a PDF solo está disponible en el navegador')
  }

  const pdfMake = (await import('pdfmake/build/pdfmake')).default as any
  const pdfFonts = (await import('pdfmake/build/vfs_fonts')).default as any
  pdfMake.vfs = pdfFonts.vfs

  const hotel = opts?.hotel || (reservation as any)?.hotel || null
  const room = opts?.room || (reservation as any)?.room || null

  const nights = diffNights(reservation.startDate as any, reservation.endDate as any)
  const roomName = (room as any)?.number || (room as any)?.name || reservation.roomId || '—'

  const docDefinition = {
    pageSize: 'LETTER',
    pageMargins: [40, 60, 40, 60],
    content: [
      { text: 'Comprobante de Reservación', style: 'title' },
      { text: `Reserva: ${reservation.id || '—'}`, margin: [0, 4, 0, 0] },
      { text: `Cliente: ${reservation.clientCui || '—'}`, margin: [0, 0, 0, 12] },

      {
        table: {
          widths: ['auto', '*'],
          body: [
            [{ text: 'Hotel', bold: true }, `${(hotel as any)?.name || reservation.hotelId || '—'}\n${(hotel as any)?.address || ''}`],
            [{ text: 'Habitación', bold: true }, `${roomName}`],
            [{ text: 'Fechas', bold: true }, `${formatDisplayDate(reservation.startDate as any)} – ${formatDisplayDate(reservation.endDate as any)} (${nights} noche${nights === 1 ? '' : 's'})`],
            [{ text: 'Precio/noche', bold: true }, `${formatGTQ((room as any)?.pricePerNight)}`],
            [{ text: 'Subtotal', bold: true }, `${formatGTQ(reservation.subtotal as any)}`],
            [{ text: 'Promoción', bold: true }, reservation.promotionApplied ? `${(reservation.promotionApplied as any)?.name || ''} ${(reservation.promotionApplied as any)?.percentOff ? `· ${(reservation.promotionApplied as any).percentOff}%` : ''} ${(reservation.promotionApplied as any)?.amountOff ? `· - ${formatGTQ((reservation.promotionApplied as any).amountOff)}` : ''}` : '—'],
            [{ text: 'Total', bold: true }, { text: `${formatGTQ(reservation.totalCost as any)}`, bold: true }],
          ],
        },
        layout: 'lightHorizontalLines',
      },

      { text: `Emitido: ${formatDisplayDate(new Date())}`, alignment: 'right', margin: [0, 12, 0, 0], color: '#64748b' },
    ],
    styles: {
      title: { fontSize: 16, bold: true, margin: [0, 0, 0, 8] },
    },
  }

  const safeId = String(reservation.id || 'reserva').replace(/[^\p{L}\p{N}_-]+/gu, '_')
  const fileName = `comprobante_reservacion_${safeId}.pdf`
  pdfMake.createPdf(docDefinition).download(fileName)
}

/**
 * Exporta una factura simple de pago de la reservación a PDF usando pdfmake.
 * Muestra un renglón por alojamiento y un renglón de descuento (si aplica).
 */
export async function exportReservationInvoicePDF(
  reservation: Partial<Reservation> & { id?: string },
  opts?: { hotel?: MaybeHotel; room?: MaybeRoom }
): Promise<void> {
  if (typeof window === 'undefined') {
    throw new Error('La exportación a PDF solo está disponible en el navegador')
  }

  const pdfMake = (await import('pdfmake/build/pdfmake')).default as any
  const pdfFonts = (await import('pdfmake/build/vfs_fonts')).default as any
  pdfMake.vfs = pdfFonts.vfs

  const hotel = opts?.hotel || (reservation as any)?.hotel || null
  const room = opts?.room || (reservation as any)?.room || null

  const nights = diffNights(reservation.startDate as any, reservation.endDate as any)
  const unitPrice = Number((room as any)?.pricePerNight ?? 0)
  const lineTotal = nights * (isFinite(unitPrice) ? unitPrice : 0)
  const discount = Math.max(0, Number((reservation.subtotal as any) ?? 0) - Number((reservation.totalCost as any) ?? 0))
  const roomName = (room as any)?.number || (room as any)?.name || reservation.roomId || 'Habitación'

  const tableBody: any[] = []
  tableBody.push([
    { text: 'Descripción', bold: true },
    { text: 'Cantidad', bold: true, alignment: 'right' },
    { text: 'P. Unitario', bold: true, alignment: 'right' },
    { text: 'Importe', bold: true, alignment: 'right' },
  ])

  tableBody.push([
    `Alojamiento - ${roomName}`,
    { text: String(nights), alignment: 'right' },
    { text: formatGTQ(unitPrice), alignment: 'right' },
    { text: formatGTQ(lineTotal), alignment: 'right' },
  ])

  if (discount > 0) {
    tableBody.push([
      `Descuento ${reservation.promotionApplied?.name ? `- ${reservation.promotionApplied?.name}` : ''}`,
      { text: '1', alignment: 'right' },
      { text: `- ${formatGTQ(discount)}`, alignment: 'right' },
      { text: `- ${formatGTQ(discount)}`, alignment: 'right' },
    ])
  }

  const subtotal = Number((reservation.subtotal as any) ?? lineTotal)
  const total = Number((reservation.totalCost as any) ?? Math.max(0, subtotal - discount))

  const docDefinition = {
    pageSize: 'LETTER',
    pageMargins: [40, 60, 40, 60],
    content: [
      { text: 'Factura de Pago', style: 'title' },
      {
        columns: [
          [
            { text: (hotel as any)?.name || 'Hotel', style: 'vendorName' },
            { text: (hotel as any)?.address || '', color: '#64748b' },
          ],
          [
            { text: `Fecha: ${formatDisplayDate(new Date())}`, alignment: 'right' },
            { text: `Reserva: ${reservation.id || '—'}`, alignment: 'right' },
            { text: `Cliente: ${reservation.clientCui || '—'}`, alignment: 'right' },
          ],
        ],
        margin: [0, 0, 0, 12],
      },

      {
        table: {
          headerRows: 1,
          widths: ['*', 'auto', 'auto', 'auto'],
          body: tableBody,
        },
        layout: 'lightHorizontalLines',
      },

      {
        columns: [
          { text: '' },
          {
            table: {
              widths: ['*', 'auto'],
              body: [
                [{ text: 'Subtotal', alignment: 'right' }, { text: formatGTQ(subtotal), alignment: 'right' }],
                ...(discount > 0 ? [[{ text: 'Descuento', alignment: 'right' }, { text: `- ${formatGTQ(discount)}`, alignment: 'right' }]] : []),
                [{ text: 'Total', bold: true, alignment: 'right' }, { text: formatGTQ(total), bold: true, alignment: 'right' }],
              ],
            },
            layout: 'noBorders',
          },
        ],
        margin: [0, 12, 0, 0],
      },
    ],
    styles: {
      title: { fontSize: 16, bold: true, margin: [0, 0, 0, 6] },
      vendorName: { fontSize: 12, bold: true },
    },
  }

  const safeId = String(reservation.id || 'reserva').replace(/[^\p{L}\p{N}_-]+/gu, '_')
  const fileName = `factura_reservacion_${safeId}.pdf`
  pdfMake.createPdf(docDefinition).download(fileName)
}
