<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Page Header -->
    <header class="max-w-7xl mx-auto mb-6" role="banner">
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link to="/admin/hoteles">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver a Hoteles" />
          </router-link>
          <div>
            <div class="flex items-center gap-3">
              <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">
                {{ hotelName }}
              </h1>
              <Tag v-if="isDisabled" severity="danger" value="Desactivado" />
            </div>
            <p class="text-slate-600 text-sm">ID: <span class="font-mono">{{ state.data?.hotel?.id }}</span></p>
          </div>
        </div>
        <div class="flex items-center gap-2">
          <router-link :to="`/admin/hoteles/editar-${state.data?.hotel?.id}`">
            <Button icon="pi pi-pencil" label="Editar" severity="warn" outlined />
          </router-link>
          <router-link v-if="!isDisabled" :to="`/admin/hoteles/deshabilitar-${state.data?.hotel?.id}`">
            <Button icon="pi pi-ban" label="Deshabilitar" severity="danger" outlined />
          </router-link>
          <router-link v-else :to="`/admin/hoteles/reactivar-${state.data?.hotel?.id}`">
            <Button icon="pi pi-refresh" label="Reactivar" severity="help" outlined />
          </router-link>
        </div>
      </div>
    </header>

    <!-- Content -->
    <main class="max-w-7xl mx-auto" role="main">
      <div v-if="state.status === 'pending'" class="py-16 text-center text-slate-600">Cargando hotel…</div>
      <div v-else-if="state.status === 'error'" class="py-16 text-center text-red-600">Ocurrió un error inesperado.</div>

      <div v-else class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <!-- Card: Información principal del hotel -->
        <section class="lg:col-span-1 rounded-xl border border-slate-200 bg-white shadow">
          <div class="p-6">
            <h2 class="text-lg font-semibold mb-4">Información</h2>
            <div class="space-y-3">
              <div>
                <p class="text-xs uppercase tracking-wide text-slate-500">Nombre</p>
                <p class="font-medium">{{ hotelName }}</p>
              </div>
              <div>
                <p class="text-xs uppercase tracking-wide text-slate-500">Dirección</p>
                <p class="font-medium">{{ state.data.hotel.address || '—' }}</p>
              </div>
              <div>
                <p class="text-xs uppercase tracking-wide text-slate-500">Costo de mantenimiento (semanal)</p>
                <p class="font-medium">{{ maintenanceFormatted }}</p>
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

        <!-- Card: Habitaciones del hotel -->
        <section class="lg:col-span-2 rounded-xl border border-slate-200 bg-white shadow">
          <div class="p-6">
            <div class="flex items-center justify-between mb-4">
              <h2 class="text-lg font-semibold">Habitaciones</h2>
              <router-link :to="`/admin/hoteles/${state.data?.hotel?.id}/habitaciones/crear`">
                <Button icon="pi pi-plus" label="Nueva habitación" rounded />
              </router-link>
            </div>

            <DataTable
              :value="roomsState.data as any[]"
              tableStyle="min-width: 56rem"
              stripedRows
              rowHover
              :loading="roomsAsyncStatus == 'loading'"
              :paginator="true"
              :rows="10"
              :rowsPerPageOptions="[10,20,50]"
            >
              <Column header="Numero">
                <template #body="slotProps">
                  {{ slotProps.data.number }}
                </template>
              </Column>
              <Column header="Capacidad">
                <template #body="slotProps">
                  {{ slotProps.data.capacity ?? '—' }}
                </template>
              </Column>
              <Column header="Precio / noche">
                <template #body="slotProps">
                  {{ formatGTQ(slotProps.data.pricePerNight) }}
                </template>
              </Column>
              <Column header="Estado">
                <template #body="slotProps">
                  <Tag :value="statusLabel(slotProps.data.status)" :severity="statusSeverity(slotProps.data.status)" />
                </template>
              </Column>
              <Column header="Acciones">
                <template #body="slotProps">
                  <div class="flex flex-wrap items-center gap-1">
                    <router-link :to="`/admin/hoteles/${state.data?.hotel?.id}/habitaciones/${slotProps.data.id}`">
                      <Button label="Ver" severity="info" variant="text" rounded />
                    </router-link>
                    <router-link :to="`/admin/hoteles/${state.data?.hotel?.id}/habitaciones/editar-${slotProps.data.id}`">
                      <Button label="Editar" severity="warn" variant="text" rounded />
                    </router-link>
                  </div>
                </template>
              </Column>

              <template #empty>
                <div class="py-10 text-center text-slate-600">
                  <i class="pi pi-inbox text-3xl mb-2 text-slate-400" aria-hidden="true"></i>
                  <div>Este hotel no tiene habitaciones registradas.</div>
                </div>
              </template>

              <template #loading>
                <div class="py-10 text-center text-slate-600">
                  Cargando habitaciones…
                </div>
              </template>

              <template #footer>
                Hay en total {{ roomsState.data ? (roomsState.data as any[]).length : 0 }} habitaciones.
              </template>
            </DataTable>
          </div>
        </section>

        <!-- Card: Restaurantes del hotel -->
        <section class="lg:col-span-2 rounded-xl border border-slate-200 bg-white shadow">
          <div class="p-6">
            <div class="flex items-center justify-between mb-4">
              <h2 class="text-lg font-semibold">Restaurantes</h2>
              <router-link :to="`/admin/restaurantes/crear?hotelId=${state.data?.hotel?.id}`">
                <Button icon="pi pi-plus" label="Nuevo restaurante" rounded />
              </router-link>
            </div>

            <DataTable
              :value="restaurantsState.data as any[]"
              tableStyle="min-width: 56rem"
              stripedRows
              rowHover
              :loading="restaurantsAsyncStatus == 'loading'"
              :paginator="true"
              :rows="10"
              :rowsPerPageOptions="[10,20,50]"
            >
              <Column header="Nombre">
                <template #body="slotProps">
                  {{ slotProps.data.name }}
                </template>
              </Column>
              <Column header="Dirección">
                <template #body="slotProps">
                  <span class="truncate max-w-[18rem] inline-block align-middle" :title="slotProps.data.address">{{ dash(slotProps.data.address) }}</span>
                </template>
              </Column>
              <Column header="Acciones">
                <template #body="slotProps">
                  <div class="flex flex-wrap items-center gap-1">
                    <router-link :to="`/admin/restaurantes/${slotProps.data.id}`">
                      <Button label="Ver" severity="info" variant="text" rounded />
                    </router-link>
                    <router-link :to="`/admin/restaurantes/editar-${slotProps.data.id}`">
                      <Button label="Editar" severity="warn" variant="text" rounded />
                    </router-link>
                  </div>
                </template>
              </Column>

              <template #empty>
                <div class="py-10 text-center text-slate-600">
                  <i class="pi pi-inbox text-3xl mb-2 text-slate-400" aria-hidden="true"></i>
                  <div>Este hotel no tiene restaurantes registrados.</div>
                </div>
              </template>

              <template #loading>
                <div class="py-10 text-center text-slate-600">
                  Cargando restaurantes…
                </div>
              </template>

              <template #footer>
                Hay en total {{ restaurantsState.data ? (restaurantsState.data as any[]).length : 0 }} restaurantes.
              </template>
            </DataTable>
          </div>
        </section>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useQueryCache } from '@pinia/colada'
