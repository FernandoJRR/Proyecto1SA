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

type MaybeRestaurant = Partial<Restaurant> | null | undefined

/**
 * Exporta una factura simple de pago de la orden a PDF usando pdfmake.
 * Lista cada ítem (platillo) con cantidad, precio unitario e importe.
 * Agrega un renglón de descuento si existe una promoción aplicada.
 */
export async function exportOrderInvoicePDF(
  order: Partial<Order> & { id?: string },
  opts?: { restaurant?: MaybeRestaurant }
): Promise<void> {
  if (typeof window === 'undefined') {
    throw new Error('La exportación a PDF solo está disponible en el navegador')
  }

  const pdfMake = (await import('pdfmake/build/pdfmake')).default as any
  const pdfFonts = (await import('pdfmake/build/vfs_fonts')).default as any
  pdfMake.vfs = pdfFonts.vfs

  const restaurant = opts?.restaurant || (order as any)?.restaurant || null

  const tableBody: any[] = []
  tableBody.push([
    { text: 'Descripción', bold: true },
    { text: 'Cantidad', bold: true, alignment: 'right' },
    { text: 'P. Unitario', bold: true, alignment: 'right' },
    { text: 'Importe', bold: true, alignment: 'right' },
  ])

  let computedSubtotal = 0
  const items = Array.isArray(order.items as any) ? (order.items as any) : []
  for (const it of items) {
    const q = Number(it?.quantity ?? 0)
    const p = Number(it?.price ?? 0)
    const line = (isFinite(q) ? q : 0) * (isFinite(p) ? p : 0)
    computedSubtotal += line
    tableBody.push([
      String(it?.name ?? it?.dishId ?? 'Ítem'),
      { text: String(q), alignment: 'right' },
      { text: formatGTQ(p), alignment: 'right' },
      { text: formatGTQ(line), alignment: 'right' },
    ])
  }

  // Discount derived from subtotal - total (if both are present)
  const subtotal = Number((order.subtotal as any) ?? computedSubtotal)
  const total = Number((order.total as any) ?? subtotal)
  const discount = Math.max(0, subtotal - total)

  if (discount > 0) {
    tableBody.push([
      `Descuento ${order.promotionApplied?.name ? `- ${order.promotionApplied?.name}` : ''}`,
      { text: '1', alignment: 'right' },
      { text: `- ${formatGTQ(discount)}`, alignment: 'right' },
      { text: `- ${formatGTQ(discount)}`, alignment: 'right' },
    ])
  }

  const docDefinition = {
    pageSize: 'LETTER',
    pageMargins: [40, 60, 40, 60],
    content: [
      { text: 'Factura de Orden', style: 'title' },
      {
        columns: [
          [
            { text: (restaurant as any)?.name || 'Restaurante', style: 'vendorName' },
            { text: (restaurant as any)?.address || '', color: '#64748b' },
          ],
          [
            { text: `Fecha: ${formatDisplayDate(new Date())}`, alignment: 'right' },
            { text: `Orden: ${order.id || '—'}`, alignment: 'right' },
            { text: `Cliente: ${order.clientCui || '—'}`, alignment: 'right' },
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

  const safeId = String(order.id || 'orden').replace(/[^\p{L}\p{N}_-]+/gu, '_')
  const fileName = `factura_orden_${safeId}.pdf`
  pdfMake.createPdf(docDefinition).download(fileName)
}
