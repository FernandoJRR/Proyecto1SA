<template>
  <div class="min-h-screen bg-slate-50">
    <!-- Header -->
    <header class="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link :to="backToHotelsLink">
            <Button icon="pi pi-arrow-left" label="Hoteles" size="small" />
          </router-link>
          <div>
            <h1 class="text-2xl sm:text-3xl font-extrabold tracking-tight text-slate-900">Confirmar reserva</h1>
            <p class="text-slate-600 text-sm sm:text-base">
              <template v-if="startDate && endDate">
                Del <strong>{{ startLabel }}</strong> al <strong>{{ endLabel }}</strong>
                <Tag class="ml-2 align-middle" :value="`${nights} ${nights === 1 ? 'noche' : 'noches'}`" />
              </template>
              <template v-else>
                Completa las fechas para continuar.
              </template>
            </p>
          </div>
        </div>
      </div>
    </header>

    <main class="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 pb-16">
      <!-- Missing info -->
      <div v-if="!hotelId || !roomId || !startDate || !endDate" class="rounded-2xl border border-slate-200 bg-white p-8 text-center">
        <i class="pi pi-info-circle text-4xl text-slate-400"></i>
        <h2 class="mt-3 text-xl font-semibold">Datos incompletos</h2>
        <p class="text-slate-600 mt-1">Vuelve a la lista de hoteles y selecciona una habitación con fechas válidas.</p>
        <router-link :to="{ path: '/hoteles' }" class="mt-4 inline-block">
          <Button icon="pi pi-arrow-left" label="Ir a Hoteles" />
        </router-link>
      </div>

      <!-- Reservation card -->
      <div v-else class="rounded-2xl border border-slate-200 bg-white p-6">
        <div class="flex items-start justify-between gap-4">
          <div>
            <div class="text-xs uppercase tracking-wide text-slate-500">Hotel</div>
            <div class="text-lg font-semibold text-slate-900">
              <template v-if="hotelStatus === 'loading'">
                Cargando hotel…
              </template>
              <template v-else-if="hotelError">
                Error al cargar hotel
              </template>
              <template v-else>
                {{ hotel?.name || '—' }}
              </template>
            </div>
            <div class="text-slate-600 text-sm" v-if="hotel?.address">{{ hotel.address }}</div>
          </div>
          <div class="text-right">
            <div class="text-xs uppercase tracking-wide text-slate-500">Noches</div>
            <div class="font-semibold">{{ nights }}</div>
          </div>
        </div>

        <div class="mt-6 grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div class="rounded-lg border border-slate-200 p-4">
            <div class="text-xs uppercase tracking-wide text-slate-500">Habitación</div>
            <div class="mt-1 font-medium">
              <template v-if="roomStatus === 'loading'">Cargando habitación…</template>
              <template v-else-if="roomError">Error al cargar habitación</template>
              <template v-else>Habitación {{ roomLabel }}</template>
            </div>
            <div class="text-sm text-slate-600" v-if="room">
              <span v-if="room.capacity">Capacidad {{ room.capacity }}</span>
            </div>
          </div>

          <div class="rounded-lg border border-slate-200 p-4">
            <div class="text-xs uppercase tracking-wide text-slate-500">Tarifa</div>
            <div class="mt-1 font-semibold">{{ formatGTQ(room?.pricePerNight ?? null) }}</div>
            <div class="text-xs text-slate-500 mt-1">Subtotal ({{ nights }} {{ nights === 1 ? 'noche' : 'noches' }})</div>
            <div class="font-medium text-slate-900">{{ formatGTQ(subtotal) }}</div>
            <div v-if="appliedPromotion" class="text-xs text-emerald-700 mt-1">
              Promoción aplicada: {{ appliedPromotion.name }} ({{ appliedPromotion.percentage }}%)
            </div>
            <div class="text-xs text-slate-500 mt-2">Total estimado</div>
            <div class="font-bold text-slate-900">{{ formatGTQ(totalCost) }}</div>
          </div>
        </div>

        <div class="mt-6 grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div class="flex flex-col gap-2">
            <label class="text-sm text-slate-700">CUI del cliente</label>
            <div class="flex gap-2">
              <InputText
                v-model="clientCui"
                placeholder="Ej: 1234567890101"
                class="flex-1"
                :maxlength="13"
                inputmode="numeric"
                pattern="\\d*"
              />
              <Button :loading="lookupLoading" label="Buscar" size="small" @click="lookupClient" />
            </div>
            <p v-if="lookupError" class="text-xs text-red-600">{{ lookupError }}</p>
          </div>
        </div>

        <!-- Client result / creation -->
        <div class="mt-4">
          <!-- Selected/Found client -->
          <div v-if="currentClient" class="rounded-lg border border-emerald-200 bg-emerald-50 p-4 flex items-center justify-between">
            <div>
              <div class="text-sm text-emerald-900 font-medium">Cliente seleccionado</div>
              <div class="text-sm text-emerald-800">
                {{ currentClient.firstName }} {{ currentClient.lastName }}
                <span class="text-emerald-700">•</span>
                {{ currentClient.email }}
              </div>
            </div>
            <Button severity="secondary" size="small" outlined label="Cambiar" @click="clearClient" />
          </div>

          <!-- Not found -> creation form -->
          <div v-else-if="lookupAttempted" class="rounded-lg border border-slate-200 p-4 bg-slate-50">
            <div class="text-sm font-medium text-slate-900">No encontramos el cliente. Crea una cuenta:</div>
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 mt-3">
              <div class="flex flex-col gap-2">
                <label class="text-xs text-slate-700">Nombres</label>
                <InputText v-model="newClient.firstName" placeholder="Ej: Juan" />
              </div>
              <div class="flex flex-col gap-2">
                <label class="text-xs text-slate-700">Apellidos</label>
                <InputText v-model="newClient.lastName" placeholder="Ej: Pérez" />
              </div>
              <div class="flex flex-col gap-2 sm:col-span-2">
                <label class="text-xs text-slate-700">Correo electrónico</label>
                <InputText v-model="newClient.email" placeholder="Ej: correo@ejemplo.com" />
              </div>
              <div class="flex flex-col gap-2 sm:col-span-2">
                <label class="text-xs text-slate-700">CUI</label>
                <InputText v-model="newClient.cui" :disabled="true" />
              </div>
            </div>
            <div class="mt-3 flex items-center justify-end gap-2">
              <Button severity="secondary" outlined size="small" label="Cancelar" @click="cancelCreate" />
              <Button size="small" :loading="creating" :disabled="!canCreateClient" label="Crear cliente" @click="createNewClient" />
            </div>
          </div>
        </div>

        <div class="mt-6 flex items-center justify-end gap-2">
          <Button severity="secondary" outlined label="Cancelar" @click="goBack" />
          <Button :disabled="reserveDisabled" :loading="submitting" icon="pi pi-check" label="Confirmar reserva" @click="submit" />
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import Tag from 'primevue/tag'
import { toast } from 'vue-sonner'
import { useRoute, useRouter } from 'vue-router'

