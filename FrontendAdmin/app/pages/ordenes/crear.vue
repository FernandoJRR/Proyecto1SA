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
import InputText from 'primevue/inputtext'
import Tag from 'primevue/tag'
import { toast } from 'vue-sonner'
import { createClient, getClientByCui } from '~/lib/api/clients/clients'
import { getAllRestaurants } from '~/lib/api/establishments/restaurants'
import { getRestaurantDishes } from '~/lib/api/establishments/restaurants'
import { createOrder, type CreateOrderPayload } from '~/lib/api/orders/orders'

const clientCui = ref('')
const restaurantId = ref('')

// Client lookup & quick register
const client = ref<null | { firstName: string; lastName: string; email: string; cui: string }>(null)
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
const items = ref<{ dishId: string; name: string; quantity: number }[]>([])
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
const dishOptions = ref<{ label: string; value: string }[]>([])

async function onRestaurantChange() {
  items.value = [] // reset when restaurant changes
  selectedDishId.value = ''
  dishOptions.value = []
  if (!restaurantId.value) return
  dishesLoading.value = true
  try {
    const dishes = await getRestaurantDishes(restaurantId.value)
    dishOptions.value = (dishes ?? []).map((d: any) => ({ label: d.name, value: d.id }))
  } catch (e: any) {
    toast.error('No se pudieron cargar los platillos', { description: e?.message })
  } finally {
    dishesLoading.value = false
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
    items.value.push({ dishId: selectedDishId.value, name: dish.label, quantity: Number(qty.value) })
  }
  selectedDishId.value = ''
  qty.value = 1
}

function removeItem(index: number) {
  items.value.splice(index, 1)
}

const submitting = ref(false)

async function onSubmit() {
  if (!clientCui.value || !restaurantId.value || items.value.length === 0) {
    toast.error('Completa CUI, restaurante y agrega al menos un ítem')
    return
  }
  if (!client.value) {
    toast.error('Busca o registra al cliente antes de crear la orden')
    return
  }
  const payload: CreateOrderPayload = {
    clientCui: clientCui.value,
    restaurantId: restaurantId.value,
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
</script>
