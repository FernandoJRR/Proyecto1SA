<template>
  <div class="min-h-screen bg-slate-50">
    <!-- Header -->
    <header class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link to="/">
            <Button icon="pi pi-arrow-left" label="Inicio" size="small" />
          </router-link>
          <div>
            <h1 class="text-2xl sm:text-3xl font-extrabold tracking-tight text-slate-900">Hoteles disponibles</h1>
            <p class="text-slate-600 text-sm sm:text-base">
              <template v-if="startDate && endDate">
                Del <strong>{{ startLabel }}</strong> al <strong>{{ endLabel }}</strong>
                <Tag class="ml-2 align-middle" :value="`${nights} ${nights === 1 ? 'noche' : 'noches'}`" />
              </template>
              <template v-else>
                Selecciona fechas en la pantalla principal para ver disponibilidad.
              </template>
            </p>
          </div>
        </div>

        <div class="flex items-center gap-2">
          <Button severity="secondary" outlined icon="pi pi-filter" label="Limpiar fechas" @click="clearDates" />
          <router-link to="/">
            <Button icon="pi pi-calendar" label="Cambiar fechas" />
          </router-link>
        </div>
      </div>
    </header>

    <!-- Missing dates state -->
    <main v-if="!startDate || !endDate" class="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 pb-16">
      <div class="rounded-2xl border border-slate-200 bg-white p-8 text-center">
        <i class="pi pi-calendar text-4xl text-slate-400"></i>
        <h2 class="mt-3 text-xl font-semibold">Selecciona tus fechas</h2>
        <p class="text-slate-600 mt-1">Regresa al inicio y elige fecha de entrada y salida para ver hoteles disponibles.</p>
        <router-link to="/" class="mt-4 inline-block">
          <Button icon="pi pi-arrow-left" label="Ir al inicio" />
        </router-link>
      </div>
    </main>

    <!-- Results -->
    <main v-else class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pb-16">
      <!-- Summary bar -->
      <div class="mb-4 flex items-center justify-between text-sm text-slate-600">
        <div>
          <span class="font-semibold">{{ visibleHotelsCount }}</span>
          {{ visibleHotelsCount === 1 ? 'hotel' : 'hoteles' }} encontrados
        </div>
        <div class="hidden sm:flex items-center gap-2">
          <Tag v-if="nights > 0" severity="info" :value="`${nights} ${nights === 1 ? 'noche' : 'noches'}`" />
        </div>
      </div>

      <!-- Hotel cards -->
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        <div
          v-for="hotel in visibleHotels"
          :key="hotel.id"
          class="rounded-2xl border border-slate-200 bg-white shadow-sm hover:shadow-md transition"
        >
          <div class="p-4">
            <div class="flex items-start justify-between gap-3">
              <div>
                <h3 class="text-lg font-semibold text-slate-900">{{ hotel.name }}</h3>
                <p class="text-xs text-slate-600">{{ hotel.city || hotel.location || hotel.address || '—' }}</p>
              </div>
              <Tag v-if="hotel.stars" :value="`${hotel.stars}★`" />
            </div>

            <!-- Inline available rooms -->
            <div class="mt-3">
              <div class="text-xs uppercase tracking-wide text-slate-500 mb-2">Habitaciones disponibles</div>

              <div v-if="roomsStatus === 'pending'" class="py-4 text-slate-600 text-sm">
                <i class="pi pi-spin pi-spinner mr-2"></i> Cargando habitaciones…
              </div>

              <div v-else>
                <div
                  v-for="room in (roomsByHotel[hotel.id] || [])"
                  :key="room.id"
                  class="rounded-lg border border-slate-200 p-3 mb-2 last:mb-0 flex items-center justify-between gap-3"
                >
                  <div>
                    <div class="font-medium">
                      Habitación {{ room.number || room.name || room.id.slice(0, 6) }}
                    </div>
                    <div class="text-sm text-slate-600">
                      <span v-if="room.type">{{ room.type }} · </span>
                      <span v-if="room.capacity">Capacidad {{ room.capacity }}</span>
                    </div>
                  </div>

                  <div class="text-right">
                    <div class="text-xs uppercase tracking-wide text-slate-500">Precio por noche</div>
                    <div class="font-semibold">{{ formatGTQ(room.pricePerNight) }}</div>
                    <div class="text-xs text-slate-500 mt-1">Total estimado ({{ nights }} {{ nights === 1 ? 'noche' : 'noches' }})</div>
                    <div class="font-bold text-slate-900">{{ formatGTQ(roomTotal(room)) }}</div>
                  </div>

                  <div class="flex items-center gap-2 shrink-0">
                    <Button size="small" label="Reservar" icon="pi pi-calendar" @click="goReserve(hotel, room)" />
                  </div>
                </div>

                <div v-if="roomsStatus !== 'error' && (roomsByHotel[hotel.id] || []).length === 0" class="py-4 text-slate-600 text-sm">
                  No hay habitaciones para mostrar.
                </div>
              </div>

              <!-- Card footer actions -->
              <div class="mt-3 flex items-center justify-end gap-2">
                <router-link :to="hotelDetailLink(hotel)">
                  <Button size="small" text icon="pi pi-arrow-right" label="Detalles" />
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import Button from 'primevue/button'
import Tag from 'primevue/tag'
import { toast } from 'vue-sonner'
import { useRoute, useRouter } from 'vue-router'

