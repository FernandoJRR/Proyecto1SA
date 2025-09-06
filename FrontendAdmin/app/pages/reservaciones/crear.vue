<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Header -->
    <header class="max-w-4xl mx-auto mb-6" role="banner">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link to="/reservaciones">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver a Reservaciones" />
          </router-link>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Crear Reservación</h1>
        </div>
      </div>
    </header>

    <!-- Form Card -->
    <main class="max-w-4xl mx-auto" role="main">
      <div class="rounded-2xl border border-slate-200 bg-white shadow">
        <div class="p-6 sm:p-8">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- CUI -->
            <div class="md:col-span-2">
              <FloatLabel>
                <label for="clientCui">CUI del cliente</label>
                <InputText id="clientCui" v-model.trim="clientCui" type="text" inputmode="numeric" class="w-full" autocomplete="off" />
              </FloatLabel>
            </div>
            <div class="mt-2 flex items-center gap-2 md:col-span-2">
              <Button icon="pi pi-search" label="Buscar cliente" :loading="clientLoading" @click="onLookupClient" />
              <Button label="Limpiar" severity="secondary" text @click="clearClient" />
              <span v-if="client" class="inline-flex items-center gap-2 text-sm text-emerald-700">
                <i class="pi pi-check-circle"></i>
                {{ client.firstName }} {{ client.lastName }} · {{ client.email }}
                <Tag value="Encontrado" severity="success" class="text-xs px-2 py-0.5" />
              </span>
              <span v-else-if="clientLookupTried && !clientLoading" class="text-sm text-slate-600">Cliente no encontrado. Puedes registrarlo abajo.</span>
            </div>

            <!-- Registro rápido de cliente (solo si no existe) -->
            <div v-if="clientLookupTried && !client && !clientLoading" class="md:col-span-2 mt-4 rounded-xl border border-slate-200 bg-slate-50 p-4">
              <h3 class="text-sm font-semibold mb-3">Registrar cliente</h3>
              <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
                <div>
                  <FloatLabel>
                    <label for="firstName">Nombre</label>
                    <InputText id="firstName" v-model.trim="clientDraft.firstName" class="w-full" autocomplete="off" />
                  </FloatLabel>
                </div>
                <div>
                  <FloatLabel>
                    <label for="lastName">Apellido</label>
                    <InputText id="lastName" v-model.trim="clientDraft.lastName" class="w-full" autocomplete="off" />
                  </FloatLabel>
                </div>
                <div>
                  <FloatLabel>
                    <label for="email">Email</label>
                    <InputText id="email" v-model.trim="clientDraft.email" class="w-full" autocomplete="off" />
                  </FloatLabel>
                </div>
              </div>
              <div class="mt-3 flex justify-end gap-2">
                <Button label="Registrar cliente" icon="pi pi-user-plus" :loading="registeringClient" @click="onRegisterClient" />
              </div>
            </div>

            <!-- Hotel -->
            <div class="md:col-span-1">
              <label class="block text-xs uppercase tracking-wide text-slate-500 mb-2" for="hotel">Hotel</label>
              <Dropdown
                id="hotel"
                v-model="hotelId"
                :options="hotelOptions"
                optionLabel="label"
                optionValue="value"
                placeholder="Selecciona un hotel"
                class="w-full"
                @change="onHotelChange"
              />
            </div>

            <!-- Habitación -->
            <div class="md:col-span-1">
              <label class="block text-xs uppercase tracking-wide text-slate-500 mb-2" for="room">Habitación</label>
              <Dropdown
                id="room"
                v-model="roomId"
                :options="roomOptions"
                optionLabel="label"
                optionValue="value"
                :disabled="!hotelId || roomsLoading"
                placeholder="Selecciona una habitación"
                class="w-full"
              />
            </div>

            <!-- Fechas -->
            <div>
              <label class="block text-xs uppercase tracking-wide text-slate-500 mb-2" for="start">Inicio</label>
              <Calendar id="start" v-model="startDate" dateFormat="yy-mm-dd" class="w-full" :manualInput="true" />
            </div>
            <div>
              <label class="block text-xs uppercase tracking-wide text-slate-500 mb-2" for="end">Fin</label>
              <Calendar id="end" v-model="endDate" dateFormat="yy-mm-dd" class="w-full" :manualInput="true" />
            </div>

            <!-- Resumen de precio -->
            <div class="md:col-span-2 rounded-lg border border-slate-200 bg-slate-50 p-4">
              <div class="grid grid-cols-1 sm:grid-cols-3 gap-4 items-center">
                <div>
                  <p class="text-xs uppercase tracking-wide text-slate-500">Precio por noche</p>
                  <p class="text-base font-semibold">{{ selectedRoomPrice != null ? formatGTQ(selectedRoomPrice) : '—' }}</p>
                </div>
                <div>
                  <p class="text-xs uppercase tracking-wide text-slate-500">Noches</p>
                  <p class="text-base font-semibold">{{ nights || '—' }}</p>
                </div>
                <div>
                  <p class="text-xs uppercase tracking-wide text-slate-500">Total estimado</p>
                  <p class="text-lg font-extrabold text-slate-900">{{ estimatedTotal != null ? formatGTQ(estimatedTotal) : '—' }}</p>
                </div>
                <div v-if="promoEligible" class="sm:col-span-3 flex flex-wrap items-center justify-between gap-3 pt-2 border-t border-slate-200 mt-2">
                  <div class="flex items-center gap-2">
                    <Tag severity="success" :value="promoEligible?.name ? `Promoción: ${promoEligible.name}` : 'Promoción disponible'" class="text-[11px] px-2 py-0.5" />
                    <span v-if="promoEligible?.percentage" class="text-sm text-emerald-700 font-medium">-{{ promoEligible.percentage }}%</span>
                  </div>
                  <div>
                    <p class="text-xs uppercase tracking-wide text-slate-500 text-right">Total con promoción</p>
                    <p class="text-base font-extrabold text-slate-900">{{ estimatedTotalWithPromo != null ? formatGTQ(estimatedTotalWithPromo) : '—' }}</p>
                  </div>
                </div>
              </div>
              <p class="mt-2 text-xs text-slate-500">* El total se calcula como (precio por noche × noches). Descuentos pueden variar al confirmar.</p>
            </div>
          </div>

          <!-- Actions -->
          <div class="mt-8 flex items-center justify-end gap-2">
            <router-link to="/reservaciones">
              <Button label="Cancelar" severity="secondary" outlined />
            </router-link>
            <Button label="Crear reservación" icon="pi pi-save" :loading="submitting" :disabled="!canSubmitReservation" @click="onSubmit" />
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import Dropdown from 'primevue/dropdown'
import Calendar from 'primevue/calendar'
import InputText from 'primevue/inputtext'
import Tag from 'primevue/tag'
import { toast } from 'vue-sonner'
import { createReservation, type CreateReservationPayload } from '~/lib/api/reservations/reservations'
import { getAllHotels, getHotelRooms } from '~/lib/api/establishments/hotels'
import { createClient, getClientByCui, type Client } from '~/lib/api/clients/clients'
import { checkReservationPromotionEligibility, type Promotion } from '~/lib/api/promotions/promotions'

