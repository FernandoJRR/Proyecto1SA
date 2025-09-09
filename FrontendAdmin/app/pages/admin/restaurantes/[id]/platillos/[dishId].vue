<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Page Header -->
    <header class="max-w-5xl mx-auto mb-6" role="banner">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link :to="backPath">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver al listado" />
          </router-link>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Detalle de Platillo</h1>
        </div>
      </div>
    </header>

    <main class="max-w-5xl mx-auto" role="main">
      <div v-if="state.status === 'pending'" class="py-16 text-center text-slate-600">Cargando orden…</div>
      <div v-else-if="state.status === 'error'" class="py-16 text-center text-red-600">Ocurrió un error inesperado.</div>

      <div v-else class="rounded-2xl border border-slate-200 bg-white shadow p-6 sm:p-8">
        <!-- Summary grid: Dish info -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <p class="font-medium text-slate-600">ID del platillo</p>
            <p class="text-sm font-mono">{{ dish?.id || dishId }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Restaurante</p>
            <p class="text-sm font-mono">{{ restaurantId }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Nombre</p>
            <p class="text-base">{{ dish?.name || '—' }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Precio</p>
            <p class="text-base">{{ dish?.price != null ? formatGTQ(dish.price) : '—' }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Categoría</p>
            <p class="text-base">{{ dish?.category || dish?.type || '—' }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Estado</p>
            <div>
              <Tag v-if="dish?.desactivatedAt" value="Desactivado" severity="danger" class="text-xs px-2 py-0.5" />
              <Tag v-else value="Activo" severity="success" class="text-xs px-2 py-0.5" />
            </div>
          </div>
        </div>

        <!-- Orders table (that include this dish) -->
        <!--
        <div class="mt-8">
          <h2 class="text-lg font-semibold mb-3">Órdenes con este platillo</h2>
          <div class="overflow-x-auto rounded-lg border border-slate-200">
            <DataTable :value="orders as any[]" tableStyle="min-width: 56rem" stripedRows rowHover :loading="ordersLoading">
              <Column header="#">
                <template #body="{ index }">{{ index + 1 }}</template>
              </Column>
              <Column header="ID">
                <template #body="{ data }"><span class="font-mono">{{ data.id }}</span></template>
              </Column>
              <Column header="Cliente">
                <template #body="{ data }">{{ data.clientCui || data.clientId || '—' }}</template>
              </Column>
              <Column header="Fecha">
                <template #body="{ data }">{{ fmtDate(data.orderedAt || data.createdAt) }}</template>
              </Column>
              <Column header="Cantidad (este platillo)">
                <template #body="{ data }">{{ (data.items?.find?.((i:any)=>i.dishId===dishId)?.quantity) ?? '—' }}</template>
              </Column>
              <Column header="Total">
                <template #body="{ data }">{{ data.total != null ? formatGTQ(data.total) : '—' }}</template>
              </Column>
              <template #empty>
                <div class="py-8 text-center text-slate-600">No hay órdenes con este platillo.</div>
              </template>
            </DataTable>
          </div>
        </div>
        -->

        <!-- Reviews table -->
        <div class="mt-8">
          <div class="flex items-center justify-between mb-3">
            <h2 class="text-lg font-semibold">Reseñas de este platillo</h2>
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
import { getRestaurantDish } from '~/lib/api/establishments/restaurants'
import { getAllOrders } from '~/lib/api/orders/orders'
import { getReviews } from '~/lib/api/reviews/reviews'

const route = useRoute()

console.log("ROUUUUUUTE")
console.log(route.params)
const restaurantId = computed(() => (route.params as any).id as string)
const dishId = computed(() => (route.params as any).dishId as string)
const backPath = computed(() => `/admin/restaurantes/${restaurantId.value}`)

// Dish details (requires both restaurantId and dishId)
const { state } = useCustomQuery({
  key: ['restaurantDish', () => `${restaurantId.value}:${dishId.value}`],
  query: () => getRestaurantDish(restaurantId.value, dishId.value),
})
const dish = computed(() => state.value.data as any)

// Orders that include this dish (filter by restaurantId + dishId)
const { state: ordersState, asyncStatus: ordersStatus } = useCustomQuery({
  key: ['dishOrders', () => `${restaurantId.value}:${dishId.value}`],
  query: () => getAllOrders({ restaurantId: restaurantId.value, dishId: dishId.value }),
})
const ordersLoading = computed(() => ordersStatus.value === 'loading')
const orders = computed(() => (ordersState.value.data as any[]) ?? [])

// Reviews for this dish (sourceId = dishId)
const { state: reviewsState, asyncStatus: reviewsStatus } = useCustomQuery({
  key: ['dishReviews', () => dishId.value],
  query: () => getReviews({ sourceId: dishId.value }),
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