import { getAllHotels } from '~/lib/api/establishments/hotels'
import { getAvailableRooms } from '~/lib/api/reservations/reservations'

const route = useRoute()
const router = useRouter()

// Dates from query (?start=YYYY-MM-DD&end=YYYY-MM-DD)
const startDate = computed<Date | null>(() => parseYMD(route.query.start as string))
const endDate = computed<Date | null>(() => parseYMD(route.query.end as string))

const startLabel = computed(() => route.query.start || '—')
const endLabel = computed(() => route.query.end || '—')

const nights = computed(() => {
  if (!startDate.value || !endDate.value) return 0
  const ms = endDate.value.getTime() - startDate.value.getTime()
  const d = Math.ceil(ms / (1000 * 60 * 60 * 24))
  return Math.max(0, d)
})

// Hotels
const { state: hotelsState, asyncStatus: hotelsStatus } = useCustomQuery({
  key: ['client_hotels_list'],
  query: () => getAllHotels()
})
const hotels = computed<any[]>(() => (hotelsState.value.data as any[]) ?? [])

type RoomsMap = Record<string, any[]>

// Fetch availability for all hotels at once (grouped client-side)
const { state: roomsState, status: roomsStatus } = useCustomQuery<any[]>({
  key: ['available_rooms', startLabel.value, endLabel.value],
  query: async () => {
    if (!startDate.value || !endDate.value) return []
    const rooms = await getAvailableRooms(startDate.value, endDate.value)
    return rooms ?? []
  }
})

const rooms = computed<any[]>(() => (roomsState.value.data as any[]) ?? [])

const roomsByHotel = computed<RoomsMap>(() => {
  const grouped: RoomsMap = {}
  const list = rooms.value ?? []
  for (const r of list) {
    const hid = r?.hotelId || r?.hotel?.id
    if (!hid) continue
    if (!grouped[hid]) grouped[hid] = []
    grouped[hid].push(r)
  }
  return grouped
})

// Only show hotels that have at least one available room
const visibleHotels = computed<any[]>(() => {
  const map = roomsByHotel.value
  return hotels.value.filter(h => (map[h.id] || []).length > 0)
})
const visibleHotelsCount = computed(() => visibleHotels.value.length)

function roomTotal(room: any) {
  if (!room?.pricePerNight || nights.value <= 0) return null
  return room.pricePerNight * nights.value
}

// Text-only design: no image header

function formatGTQ(v: number | null | undefined) {
  if (v === null || v === undefined || isNaN(Number(v))) return '—'
  try {
    return new Intl.NumberFormat('es-GT', { style: 'currency', currency: 'GTQ', minimumFractionDigits: 2 }).format(Number(v))
  } catch {
    return `Q ${Number(v).toFixed(2)}`
  }
}

function parseYMD(s?: string): Date | null {
  if (!s || typeof s !== 'string') return null
  const m = /^(\d{4})-(\d{2})-(\d{2})$/.exec(s)
  if (!m) return null
  const d = new Date(Number(m[1]), Number(m[2]) - 1, Number(m[3]))
  return isNaN(d.getTime()) ? null : d
}

function ymd(d: Date | null): string {
  if (!d) return ''
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${y}-${m}-${day}`
}

function clearDates() {
  router.replace({ path: '/hoteles' })
}

function goReserve(hotel: any, room: any) {
  if (!startDate.value || !endDate.value) {
    toast.info('Selecciona fechas antes de reservar')
    return
  }
  // Lleva a un flujo de reserva; ajusta la ruta si tienes una página específica de checkout
  router.push({
    path: '/reservar',
    query: {
      hotelId: hotel.id,
      roomId: room.id,
      start: ymd(startDate.value),
      end: ymd(endDate.value),
    }
  })
}

function hotelDetailLink(hotel: any) {
  const query: any = {}
  if (startDate.value && endDate.value) {
    query.start = ymd(startDate.value)
    query.end = ymd(endDate.value)
  }
  return { path: `/hoteles/${hotel.id}`, query }
}
</script>

<style scoped>
/* optional: adjust dialog content spacing on mobile */
</style>