import { getHotelById, getHotelRoom } from '~/lib/api/establishments/hotels'
import { createReservation } from '~/lib/api/reservations/reservations'
import { checkReservationPromotionEligibility, type Promotion } from '~/lib/api/promotions/promotions'
import { getClientByCui, createClient, type Client, type CreateClientPayload } from '~/lib/api/clients/clients'

const route = useRoute()
const router = useRouter()

const hotelId = computed(() => (route.query.hotelId as string) || '')
const roomId = computed(() => (route.query.roomId as string) || '')

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

const backToHotelsLink = computed(() => {
  const q: any = {}
  if (startDate.value && endDate.value) {
    q.start = ymd(startDate.value)
    q.end = ymd(endDate.value)
  }
  return { path: '/hoteles', query: q }
})

// Fetch hotel and room details
const { state: hotelState, asyncStatus: hotelStatus, status: syncHotelStatus } = useCustomQuery({
  key: computed(() => ['reserve_hotel', hotelId.value]).value,
  query: async () => hotelId.value ? await getHotelById(hotelId.value) : null
})
const hotel = computed<any>(() => (hotelState.value.data as any) ?? null)
const hotelError = computed(() => syncHotelStatus.value === 'error')

const { state: roomState, asyncStatus: roomStatus, status: syncRoomStatus } = useCustomQuery({
  key: computed(() => ['reserve_room', hotelId.value, roomId.value]).value,
  query: async () => (hotelId.value && roomId.value) ? await getHotelRoom(hotelId.value, roomId.value) : null
})
const room = computed<any>(() => (roomState.value.data as any) ?? null)
const roomError = computed(() => syncRoomStatus.value === 'error')
const roomLabel = computed(() => room.value?.number || room.value?.name || (room.value?.id ? room.value.id.slice(0, 6) : '—'))

