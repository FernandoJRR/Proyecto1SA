<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Page Header -->
    <header class="max-w-5xl mx-auto mb-6" role="banner">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link :to="backPath">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver al listado" />
          </router-link>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Detalle de Habitación</h1>
        </div>
      </div>
    </header>

    <main class="max-w-5xl mx-auto" role="main">
      <div v-if="state.status === 'pending'" class="py-16 text-center text-slate-600">Cargando orden…</div>
      <div v-else-if="state.status === 'error'" class="py-16 text-center text-red-600">Ocurrió un error inesperado.</div>

      <div v-else class="rounded-2xl border border-slate-200 bg-white shadow p-6 sm:p-8">
        <!-- Summary grid: Room info -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <p class="font-medium text-slate-600">ID de la habitación</p>
            <p class="text-sm font-mono">{{ room?.id || roomId }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Hotel</p>
            <p class="text-sm font-mono">{{ hotelId }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Número / Nombre</p>
            <p class="text-base">{{ room?.number || room?.name || '—' }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Precio por noche</p>
            <p class="text-base">{{ room?.pricePerNight != null ? formatGTQ(room.pricePerNight) : '—' }}</p>
          </div>
        </div>

        <!-- Reservations table -->
        <div class="mt-8">
          <h2 class="text-lg font-semibold mb-3">Reservas de esta habitación</h2>
          <div class="overflow-x-auto rounded-lg border border-slate-200">
            <DataTable :value="reservations as any[]" tableStyle="min-width: 56rem" stripedRows rowHover :loading="reservationsLoading">
              <Column header="#">
                <template #body="{ index }">{{ index + 1 }}</template>
              </Column>
              <Column header="ID">
                <template #body="{ data }"><span class="font-mono">{{ data.id }}</span></template>
              </Column>
              <Column header="Cliente">
                <template #body="{ data }">{{ data.clientCui || data.clientId || '—' }}</template>
              </Column>
              <Column header="Inicio">
                <template #body="{ data }">{{ fmtDate(data.startDate) }}</template>
              </Column>
              <Column header="Fin">
                <template #body="{ data }">{{ fmtDate(data.endDate) }}</template>
              </Column>
              <Column header="Estado">
                <template #body="{ data }">{{ data.status || '—' }}</template>
              </Column>
              <Column header="Total">
                <template #body="{ data }">{{ data.totalCost != null ? formatGTQ(data.totalCost) : '—' }}</template>
              </Column>
              <template #empty>
                <div class="py-8 text-center text-slate-600">No hay reservaciones para esta habitación.</div>
              </template>
            </DataTable>
          </div>
        </div>

        <!-- Reviews table -->
        <div class="mt-8">
          <div class="flex items-center justify-between mb-3">
            <h2 class="text-lg font-semibold">Reseñas de esta habitación</h2>
            <div v-if="averageRating != null" class="flex items-center gap-2">
              <Tag severity="info" :value="`Promedio: ${averageRating.toFixed(1)} / 5`" />
            </div>
          </div>
          <div class="overflow-x-auto rounded-lg border border-slate-200">
            <DataTable :value="reviews as any[]" tableStyle="min-width: 56rem" stripedRows rowHover :loading="reviewsLoading">
              <Column header="#">
                <template #body="{ index }">{{ index + 1 }}</template>
              </Column>
              <Column header="Calificación">
                <template #body="{ data }">{{ Number(data.rating)?.toFixed(1) ?? '—' }}</template>
              </Column>
              <Column header="Comentario">
                <template #body="{ data }">
                  <span class="truncate max-w-[28rem] inline-block align-middle" :title="data.comment">{{ data.comment || '—' }}</span>
                </template>
              </Column>
              <Column header="Fuente (sourceId)">
                <template #body="{ data }">{{ data.sourceId || '—' }}</template>
              </Column>
              <template #empty>
                <div class="py-8 text-center text-slate-600">No hay reseñas para esta habitación.</div>
              </template>
            </DataTable>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import Tag from 'primevue/tag'
import { useRoute } from 'vue-router'
import { getHotelRoom } from '~/lib/api/establishments/hotels'
import { getAllReservations } from '~/lib/api/reservations/reservations'
import { getReviews } from '~/lib/api/reviews/reviews'

const route = useRoute()

console.log("ROUUUUUUTE")
console.log(route.params)
const hotelId = computed(() => (route.params as any).id as string)
const roomId = computed(() => ((route.params as any).roomId ?? (route.params as any).roomId ?? (route.params as any).rid) as string)
const backPath = computed(() => `/admin/hoteles/${hotelId.value}`)

// Room details (requires both hotelId and roomId)
const { state } = useCustomQuery({
  key: ['hotelRoom', () => `${hotelId.value}:${roomId.value}`],
  query: () => getHotelRoom(hotelId.value, roomId.value),
})
const room = computed(() => state.value.data as any)

// Reservations for this room
const { state: reservationsState, asyncStatus: reservationsStatus } = useCustomQuery({
  key: ['roomReservations', () => `${hotelId.value}:${roomId.value}`],
  query: () => getAllReservations({ hotelId: hotelId.value, roomId: roomId.value }),
})
const reservationsLoading = computed(() => reservationsStatus.value === 'loading')
const reservations = computed(() => (reservationsState.value.data as any[]) ?? [])

// Reviews for this room (sourceId = roomId)
const { state: reviewsState, asyncStatus: reviewsStatus } = useCustomQuery({
  key: ['roomReviews', () => roomId.value],
  query: () => getReviews({ sourceId: roomId.value }),
})
const reviewsLoading = computed(() => reviewsStatus.value === 'loading')
const reviews = computed(() => (reviewsState.value.data as any[]) ?? [])
const averageRating = computed(() => {
  const list = reviews.value || []
  if (!list.length) return null
  const sum = list.reduce((acc, r) => acc + (Number(r.rating) || 0), 0)
  return (sum / list.length)
})

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

function fmtDate(d: any) {
  if (!d) return '—'
  const date = typeof d === 'string' || typeof d === 'number' ? new Date(d) : d
  if (!(date instanceof Date) || isNaN(date.getTime())) return '—'
  return `${date.getFullYear()}-${String(date.getMonth()+1).padStart(2,'0')}-${String(date.getDate()).padStart(2,'0')}`
}
</script>
