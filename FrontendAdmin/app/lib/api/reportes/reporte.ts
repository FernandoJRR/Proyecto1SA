import type { Payment } from "../payments/payments";

const CURRENT_REPORTS_ESTABLISHMENTS_URI = "/v1/establishments/reports";
const CURRENT_REPORTS_CLIENTS_URI = "/v1/clients/reports";
const CURRENT_REPORTS_PAYMENTS_URI = "/v1/payments/reports";
const CURRENT_REPORTS_RESERVATIONS_URI = "/v1/reservations/reports";
const CURRENT_REPORTS_ORDERS_URI = "/v1/orders/reports";

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

export async function getIncomeOutcomeReport(
  startDate: Date,
  endDate: Date
) {
  return await $api<any>(
    `${CURRENT_REPORTS_PAYMENTS_URI}/income-outcome`, {
    query: {
        fromDate: formatISODate(startDate),
        toDate: formatISODate(endDate)
    },
    method: "GET",
  });
}

export async function getMostPopularRoomReport(
  hotelId?: string | null,
) {
  return await $api<any>(
    `${CURRENT_REPORTS_RESERVATIONS_URI}/most-popular-room`, {
    query: {
        hotelId: hotelId
    },
    method: "GET",
  });
}

export async function getMostPopularRestaurantReport() {
  return await $api<any>(
    `${CURRENT_REPORTS_ORDERS_URI}/most-popular-restaurant`, {
    method: "GET",
  });
}

// --------------------
// PDF Export Utilities
// --------------------

type EstablishmentIncomeRow = {
  id?: string
  date?: string | Date | null
  establishmentId?: string
  description?: string | null
  amount?: number | string | null
}

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

/**
 * Export Establishment Income (Ingresos por establecimiento) to PDF using pdfmake.
 * This function runs on client side only.
 */
export async function exportEstablishmentIncomePDF(
  rows: EstablishmentIncomeRow[],
  opts?: {
    establishmentLabel?: string
    establishmentId?: string
    startDate?: Date | null
    endDate?: Date | null
  }
): Promise<void> {
  if (typeof window === 'undefined') {
    throw new Error('La exportación a PDF solo está disponible en el navegador')
  }

  // Lazy-load pdfmake to avoid SSR issues
  const pdfMake = (await import('pdfmake/build/pdfmake')).default as any
  const pdfFonts = (await import('pdfmake/build/vfs_fonts')).default as any
  pdfMake.vfs = pdfFonts.vfs

  const establishmentName = opts?.establishmentLabel || opts?.establishmentId || 'Establecimiento'
  const dateRangeLabel = (opts?.startDate && opts?.endDate)
    ? `${formatDisplayDate(opts.startDate)} – ${formatDisplayDate(opts.endDate)}`
    : 'Todos'

  // Build table body
  const body: any[] = []
  body.push([
    { text: 'Fecha', bold: true },
    { text: 'Descripción', bold: true },
    { text: 'Monto', bold: true, alignment: 'right' },
  ])

  let total = 0
  for (const r of rows || []) {
    const amount = Number(r.amount ?? 0)
    total += isFinite(amount) ? amount : 0
    body.push([
      formatDisplayDate(r.date ?? null),
      r.description || '',
      { text: formatGTQ(amount), alignment: 'right' },
    ])
  }

  const docDefinition = {
    pageSize: 'LETTER',
    pageOrientation: 'landscape',
    pageMargins: [40, 60, 40, 60],
    content: [
      { text: 'Ingresos por establecimiento', style: 'title' },
      { text: `Establecimiento: ${establishmentName}`, margin: [0, 6, 0, 0] },
      { text: `Rango de fechas: ${dateRangeLabel}`, margin: [0, 0, 0, 12] },

      {
        table: {
          headerRows: 1,
          widths: ['auto', '*', 'auto'],
          body,
        },
        layout: 'lightHorizontalLines',
      },

      {
        columns: [
          { text: '' },
          { text: 'Total:', alignment: 'right', bold: true },
          { text: formatGTQ(total), alignment: 'right', bold: true },
        ],
        margin: [0, 10, 0, 0],
      },
    ],
    styles: {
      title: { fontSize: 16, bold: true, margin: [0, 0, 0, 6] },
    },
  }

  const safeName = String(establishmentName).replace(/[^\p{L}\p{N}_-]+/gu, '_') || 'reporte'
  const fileName = `ingresos_${safeName}.pdf`
  pdfMake.createPdf(docDefinition).download(fileName)
}

