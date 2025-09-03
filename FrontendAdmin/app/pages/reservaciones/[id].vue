<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Page Header -->
    <header class="max-w-5xl mx-auto mb-6" role="banner">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link to="/reservaciones">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver al listado" />
          </router-link>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Detalle de Reservación</h1>
        </div>
      </div>
    </header>

    <main class="max-w-5xl mx-auto" role="main">
      <div v-if="state.status === 'pending'" class="py-16 text-center text-slate-600">Cargando reservación…</div>
      <div v-else-if="state.status === 'error'" class="py-16 text-center text-red-600">Ocurrió un error inesperado.</div>

      <div v-else class="rounded-2xl border border-slate-200 bg-white shadow p-6 sm:p-8">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <p class="font-medium text-slate-600">ID de la reservación</p>
            <p class="text-sm font-mono">{{ reservation.id }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">CUI del cliente</p>
            <p class="text-base">{{ reservation.clientCui }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Hotel</p>
            <p class="text-sm font-mono">{{ reservation.hotelId || '—' }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Habitación</p>
            <p class="text-sm font-mono">{{ reservation.roomId || '—' }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Inicio</p>
            <p class="text-base">{{ formatDate(reservation.startDate) }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Fin</p>
            <p class="text-base">{{ formatDate(reservation.endDate) }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Subtotal</p>
            <p class="text-base">{{ formatGTQ(reservation.subtotal) }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Promoción aplicada</p>
            <div>
              <Tag v-if="reservation.promotionApplied" :value="promoLabel(reservation.promotionApplied)" severity="info" class="text-xs px-2 py-0.5 truncate max-w-[12rem]">
                {{ reservation.promotionApplied.name }}<br/>
                - Q.{{ reservation.promotionApplied.amountOff }}<br/>
                {{ reservation.promotionApplied.percentOff }}% descuento
              </Tag>
              <span v-else class="text-slate-500">—</span>
            </div>
          </div>

          <div class="md:col-span-2">
            <p class="font-medium text-slate-600">Total</p>
            <p class="text-lg font-semibold">{{ formatGTQ(reservation.totalCost) }}</p>
          </div>
        </div>
      </div>
      <div class="mt-6 rounded-2xl border border-slate-200 bg-white shadow p-6 sm:p-8">
        <div class="flex items-center justify-between mb-4">
          <h2 class="text-lg font-semibold">Hotel y habitación</h2>
          <div class="flex items-center gap-2" v-if="hotel?.id">
            <router-link :to="`/admin/hoteles/${hotel.id}`">
              <Button icon="pi pi-external-link" label="Ver hotel" rounded />
            </router-link>
            <router-link v-if="room?.id" :to="`/admin/hoteles/${reservation.hotelId}/habitaciones/${room.id}`">
              <Button icon="pi pi-search" label="Ver habitación" rounded severity="info" />
            </router-link>
          </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <p class="font-medium text-slate-600">Hotel</p>
            <div class="space-y-1">
              <p class="text-base">{{ hotel?.name || '—' }}</p>
              <p class="text-sm text-slate-600">{{ hotel?.address || '—' }}</p>
            </div>
          </div>

          <div>
            <p class="font-medium text-slate-600">Habitación</p>
            <div class="space-y-1">
              <p class="text-base">{{ room?.number || '—' }}</p>
              <p class="text-sm text-slate-600">Capacidad: {{ room?.capacity ?? '—' }}</p>
              <p class="text-sm text-slate-600">Precio/noche: {{ formatGTQ(room?.pricePerNight) }}</p>
              <p class="text-sm text-slate-600">Estado: {{ statusLabel(room?.status) }}</p>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { Tag } from 'primevue'
import { getReservationById } from '~/lib/api/reservations/reservations'
import { getHotelById, getHotelRooms } from '~/lib/api/establishments/hotels'

const route = useRoute()

const { state } = useCustomQuery({
  key: ['reservation', () => route.params.id as string],
  query: () => getReservationById(route.params.id as string),
})

const reservation = computed(() => state.value.data as any)

const hotel = ref<any | null>(null)
const room = ref<any | null>(null)

watchEffect(async () => {
  const r = reservation.value as any
  if (!r?.hotelId) { hotel.value = null; room.value = null; return }
  try {
    const h = await getHotelById(r.hotelId)
    hotel.value = h
    if (r.roomId) {
      const rooms = await getHotelRooms(r.hotelId)
      room.value = Array.isArray(rooms) ? rooms.find((x: any) => x.id === r.roomId) || null : null
    } else {
      room.value = null
    }
  } catch {
    hotel.value = null
    room.value = null
  }
})

function formatDate(v: string | Date | null | undefined) {
  if (!v) return '—'
  try {
    const d = typeof v === 'string' ? new Date(v) : v
    return new Intl.DateTimeFormat('es-GT', { dateStyle: 'medium' }).format(d)
  } catch {
    return String(v)
  }
}

function formatGTQ(v: number | string | null | undefined) {
  if (v === null || v === undefined) return '—'
  const n = Number(v)
  if (Number.isNaN(n)) return '—'
  try {
    return new Intl.NumberFormat('es-GT', { style: 'currency', currency: 'GTQ', minimumFractionDigits: 2 }).format(n)
  } catch {
    return `Q.${n.toFixed(2)}`
  }
}

function promoLabel(p: any) {
  if (!p) return ''
  const parts: string[] = []
  if (p.name) parts.push(p.name)
  if (p.percentOff) parts.push(`${p.percentOff}% off`)
  if (p.amountOff) parts.push(`- ${formatGTQ(p.amountOff)}`)
  return parts.join(' · ')
}

function statusLabel(s?: string) {
  if (!s) return '—'
  const m: Record<string, string> = { AVAILABLE: 'Disponible', OCCUPIED: 'Ocupada', MAINTENANCE: 'Mantenimiento' }
  return m[s] || s
}
</script>