import { getHotelById, getHotelRooms } from '~/lib/api/establishments/hotels'
import { getRestaurantsByHotelId } from '~/lib/api/establishments/restaurants'

const queryCache = useQueryCache()

// Hotel detail
const { state } = useCustomQuery({
  key: ['hotel', useRoute().params.id as string],
  query: () => getHotelById(useRoute().params.id as string).then((res) => ({ hotel: res })),
})

// Rooms for this hotel
const { state: roomsState, asyncStatus: roomsAsyncStatus } = useCustomQuery({
  key: ['hotelRooms', useRoute().params.id as string],
  query: () => getHotelRooms(useRoute().params.id as string),
})

// Restaurants for this hotel
const { state: restaurantsState, asyncStatus: restaurantsAsyncStatus } = useCustomQuery({
  key: ['hotelRestaurants', useRoute().params.id as string],
  query: () => getRestaurantsByHotelId(useRoute().params.id as string),
})

const hotelName = computed(() => state.value.data?.hotel?.name || '')
const isDisabled = computed(() => !!(state.value.data?.hotel as any)?.desactivatedAt)

const maintenanceFormatted = computed(() => formatGTQ((state.value.data?.hotel as any)?.maintenanceCostPerWeek))

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

function statusLabel(s: string | undefined) {
  if (!s) return '—'
  const m: Record<string, string> = {
    AVAILABLE: 'Disponible',
    OCCUPIED: 'Ocupada',
    MAINTENANCE: 'Mantenimiento',
  }
  return m[s] || s
}

function statusSeverity(s: string | undefined) {
  if (!s) return 'secondary'
  const m: Record<string, any> = {
    AVAILABLE: 'success',
    OCCUPIED: 'warn',
    MAINTENANCE: 'danger',
  }
  return m[s] || 'secondary'
}

function dash(v?: string) { return v || '—' }
</script>
