<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Page Header -->
    <header class="max-w-7xl mx-auto mb-6" role="banner">
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link to="/admin/restaurantes">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver a Restaurantes" />
          </router-link>
          <div>
            <div class="flex items-center gap-3">
              <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">
                {{ restaurantName }}
              </h1>
              <Tag v-if="isDisabled" severity="danger" value="Desactivado" />
            </div>
            <p class="text-slate-600 text-sm">ID: <span class="font-mono">{{ restaurant?.id }}</span></p>
          </div>
        </div>
        <div class="flex items-center gap-2">
          <router-link :to="`/admin/restaurantes/editar-${restaurant?.id}`">
            <Button icon="pi pi-pencil" label="Editar" severity="warn" outlined />
          </router-link>
          <router-link v-if="!isDisabled" :to="`/admin/restaurantes/deshabilitar-${restaurant?.id}`">
            <Button icon="pi pi-ban" label="Deshabilitar" severity="danger" outlined />
          </router-link>
          <router-link v-else :to="`/admin/restaurantes/reactivar-${restaurant?.id}`">
            <Button icon="pi pi-refresh" label="Reactivar" severity="help" outlined />
          </router-link>
        </div>
      </div>
    </header>

    <!-- Content -->
    <main class="max-w-7xl mx-auto" role="main">
      <div v-if="state.status === 'pending'" class="py-16 text-center text-slate-600">Cargando restaurante…</div>
      <div v-else-if="state.status === 'error'" class="py-16 text-center text-red-600">Ocurrió un error inesperado.</div>

      <div v-else class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <!-- Card: Información principal del restaurante -->
        <section class="lg:col-span-1 rounded-xl border border-slate-200 bg-white shadow">
          <div class="p-6">
            <h2 class="text-lg font-semibold mb-4">Información</h2>
            <div class="space-y-3">
              <div>
                <p class="text-xs uppercase tracking-wide text-slate-500">Nombre</p>
                <p class="font-medium">{{ restaurantName }}</p>
              </div>
              <div>
                <p class="text-xs uppercase tracking-wide text-slate-500">Dirección</p>
                <p class="font-medium">{{ dash(restaurant?.address) }}</p>
              </div>
              <div>
                <p class="text-xs uppercase tracking-wide text-slate-500">Estado</p>
                <div class="flex items-center gap-2">
                  <Tag v-if="!isDisabled" value="Activo" severity="success" />
                  <Tag v-else value="Desactivado" severity="danger" />
                </div>
              </div>
            </div>
          </div>
        </section>

        <!-- Card: Preview del hotel (si aplica) -->
        <section v-if="restaurant?.hotelId" class="lg:col-span-2 rounded-xl border border-slate-200 bg-white shadow">
          <div class="p-6">
            <div class="flex items-center justify-between mb-2">
              <h2 class="text-lg font-semibold">Hotel asociado</h2>
              <router-link :to="`/admin/hoteles/${restaurant?.hotelId}`">
                <Button icon="pi pi-external-link" label="Ver hotel" rounded />
              </router-link>
            </div>
            <div v-if="hotel" class="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <p class="text-xs uppercase tracking-wide text-slate-500">Nombre</p>
                <p class="font-medium">{{ hotel.name }}</p>
              </div>
              <div>
                <p class="text-xs uppercase tracking-wide text-slate-500">Dirección</p>
                <p class="font-medium">{{ dash(hotel.address) }}</p>
              </div>
            </div>
            <div v-else class="text-slate-600">Cargando información del hotel…</div>
          </div>
        </section>

        <!-- Card: Platillos del restaurante -->
        <section class="lg:col-span-2 rounded-xl border border-slate-200 bg-white shadow">
          <div class="p-6">
            <div class="flex items-center justify-between mb-4">
              <h2 class="text-lg font-semibold">Platillos</h2>
              <router-link :to="`/admin/restaurantes/${restaurant?.id}/platillos/crear`">
                <Button icon="pi pi-plus" label="Nuevo platillo" rounded />
              </router-link>
            </div>

            <DataTable
              :value="dishesState.data as any[]"
              tableStyle="min-width: 56rem"
              stripedRows
              rowHover
              :loading="dishesAsyncStatus == 'loading'"
              :paginator="true"
              :rows="10"
              :rowsPerPageOptions="[10,20,50]"
            >
              <Column header="Nombre">
                <template #body="slotProps">
                  {{ slotProps.data.name }}
                </template>
              </Column>
              <Column header="Precio">
                <template #body="slotProps">
                  {{ formatGTQ(slotProps.data.price) }}
                </template>
              </Column>
              <Column header="Acciones">
                <template #body="slotProps">
                  <div class="flex flex-wrap items-center gap-1">
                    <router-link :to="`/admin/restaurantes/${restaurant?.id}/platillos/${slotProps.data.id}`">
                      <Button label="Ver" severity="info" variant="text" rounded />
                    </router-link>
                    <router-link :to="`/admin/restaurantes/${restaurant?.id}/platillos/editar-${slotProps.data.id}`">
                      <Button label="Editar" severity="warn" variant="text" rounded />
                    </router-link>
                  </div>
                </template>
              </Column>

              <template #empty>
                <div class="py-10 text-center text-slate-600">
                  <i class="pi pi-inbox text-3xl mb-2 text-slate-400" aria-hidden="true"></i>
                  <div>Este restaurante no tiene platillos registrados.</div>
                </div>
              </template>

              <template #loading>
                <div class="py-10 text-center text-slate-600">
                  Cargando platillos…
                </div>
              </template>

              <template #footer>
                Hay en total {{ dishesState.data ? (dishesState.data as any[]).length : 0 }} platillos.
              </template>
            </DataTable>
          </div>
        </section>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watchEffect } from 'vue'
import { getHotelById } from '~/lib/api/establishments/hotels'
import { getRestaurantById, getRestaurantDishes } from '~/lib/api/establishments/restaurants'

// Restaurant detail
const { state } = useCustomQuery({
  key: ['restaurant', useRoute().params.id as string],
  query: () => getRestaurantById(useRoute().params.id as string).then((res) => ({ restaurant: res })),
})

// Dishes for this restaurant
const { state: dishesState, asyncStatus: dishesAsyncStatus } = useCustomQuery({
  key: ['restaurantDishes', useRoute().params.id as string],
  query: () => getRestaurantDishes(useRoute().params.id as string),
})

// Optional hotel preview
const hotel = ref<any | null>(null)
watchEffect(async () => {
  const r: any = state.value.data?.restaurant
  if (!r?.hotelId) {
    hotel.value = null
    return
  }
  try {
    hotel.value = await getHotelById(r.hotelId)
  } catch {
    hotel.value = null
  }
})

const restaurant = computed(() => state.value.data?.restaurant as any)
const restaurantName = computed(() => restaurant.value?.name || '')
const isDisabled = computed(() => !!restaurant.value?.desactivatedAt)

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

function dash(v?: string) { return v || '—' }
</script>