// State
const clientCui = ref('')
const hotelId = ref('')
const roomId = ref('')
const startDate = ref<Date | null>(null)
const endDate = ref<Date | null>(null)

// Client lookup & quick register
const client = ref<null | Client>(null)
const clientLoading = ref(false)
const clientLookupTried = ref(false)
const registeringClient = ref(false)
const clientDraft = reactive({ firstName: '', lastName: '', email: '' })

async function onLookupClient() {
  clientLookupTried.value = true
  clientLoading.value = true
  client.value = null
  try {
    const res = await getClientByCui(clientCui.value)
    client.value = res as Client
    if (client?.value?.cui) clientCui.value = client.value.cui
  } catch (e: any) {
    client.value = null
  } finally {
    clientLoading.value = false
  }
}

function clearClient() {
  client.value = null
  clientLookupTried.value = false
  clientDraft.firstName = ''
  clientDraft.lastName = ''
  clientDraft.email = ''
}

async function onRegisterClient() {
  if (!clientCui.value) {
    toast.error('Ingresa el CUI del cliente para registrarlo')
    return
  }
  if (!clientDraft.firstName || !clientDraft.lastName || !clientDraft.email) {
    toast.error('Completa nombre, apellido y email')
    return
  }
  try {
    registeringClient.value = true
    const newClient = await createClient({
      firstName: clientDraft.firstName,
      lastName: clientDraft.lastName,
      email: clientDraft.email,
      cui: clientCui.value,
    })
    client.value = newClient as any
    toast.success('Cliente registrado')
  } catch (e: any) {
    toast.error('No se pudo registrar el cliente', { description: e?.message })
  } finally {
    registeringClient.value = false
  }
}

// Hotels
const { state: hotelsState } = useCustomQuery({ key: ['hotelsForReservation'], query: () => getAllHotels() })
const hotelOptions = computed(() => (hotelsState.value.data ?? []).map((h: any) => ({ label: h.name, value: h.id })))