// Client Activity (Ingresos por cliente)
type ClientActivityRow = {
  id?: string
  date?: string | Date | null
  type?: string | null
  establishmentId?: string | null
  document?: string | null
  amount?: number | string | null
}

export async function exportClientActivityPDF(
  rows: ClientActivityRow[],
  opts?: {
    clientId?: string
    establishmentLabel?: string
    startDate?: Date | null
    endDate?: Date | null
  }
): Promise<void> {
  if (typeof window === 'undefined') {
    throw new Error('La exportación a PDF solo está disponible en el navegador')
  }

  const pdfMake = (await import('pdfmake/build/pdfmake')).default as any
  const pdfFonts = (await import('pdfmake/build/vfs_fonts')).default as any
  pdfMake.vfs = pdfFonts.vfs

  const client = opts?.clientId || '—'
  const establishment = opts?.establishmentLabel || 'Todos'
  const dateRangeLabel = (opts?.startDate && opts?.endDate)
    ? `${formatDisplayDate(opts.startDate)} – ${formatDisplayDate(opts.endDate)}`
    : 'Todos'

  const body: any[] = []
  body.push([
    { text: 'Fecha', bold: true },
    { text: 'Monto', bold: true, alignment: 'right' },
  ])

  let total = 0
  for (const r of rows || []) {
    const amount = Number(r.amount ?? 0)
    total += isFinite(amount) ? amount : 0
    body.push([
      formatDisplayDate(r.date ?? null),
      { text: formatGTQ(amount), alignment: 'right' },
    ])
  }

  const docDefinition = {
    pageSize: 'LETTER',
    pageOrientation: 'landscape',
    pageMargins: [40, 60, 40, 60],
    content: [
      { text: 'Actividad por cliente', style: 'title' },
      { text: `Cliente: ${client}`, margin: [0, 6, 0, 0] },
      { text: `Establecimiento: ${establishment}`, margin: [0, 0, 0, 0] },
      { text: `Rango de fechas: ${dateRangeLabel}`, margin: [0, 0, 0, 12] },

      {
        table: {
          headerRows: 1,
          widths: ['auto', 'auto'],
          body,
        },
        layout: 'lightHorizontalLines',
      },

      {
        columns: [
          { text: 'Total:', alignment: 'right', bold: true },
          { text: formatGTQ(total), alignment: 'right', bold: true },
        ],
        margin: [0, 10, 0, 0],
      },
    ],
    styles: {
      title: { fontSize: 16, bold: true, margin: [0, 0, 0, 6] },
    },
  }

  const safeClient = String(client).replace(/[^\p{L}\p{N}_-]+/gu, '_') || 'cliente'
  const fileName = `actividad_cliente_${safeClient}.pdf`
  pdfMake.createPdf(docDefinition).download(fileName)
}

// Income vs Outcome (Ingresos vs Egresos)
type IncomeOutcomeRow = {
  id?: string
  date?: string | Date | null
  sourceType?: string | null
  establishmentId?: string | null
  clientId?: string | null
  total?: number | string | null
}