const subtotal = computed<number | null>(() => {
  if (!room.value?.pricePerNight || nights.value <= 0) return null
  return room.value.pricePerNight * nights.value
})

const appliedPromotion = ref<Promotion | null>(null)

const totalCost = computed<number | null>(() => {
  if (subtotal.value === null) return null
  const percent = appliedPromotion.value?.percentage ?? 0
  const discount = (subtotal.value * percent) / 100
  return Math.max(0, subtotal.value - discount)
})

// Form state
const clientCui = ref('')
const isValidCui = computed(() => /^\d{13}$/.test(clientCui.value))
const submitting = ref(false)

// Client lookup / creation state
const lookupLoading = ref(false)
const lookupAttempted = ref(false)
const lookupError = ref('')
const currentClient = ref<Client | null>(null)

const creating = ref(false)
const newClient = reactive<CreateClientPayload>({ firstName: '', lastName: '', email: '', cui: '' })
const canCreateClient = computed(() => {
  return !!newClient.firstName && !!newClient.lastName && !!newClient.email && /.+@.+\..+/.test(newClient.email) && /^\d{13}$/.test(newClient.cui)
})

const reserveDisabled = computed(() => {
  return submitting.value || !hotelId.value || !roomId.value || !startDate.value || !endDate.value || nights.value <= 0 || !isValidCui.value || !room.value || !currentClient.value
})

async function lookupClient() {
  lookupError.value = ''
  lookupAttempted.value = false
  currentClient.value = null
  if (!isValidCui.value) {
    lookupError.value = 'El CUI debe tener 13 dígitos'
    return
  }
  lookupLoading.value = true
  try {
    const client = await getClientByCui(clientCui.value)
    currentClient.value = client as any
    lookupAttempted.value = true
    // sync CUI to newClient in case user wants to switch
    newClient.cui = clientCui.value
    toast.success('Cliente encontrado')
    await maybeCheckPromotion()
  } catch (e: any) {
    // Consider not found or other errors; allow creation
    lookupAttempted.value = true
    newClient.cui = clientCui.value
    if (e?.message) {
      // If backend sends 404 message, keep it concise
      lookupError.value = ''
    }
  } finally {
    lookupLoading.value = false
  }
}

function clearClient() {
  currentClient.value = null
}

function cancelCreate() {
  lookupAttempted.value = false
}

async function createNewClient() {
  if (!canCreateClient.value) return
  creating.value = true
  try {
    const created = await createClient({ ...newClient })
    currentClient.value = created as any
    toast.success('Cliente creado correctamente')
    await maybeCheckPromotion()
  } catch (e: any) {
    toast.error(e?.message || 'No se pudo crear el cliente')
  } finally {
    creating.value = false
  }
}

async function maybeCheckPromotion() {
  if (!currentClient.value || !hotelId.value || !roomId.value || !startDate.value || !endDate.value) {
    appliedPromotion.value = null
    return
  }
  try {
    const promo = await checkReservationPromotionEligibility({
      clientId: currentClient.value.id,
      hotelId: hotelId.value,
      roomId: roomId.value,
      startDate: ymd(startDate.value!),
      endDate: ymd(endDate.value!)
    })
    appliedPromotion.value = promo as any
  } catch (_e) {
    appliedPromotion.value = null
  }
}

watch([currentClient, hotelId, roomId, startDate, endDate], () => {
  // Non-blocking; ignore errors
  maybeCheckPromotion()
})

async function submit() {
  if (reserveDisabled.value) return
  submitting.value = true
  try {
    const payload = {
      clientCui: clientCui.value,
      hotelId: hotelId.value,
      roomId: roomId.value,
      startDate: ymd(startDate.value!),
      endDate: ymd(endDate.value!),
      // Include promotionId if a promotion was applied
      ...(appliedPromotion.value?.id ? { promotionId: appliedPromotion.value.id } : {})
    }
    await createReservation(payload)
    toast.success('Reserva creada correctamente')
    router.replace(backToHotelsLink.value)
  } catch (e: any) {
    toast.error(e?.message || 'No se pudo crear la reserva')
  } finally {
    submitting.value = false
  }
}

function goBack() {
  router.push(backToHotelsLink.value)
}

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
</script>

<style scoped>
</style>
