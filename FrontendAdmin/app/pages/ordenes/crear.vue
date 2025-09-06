<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Header -->
    <header class="max-w-4xl mx-auto mb-6" role="banner">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link to="/ordenes">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver a Órdenes" />
          </router-link>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Crear Orden</h1>
        </div>
      </div>
    </header>

    <!-- Form Card -->
    <main class="max-w-4xl mx-auto" role="main">
      <div class="rounded-2xl border border-slate-200 bg-white shadow">
        <div class="p-6 sm:p-8">
          <!-- Datos principales -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
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

            <div class="md:col-span-2">
              <label class="block text-xs uppercase tracking-wide text-slate-500 mb-2" for="restaurant">Restaurante</label>
              <Dropdown
                id="restaurant"
                v-model="restaurantId"
                :options="restaurantOptions"
                optionLabel="label"
                optionValue="value"
                placeholder="Selecciona un restaurante"
                class="w-full"
                @change="onRestaurantChange"
              />
            </div>
          </div>

          <!-- Fecha de orden -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mt-2">
            <div class="md:col-span-2">
              <label class="block text-xs uppercase tracking-wide text-slate-500 mb-2" for="orderedAt">Fecha de la orden</label>
              <Calendar id="orderedAt" v-model="orderedAt" dateFormat="yy-mm-dd" class="w-full" :manualInput="true" />
              <p class="mt-1 text-xs text-slate-500">Usada para evaluar promociones aplicables en esta fecha.</p>
            </div>
          </div>

          <!-- Items -->
          <div class="mt-8">
            <h2 class="text-lg font-semibold mb-3">Ítems de la orden</h2>

            <div class="grid grid-cols-1 sm:grid-cols-3 gap-4 items-end">
              <div class="sm:col-span-2">
                <label class="block text-xs uppercase tracking-wide text-slate-500 mb-2" for="dish">Platillo</label>
                <Dropdown
                  id="dish"
                  v-model="selectedDishId"
                  :options="dishOptions"
                  optionLabel="label"
                  optionValue="value"
                  :disabled="!restaurantId || dishesLoading"
                  placeholder="Selecciona un platillo"
                  class="w-full"
                />
              </div>
              <div>
                <FloatLabel>
                  <label for="qty">Cantidad</label>
                  <InputNumber id="qty" v-model="qty" :min="1" :useGrouping="false" class="w-full" />
                </FloatLabel>
              </div>
            </div>

            <div class="mt-3 flex justify-end">
              <Button label="Agregar" icon="pi pi-plus" :disabled="!canAddItem" @click="addItem" />
            </div>

            <div class="mt-4 overflow-x-auto rounded-lg border border-slate-200">
              <DataTable :value="items" tableStyle="min-width: 48rem" stripedRows rowHover>
                <Column header="#">
                  <template #body="{ index }">{{ index + 1 }}</template>
                </Column>
                <Column header="Platillo">
                  <template #body="{ data }">{{ data.name }}</template>
                </Column>
                <Column header="Cantidad">
                  <template #body="{ data }">{{ data.quantity }}</template>
                </Column>
                <Column header="Precio">
                  <template #body="{ data }">{{ formatGTQ(data.price ?? 0) }}</template>
                </Column>
                <Column header="Importe">
                  <template #body="{ data }">{{ formatGTQ((data.price ?? 0) * data.quantity) }}</template>
                </Column>
                <Column header="Acciones">
                  <template #body="{ index }">
                    <Button icon="pi pi-trash" text severity="danger" @click="removeItem(index)" />
                  </template>
                </Column>
                <template #empty>
                  <div class="py-6 text-center text-slate-600">No hay ítems agregados.</div>
                </template>
              </DataTable>
            </div>

          <div class="mt-4 rounded-lg border border-slate-200 bg-slate-50 p-4">
            <div class="flex flex-col sm:flex-row sm:items-center sm:justify-end gap-4">
              <div class="text-right">
                <p class="text-xs uppercase tracking-wide text-slate-500">Subtotal</p>
                <p class="text-base font-semibold">{{ formatGTQ(subtotal) }}</p>
              </div>
              <div class="text-right" v-if="!promoEligible">
                <p class="text-xs uppercase tracking-wide text-slate-500">Total</p>
                <p class="text-lg font-extrabold text-slate-900">{{ formatGTQ(total) }}</p>
              </div>
              <div class="text-right" v-else>
                <p class="text-xs uppercase tracking-wide text-slate-500">Total</p>
                <p class="text-lg font-extrabold text-slate-900">{{ formatGTQ(totalWithPromo) }}</p>
              </div>
            </div>

            <div v-if="promoChecking" class="mt-3 flex items-center gap-2 text-slate-600">
              <i class="pi pi-spin pi-spinner"></i>
              <span class="text-sm">Buscando promociones disponibles…</span>
            </div>

            <div v-if="promoEligible" class="mt-3 flex flex-wrap items-center justify-between gap-3 pt-2 border-t border-slate-200">
              <div class="flex items-center gap-2">
                <Tag severity="success" :value="promoEligible?.name ? `Promoción: ${promoEligible.name}` : 'Promoción disponible'" class="text-[11px] px-2 py-0.5" />
                <span v-if="promoEligible?.percentage" class="text-sm text-emerald-700 font-medium">-{{ promoEligible.percentage }}%</span>
              </div>
              <div class="text-right">
                <p class="text-xs uppercase tracking-wide text-slate-500">Ahorro</p>
                <p class="text-base font-semibold">
                  {{ formatGTQ(Math.max(0, total - totalWithPromo)) }}
                </p>
              </div>
            </div>

            <p class="mt-2 text-xs text-slate-500">* El total estimado; el cálculo final puede considerar promociones.</p>
          </div>

          </div>

          <!-- Actions -->
          <div class="mt-8 flex items-center justify-end gap-2">
            <router-link to="/ordenes">
              <Button label="Cancelar" severity="secondary" outlined />
            </router-link>
            <Button label="Crear orden" icon="pi pi-save" :loading="submitting" :disabled="!canSubmitOrder" @click="onSubmit" />
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import Dropdown from 'primevue/dropdown'
import InputNumber from 'primevue/inputnumber'
import Calendar from 'primevue/calendar'
import InputText from 'primevue/inputtext'
import Tag from 'primevue/tag'
import { toast } from 'vue-sonner'
import { createClient, getClientByCui, type Client } from '~/lib/api/clients/clients'
import { getAllRestaurants } from '~/lib/api/establishments/restaurants'
import { getRestaurantDishes } from '~/lib/api/establishments/restaurants'
import { createOrder, type CreateOrderPayload, type OrderItem } from '~/lib/api/orders/orders'
import { checkOrderPromotionEligibility, type Promotion } from '~/lib/api/promotions/promotions'