// Rooms
const roomsLoading = ref(false)
const roomOptions = ref<{ label: string; value: string; price?: number }[]>([])

async function onHotelChange() {
  roomId.value = ''
  roomOptions.value = []
  if (!hotelId.value) return
  roomsLoading.value = true
  try {
    const rooms = await getHotelRooms(hotelId.value)
    roomOptions.value = (rooms ?? []).map((r: any) => ({ label: r.number ?? r.id, value: r.id, price: r.pricePerNight }))
  } catch (e: any) {
    toast.error('No se pudieron cargar las habitaciones', { description: e?.message })
  } finally {
    roomsLoading.value = false
  }
}

const selectedRoomPrice = computed<number | null>(() => {
  const opt = roomOptions.value.find(o => o.value === roomId.value)
  return (opt && typeof opt.price === 'number') ? opt.price : null
})

const nights = computed<number>(() => {
  if (!startDate.value || !endDate.value) return 0
  const ms = endDate.value.getTime() - startDate.value.getTime()
  const d = Math.ceil(ms / (1000 * 60 * 60 * 24))
  return Math.max(0, d)
})

const estimatedTotal = computed<number | null>(() => {
  if (!selectedRoomPrice.value || nights.value <= 0) return null
  return selectedRoomPrice.value * nights.value
})

// --- Promotion eligibility (silent if none / 404) ---
const promoChecking = ref(false)
const promoEligible = ref<null | Promotion>(null)

function fmtYmd(d: Date | null): string {
  if (!d) return ''
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${y}-${m}-${day}`
}

const canSubmitBase = computed(() => !!clientCui.value && !!hotelId.value && !!roomId.value && !!startDate.value && !!endDate.value)
const canSubmitReservation = computed(() => canSubmitBase.value && !!client)

function fmt(d: Date | null): string {
  if (!d) return ''
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${y}-${m}-${day}`
}

const submitting = ref(false)
async function onSubmit() {
  if (!canSubmitReservation.value) {
    toast.error('Completa todos los campos requeridos')
    return
  }
  if (!client.value) {
    toast.error('Busca o registra al cliente antes de crear la reservación')
    return
  }
  // basic date validation: start < end (al menos 1 noche)
  if (startDate.value && endDate.value) {
    if (startDate.value >= endDate.value) {
      toast.error('La fecha de fin debe ser posterior a la fecha de inicio (mínimo 1 noche)')
      return
    }
  }
  const payload: CreateReservationPayload = {
    clientCui: clientCui.value,
    hotelId: hotelId.value,
    roomId: roomId.value,
    startDate: fmt(startDate.value),
    endDate: fmt(endDate.value),
    promotionId: promoEligible.value?.id
  }
  submitting.value = true
  try {
    await createReservation(payload)
    toast.success('Reservación creada correctamente')
    navigateTo('/reservaciones')
  } catch (error: any) {
    toast.error('Error al crear la reservación', { description: error?.message })
  } finally {
    submitting.value = false
  }
}

// React to changes in key fields (debounced)
watch([client, hotelId, roomId, startDate, endDate, selectedRoomPrice], () => {
  promoEligible.value = null
  if (!client.value || !hotelId.value || !roomId.value || !startDate.value || !endDate.value || !selectedRoomPrice.value) {
    return
  }
  promoChecking.value = true
  checkReservationPromotionEligibility({
    clientId: client.value.id,
    hotelId: hotelId.value,
    roomId: roomId.value,
    startDate: fmtYmd(startDate.value),
    endDate: fmtYmd(endDate.value),
  }).then(promo => {
    promoEligible.value = promo ?? null
  }).catch(() => {
    promoEligible.value = null
  }).finally(() => {
    promoChecking.value = false
  })
})

const estimatedTotalWithPromo = computed<number | null>(() => {
  if (estimatedTotal.value == null) return null
  if (!promoEligible.value) return null
  // Percent off takes precedence if present
  if (typeof promoEligible.value.percentage === 'number') {
    return Math.max(0, estimatedTotal.value * (1 - promoEligible.value.percentage / 100))
  }
  return null
})

function formatGTQ(v: number | null | undefined) {
  if (v === null || v === undefined) return '—'
  try {
    return new Intl.NumberFormat('es-GT', { style: 'currency', currency: 'GTQ', minimumFractionDigits: 2 }).format(v)
  } catch {
    return `Q.${Number(v).toFixed(2)}`
  }
}
</script>