export async function exportIncomeOutcomePDF(
  rows: IncomeOutcomeRow[],
  opts?: {
    startDate?: Date | null
    endDate?: Date | null
    totalIncome?: number | null
    totalOutcome?: number | null
    outcomeHotels?: number | null
    outcomeRestaurants?: number | null
  }
): Promise<void> {
  if (typeof window === 'undefined') {
    throw new Error('La exportación a PDF solo está disponible en el navegador')
  }

  const pdfMake = (await import('pdfmake/build/pdfmake')).default as any
  const pdfFonts = (await import('pdfmake/build/vfs_fonts')).default as any
  pdfMake.vfs = pdfFonts.vfs

  const dateRangeLabel = (opts?.startDate && opts?.endDate)
    ? `${formatDisplayDate(opts.startDate)} – ${formatDisplayDate(opts.endDate)}`
    : '—'

  // Table
  const body: any[] = []
  body.push([
    { text: 'Fecha', bold: true },
    { text: 'Fuente', bold: true },
    { text: 'Establecimiento', bold: true },
    { text: 'Cliente', bold: true },
    { text: 'Monto', bold: true, alignment: 'right' },
  ])

  let computedIncome = 0
  for (const r of rows || []) {
    const amount = Number(r.total ?? 0)
    if (isFinite(amount)) computedIncome += amount
    body.push([
      formatDisplayDate(r.date ?? null),
      r.sourceType || '—',
      r.establishmentId || '—',
      r.clientId || '—',
      { text: formatGTQ(amount), alignment: 'right' },
    ])
  }

  const totalIncome = opts?.totalIncome ?? computedIncome
  const totalOutcome = opts?.totalOutcome ?? 0
  const outcomeHotels = opts?.outcomeHotels ?? null
  const outcomeRestaurants = opts?.outcomeRestaurants ?? null
  const net = totalIncome - totalOutcome

  const summaryLines: any[] = [
    { columns: [ { text: 'Ingresos:', width: '*', alignment: 'right' }, { text: formatGTQ(totalIncome), width: 'auto', alignment: 'right', bold: true } ], margin: [0, 2, 0, 0] },
    { columns: [ { text: 'Egresos:', width: '*', alignment: 'right' }, { text: formatGTQ(totalOutcome), width: 'auto', alignment: 'right', bold: true } ], margin: [0, 2, 0, 0] },
    { columns: [ { text: 'Ganancia Neta:', width: '*', alignment: 'right' }, { text: formatGTQ(net), width: 'auto', alignment: 'right', bold: true } ], margin: [0, 6, 0, 0] },
  ]
  if (typeof outcomeHotels === 'number') {
    summaryLines.splice(1, 0, { columns: [ { text: 'Egresos Hoteles:', width: '*', alignment: 'right' }, { text: formatGTQ(outcomeHotels), width: 'auto', alignment: 'right' } ] })
  }
  if (typeof outcomeRestaurants === 'number') {
    summaryLines.splice(2, 0, { columns: [ { text: 'Egresos Restaurantes:', width: '*', alignment: 'right' }, { text: formatGTQ(outcomeRestaurants), width: 'auto', alignment: 'right' } ] })
  }

  const docDefinition = {
    pageSize: 'LETTER',
    pageOrientation: 'landscape',
    pageMargins: [40, 60, 40, 60],
    content: [
      { text: 'Ingresos vs Egresos', style: 'title' },
      { text: `Rango de fechas: ${dateRangeLabel}`, margin: [0, 6, 0, 12] },

      { table: { headerRows: 1, widths: ['auto', 'auto', '*', '*', 'auto'], body }, layout: 'lightHorizontalLines' },

      { text: 'Resumen', style: 'subtitle', margin: [0, 12, 0, 6] },
      ...summaryLines,
    ],
    styles: {
      title: { fontSize: 16, bold: true },
      subtitle: { fontSize: 12, bold: true },
    },
  }

  const fileName = `ingresos_vs_egresos.pdf`
  pdfMake.createPdf(docDefinition).download(fileName)
}

// Most Popular Room (Habitación más popular)
type MostPopularRoomRow = {
  stayId?: string
  room?: string | number | null
  hotel?: string | null
  clientCui?: string | null
  startDate?: string | Date | null
  endDate?: string | Date | null
  totalCost?: number | string | null
}