const clientCui = ref('')
const restaurantId = ref('')
const orderedAt = ref<Date | null>(new Date())

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
    // assuming 200 returns the Client object
    client.value = res as any
    // normalize CUI back into the field
    if (client?.value?.cui) clientCui.value = client.value.cui
  } catch (e: any) {
    // if not found (e.g., 404), keep client as null and let user register
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

const canSubmitOrder = computed(() => !!clientCui.value && !!restaurantId.value && items.value.length > 0 && !!client)

// Items state
const items = ref<OrderItem[]>([])
const selectedDishId = ref('')
const qty = ref(1)

// Restaurants options
const { state: restaurantsState, asyncStatus: restaurantsAsync } = useCustomQuery({
  key: ['orderRestaurants'],
  query: () => getAllRestaurants(),
})

const restaurantOptions = computed(() => (restaurantsState.value.data ?? []).map((r: any) => ({ label: r.name, value: r.id })))

// Dishes for selected restaurant
const dishesLoading = ref(false)
const dishOptions = ref<{ label: string; value: string; price?: number }[]>([])

async function onRestaurantChange() {
  items.value = [] // reset when restaurant changes
  selectedDishId.value = ''
  dishOptions.value = []
  if (!restaurantId.value) return
  dishesLoading.value = true
  try {
    const dishes = await getRestaurantDishes(restaurantId.value)
    dishOptions.value = (dishes ?? []).map((d: any) => ({ label: d.name, value: d.id, price: d.price }))
  } catch (e: any) {
    toast.error('No se pudieron cargar los platillos', { description: e?.message })
  } finally {
    dishesLoading.value = false
    promoEligible.value = null
  }
}

const canAddItem = computed(() => !!selectedDishId.value && qty.value > 0)

function addItem() {
  if (!canAddItem.value) return
  const dish = dishOptions.value.find(d => d.value === selectedDishId.value)
  if (!dish) return

  // if exists, sum quantity
  const existing = items.value.find(i => i.dishId === selectedDishId.value)
  if (existing) {
    existing.quantity += Number(qty.value)
  } else {
    items.value.push({ dishId: selectedDishId.value, name: dish.label, quantity: Number(qty.value), price: dish.price ?? 0 })
  }
  selectedDishId.value = ''
  qty.value = 1
}

function removeItem(index: number) {
  items.value.splice(index, 1)
}

const subtotal = computed(() => items.value.reduce((acc, it) => acc + (Number(it.price || 0) * Number(it.quantity || 0)), 0))
const total = computed(() => subtotal.value) // sin promociones aquí; se calcula en backend si aplica

function formatGTQ(v: number | null | undefined) {
  if (v === null || v === undefined) return '—'
  try {
    return new Intl.NumberFormat('es-GT', { style: 'currency', currency: 'GTQ', minimumFractionDigits: 2 }).format(v)
  } catch {
    return `Q.${Number(v).toFixed(2)}`
  }
}

// --- Promotion eligibility for ORDERS (silent if none / 404) ---
const promoChecking = ref(false)
const promoEligible = ref<null | Promotion>(null)

function fmtYmd(d: Date | null | undefined): string {
  const date = d instanceof Date ? d : new Date()
  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${y}-${m}-${day}`
}

const totalWithPromo = computed<number>(() => {
  if (!promoEligible.value) return total.value
  const pct = typeof promoEligible.value.percentage === 'number' ? promoEligible.value.percentage : 0
  return Math.max(0, total.value * (1 - pct / 100))
})

// React to changes: when client, restaurant and at least 1 dish exist, check eligibility
let promoDebounce: any = null
watch([client, restaurantId, items], () => {
  // reset promo if prerequisites missing
  if (!client.value || !restaurantId.value || items.value.length === 0) {
    promoEligible.value = null
    return
  }
  // debounce to avoid spamming API on rapid edits
  if (promoDebounce) clearTimeout(promoDebounce)
  promoDebounce = setTimeout(async () => {
    promoChecking.value = true
    try {
      const dishesIds = items.value.map(i => i.dishId)
      const payload = {
        clientId: (client.value as any).id,
        restaurantId: restaurantId.value,
        dishesIds,
        orderedAt: fmtYmd(new Date()),
      }
      const promo = await checkOrderPromotionEligibility(payload as any)
      promoEligible.value = promo ?? null
    } catch (e: any) {
      // 404 => no applicable promotion (silent)
      promoEligible.value = null
    } finally {
      promoChecking.value = false
    }
  }, 500)
}, { deep: true })

const submitting = ref(false)

async function onSubmit() {
  if (!clientCui.value || !restaurantId.value || !orderedAt.value || items.value.length === 0) {
    toast.error('Completa CUI, restaurante, fecha de orden y agrega al menos un ítem')
    return
  }
  if (!client.value) {
    toast.error('Busca o registra al cliente antes de crear la orden')
    return
  }
  const payload: CreateOrderPayload = {
    clientCui: clientCui.value,
    restaurantId: restaurantId.value,
    promotionId: promoEligible.value?.id,
    orderedAt: orderedAt.value,
    createOrderItemRequests: items.value.map(i => ({ dishId: i.dishId, quantity: i.quantity })),
  }
  submitting.value = true
  try {
    await createOrder(payload)
    toast.success('Orden creada correctamente')
    navigateTo('/ordenes')
  } catch (error: any) {
    toast.error('Error al crear la orden', { description: error?.message })
  } finally {
    submitting.value = false
  }
}

watch([client, restaurantId, items, orderedAt], () => {
  if (!client.value || !restaurantId.value || items.value.length === 0 || !orderedAt.value) {
    // skip if missing prerequisites
    // (implementation of promotions update omitted as it was not in original code)
    return
  }
  // Example payload for promotions might look like this:
  const dishesIds = items.value.map(i => i.dishId)
  const payload = {
    clientId: (client.value as any).id,
    restaurantId: restaurantId.value,
    dishesIds,
    orderedAt: fmtYmd(orderedAt.value || new Date()),
  }
  // (implementation of promotions update omitted as it was not in original code)
})
</script>
