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
            <h1 class="text-2xl sm:text-3xl font-extrabold tracking-tight text-slate-900">Mis reservaciones</h1>
            <p class="text-slate-600 text-sm sm:text-base">Ingresa tu CUI para consultar tus reservas.</p>
          </div>
        </div>
      </div>
    </header>

    <!-- Content -->
    <main class="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 pb-16">
      <!-- Search form -->
      <div class="rounded-2xl border border-slate-200 bg-white p-4 sm:p-6">
        <div class="grid grid-cols-1 sm:grid-cols-3 gap-3 items-end">
          <div class="sm:col-span-2">
            <label class="block text-xs font-semibold tracking-wide text-slate-600 mb-1">CUI del cliente</label>
            <InputText
              v-model="clientCui"
              placeholder="Ej: 1234567890101"
              class="w-full"
              :maxlength="13"
              inputmode="numeric"
              pattern="\\d*"
            />
            <p v-if="validationError" class="text-xs text-red-600 mt-1">{{ validationError }}</p>
          </div>
          <div class="flex sm:justify-end">
            <Button :loading="loading" icon="pi pi-search" label="Buscar" @click="onSearch" />
          </div>
        </div>
      </div>

      <!-- Results -->
      <div class="mt-6">
        <div v-if="loading" class="rounded-2xl border border-slate-200 bg-white p-8 text-center text-slate-600">
          <i class="pi pi-spin pi-spinner mr-2"></i> Buscando reservaciones…
        </div>

        <div v-else-if="error" class="rounded-2xl border border-red-200 bg-red-50 p-4 text-red-800">
          {{ error }}
        </div>

        <div v-else-if="reservations.length === 0 && hasSearched" class="rounded-2xl border border-slate-200 bg-white p-8 text-center text-slate-600">
          No se encontraron reservaciones para el CUI ingresado.
        </div>

        <div v-else-if="reservations.length > 0" class="grid grid-cols-1 gap-4">
          <div
            v-for="r in reservations"
            :key="r.id"
            class="rounded-2xl border border-slate-200 bg-white p-4 sm:p-5 shadow-sm"
          >
            <div class="flex items-start justify-between gap-4">
              <div>
                <div class="text-xs uppercase tracking-wide text-slate-500">Hotel</div>
                <div class="text-lg font-semibold text-slate-900">{{ r.hotel?.name || r.hotelId }}</div>
                <div class="text-sm text-slate-600" v-if="r.hotel?.address">{{ r.hotel.address }}</div>
              </div>
              <div class="text-right flex flex-col items-end gap-2">
                <div class="text-xs uppercase tracking-wide text-slate-500">Total</div>
                <div class="font-bold text-slate-900">{{ formatGTQ(r.totalCost) }}</div>
                <router-link :to="{ path: `/mis-reservaciones/${r.id}`, query: { cui: clientCui } }">
                  <Button size="small" icon="pi pi-search" label="Ver detalle" outlined />
                </router-link>
              </div>
            </div>

            <div class="mt-4 grid grid-cols-1 sm:grid-cols-3 gap-3">
              <div class="rounded-lg border border-slate-200 p-3">
                <div class="text-xs uppercase tracking-wide text-slate-500">Fecha de entrada</div>
                <div class="font-medium">{{ formatDate(r.startDate) }}</div>
              </div>
              <div class="rounded-lg border border-slate-200 p-3">
                <div class="text-xs uppercase tracking-wide text-slate-500">Fecha de salida</div>
                <div class="font-medium">{{ formatDate(r.endDate) }}</div>
              </div>
              <div class="rounded-lg border border-slate-200 p-3">
                <div class="text-xs uppercase tracking-wide text-slate-500">Habitación</div>
                <div class="font-medium">{{ roomLabel(r) }}</div>
              </div>
            </div>

            <div class="mt-3 text-sm text-slate-700">
              <div>Subtotal: <strong>{{ formatGTQ(r.subtotal) }}</strong></div>
              <div v-if="r.promotionApplied" class="text-emerald-700">
                Promoción aplicada: {{ r.promotionApplied.name }} ({{ r.promotionApplied.percentOff }}%)
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
import InputText from 'primevue/inputtext'
import { getAllReservationsClient, type Reservation } from '~/lib/api/reservations/reservations'

const route = useRoute()
const clientCui = ref<string>((route.query.cui as string) || '')

const loading = ref(false)
const error = ref('')
const hasSearched = ref(false)
const reservations = ref<Reservation[]>([])

const isValidCui = computed(() => /^\d{13}$/.test(clientCui.value))
const validationError = computed(() => (clientCui.value && !isValidCui.value) ? 'El CUI debe tener 13 dígitos' : '')

async function onSearch() {
  error.value = ''
  hasSearched.value = true
  reservations.value = []
  if (!isValidCui.value) {
    error.value = 'Ingresa un CUI válido de 13 dígitos'
    return
  }
  loading.value = true
  try {
    const data = await getAllReservationsClient(clientCui.value)
    reservations.value = (data as any[]) || []
  } catch (e: any) {
    error.value = e?.message || 'No se pudieron obtener las reservaciones'
  } finally {
    loading.value = false
  }
}

function formatGTQ(v: number | null | undefined) {
  if (v === null || v === undefined || isNaN(Number(v))) return '—'
  try {
    return new Intl.NumberFormat('es-GT', { style: 'currency', currency: 'GTQ', minimumFractionDigits: 2 }).format(Number(v))
  } catch {
    return `Q ${Number(v).toFixed(2)}`
  }
}

function toDate(d: any): Date | null {
  if (!d) return null
  try {
    if (d instanceof Date) return d
    // Try parse ISO or Y-M-D
    const parsed = new Date(d)
    return isNaN(parsed.getTime()) ? null : parsed
  } catch { return null }
}

function formatDate(d: any) {
  const dt = toDate(d)
  if (!dt) return '—'
  try {
    return new Intl.DateTimeFormat('es-GT', { year: 'numeric', month: '2-digit', day: '2-digit' }).format(dt)
  } catch {
    const y = dt.getFullYear()
    const m = String(dt.getMonth() + 1).padStart(2, '0')
    const day = String(dt.getDate()).padStart(2, '0')
    return `${y}-${m}-${day}`
  }
}

function roomLabel(r: any) {
  const room = r?.room
  if (room?.number) return room.number
  if (room?.name) return room.name
  return r?.roomId?.slice?.(0, 6) || '—'
}

// Auto-search if CUI is provided via query
onMounted(() => {
  if (isValidCui.value) onSearch()
})
</script>

<style scoped>
</style>