export async function exportMostPopularRoomPDF(
  rows: MostPopularRoomRow[],
  opts?: {
    hotelName?: string | null
    roomNumber?: string | number | null
  }
): Promise<void> {
  if (typeof window === 'undefined') {
    throw new Error('La exportación a PDF solo está disponible en el navegador')
  }

  const pdfMake = (await import('pdfmake/build/pdfmake')).default as any
  const pdfFonts = (await import('pdfmake/build/vfs_fonts')).default as any
  pdfMake.vfs = pdfFonts.vfs

  const headerHotel = opts?.hotelName ?? '—'
  const headerRoom = opts?.roomNumber != null && opts?.roomNumber !== '' ? String(opts?.roomNumber) : '—'

  const body: any[] = []
  body.push([
    { text: 'Habitación', bold: true },
    { text: 'Hotel', bold: true },
    { text: 'Cliente (CUI)', bold: true },
    { text: 'Inicio', bold: true },
    { text: 'Fin', bold: true },
    { text: 'Total', bold: true, alignment: 'right' },
  ])

  let total = 0
  for (const r of rows || []) {
    const amount = Number(r.totalCost ?? 0)
    if (isFinite(amount)) total += amount
    body.push([
      r.room != null ? `#${r.room}` : '—',
      r.hotel || '—',
      r.clientCui || '—',
      formatDisplayDate(r.startDate ?? null),
      formatDisplayDate(r.endDate ?? null),
      { text: formatGTQ(amount), alignment: 'right' },
    ])
  }

  const docDefinition = {
    pageSize: 'LETTER',
    pageOrientation: 'landscape',
    pageMargins: [40, 60, 40, 60],
    content: [
      { text: 'Habitación más popular', style: 'title' },
      { text: `Hotel: ${headerHotel}`, margin: [0, 6, 0, 0] },
      { text: `Habitación: ${headerRoom}`, margin: [0, 0, 0, 12] },

      { table: { headerRows: 1, widths: ['auto', '*', 'auto', 'auto', 'auto', 'auto'], body }, layout: 'lightHorizontalLines' },

      {
        columns: [
          { text: '' },
          { text: '' },
          { text: '' },
          { text: '' },
          { text: 'Total:', alignment: 'right', bold: true },
          { text: formatGTQ(total), alignment: 'right', bold: true },
        ],
        margin: [0, 10, 0, 0],
      },
    ],
    styles: {
      title: { fontSize: 16, bold: true },
    },
  }

  const safeHotel = String(headerHotel).replace(/[^\p{L}\p{N}_-]+/gu, '_') || 'hotel'
  const safeRoom = String(headerRoom).replace(/[^\p{L}\p{N}_-]+/gu, '_') || 'habitacion'
  const fileName = `habitacion_mas_popular_${safeHotel}_${safeRoom}.pdf`
  pdfMake.createPdf(docDefinition).download(fileName)
}

// Most Popular Restaurant (Restaurante más popular)
type MostPopularRestaurantRow = {
  orderId?: string
  restaurant?: string | null
  clientCui?: string | null
  items?: string | null
  total?: number | string | null
}

export async function exportMostPopularRestaurantPDF(
  rows: MostPopularRestaurantRow[],
  opts?: {
    restaurantName?: string | null
    startDate?: Date | null
    endDate?: Date | null
  }
): Promise<void> {
  if (typeof window === 'undefined') {
    throw new Error('La exportación a PDF solo está disponible en el navegador')
  }

  const pdfMake = (await import('pdfmake/build/pdfmake')).default as any
  const pdfFonts = (await import('pdfmake/build/vfs_fonts')).default as any
  pdfMake.vfs = pdfFonts.vfs

  const headerRestaurant = opts?.restaurantName ?? (rows?.[0]?.restaurant ?? '—')
  const dateRangeLabel = (opts?.startDate && opts?.endDate)
    ? `${formatDisplayDate(opts.startDate)} – ${formatDisplayDate(opts.endDate)}`
    : '—'

  const body: any[] = []
  body.push([
    { text: 'Restaurante', bold: true },
    { text: 'Cliente (CUI)', bold: true },
    { text: 'Ítems', bold: true },
    { text: 'Total', bold: true, alignment: 'right' },
  ])

  let total = 0
  for (const r of rows || []) {
    const amount = Number(r.total ?? 0)
    if (isFinite(amount)) total += amount
    body.push([
      r.restaurant || headerRestaurant || '—',
      r.clientCui || '—',
      r.items || '—',
      { text: formatGTQ(amount), alignment: 'right' },
    ])
  }

  const docDefinition = {
    pageSize: 'LETTER',
    pageOrientation: 'landscape',
    pageMargins: [40, 60, 40, 60],
    content: [
      { text: 'Restaurante más popular', style: 'title' },
      { text: `Restaurante: ${headerRestaurant}`, margin: [0, 6, 0, 0] },
      { text: `Rango de fechas: ${dateRangeLabel}`, margin: [0, 0, 0, 12] },

      { table: { headerRows: 1, widths: ['*', 'auto', '*', 'auto'], body }, layout: 'lightHorizontalLines' },

      {
        columns: [
          { text: '' },
          { text: '' },
          { text: 'Total:', alignment: 'right', bold: true },
          { text: formatGTQ(total), alignment: 'right', bold: true },
        ],
        margin: [0, 10, 0, 0],
      },
    ],
    styles: {
      title: { fontSize: 16, bold: true },
    },
  }

  const safeRestaurant = String(headerRestaurant).replace(/[^\p{L}\p{N}_-]+/gu, '_') || 'restaurante'
  const fileName = `restaurante_mas_popular_${safeRestaurant}.pdf`
  pdfMake.createPdf(docDefinition).download(fileName)
}
