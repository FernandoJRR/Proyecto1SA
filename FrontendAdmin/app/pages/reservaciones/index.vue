<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Page Header -->
    <header class="max-w-7xl mx-auto mb-6" role="banner">
        <NuxtLink to="/" class="inline-block mb-2 sm:mb-0">
            <Button size="small" icon="pi pi-arrow-left" label="Volver"
                class="text-slate-700 hover:text-slate-900 mb-4" />
        </NuxtLink>
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Reservaciones</h1>
        <router-link to="/reservaciones/crear">
          <Button icon="pi pi-plus" label="Nueva reservación" rounded raised />
        </router-link>
      </div>
    </header>

    <!-- Table -->
    <main class="max-w-7xl mx-auto" role="main">
      <div class="overflow-x-auto rounded-xl border border-slate-200 bg-white shadow">
        <DataTable
          :value="state.data as any[]"
          tableStyle="min-width: 64rem"
          stripedRows
          rowHover
          :loading="asyncStatus === 'loading'"
          :paginator="true"
          :rows="10"
          :rowsPerPageOptions="[10,20,50]"
        >
          <template #header>
            <div class="flex flex-wrap items-center justify-between gap-2">
              <span class="text-sm text-slate-600">Total: <span class="font-semibold">{{ state.data ? (state.data as any[]).length : 0 }}</span></span>
            </div>
          </template>

          <Column header="ID">
            <template #body="{ data }">
              <span class="font-mono text-xs">{{ data.id }}</span>
            </template>
          </Column>

          <Column header="CUI cliente">
            <template #body="{ data }">{{ data.clientCui || '—' }}</template>
          </Column>

          <Column header="Hotel">
            <template #body="{ data }">
              <span class="font-mono text-xs">{{ data.hotel.name || '—' }}</span>
            </template>
          </Column>

          <Column header="Habitación">
            <template #body="{ data }">
              <span class="font-mono text-xs">{{ data.room.number || '—' }}</span>
            </template>
          </Column>

          <Column header="Inicio">
            <template #body="{ data }">{{ formatDate(data.startDate) }}</template>
          </Column>

          <Column header="Fin">
            <template #body="{ data }">{{ formatDate(data.endDate) }}</template>
          </Column>

          <Column header="Subtotal">
            <template #body="{ data }">{{ formatGTQ(data.subtotal) }}</template>
          </Column>

          <Column header="Promo">
            <template #body="{ data }">
              <template v-if="data.promotionApplied">
                <Tag severity="info" class="text-xs px-2 py-0.5 truncate max-w-[8rem] text-center">
                {{data.promotionApplied.percentOff}}%<br/>
                - Q.{{data.promotionApplied.amountOff}}
                </Tag>
              </template>
              <span v-else><Tag :value="'No'" severity="contrast"/></span>
            </template>
          </Column>

          <Column header="Total">
            <template #body="{ data }">{{ formatGTQ(data.totalCost) }}</template>
          </Column>

          <Column header="Acciones">
            <template #body="{ data }">
              <router-link :to="`/reservaciones/${data.id}`">
                <Button icon="pi pi-search" text rounded label="Ver" />
              </router-link>
            </template>
          </Column>

          <template #empty>
            <div class="py-10 text-center text-slate-600">
              <i class="pi pi-inbox text-3xl mb-2 text-slate-400" aria-hidden="true"></i>
              <div>No hay reservaciones registradas.</div>
            </div>
          </template>

          <template #loading>
            <div class="py-10 text-center text-slate-600">
              Cargando reservaciones…
            </div>
          </template>

          <template #footer>
            Hay en total {{ state.data ? (state.data as any[]).length : 0 }} reservaciones.
          </template>
        </DataTable>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { Tag } from 'primevue'
import { getAllReservations } from '~/lib/api/reservations/reservations'

// Data
const { state, asyncStatus } = useCustomQuery({
  key: ['reservationsIndex'],
  query: () => getAllReservations(),
})

// Helpers
function formatDate(v: string | Date | null | undefined) {
  if (!v) return '—'
  try {
    const d = typeof v === 'string' ? new Date(v) : v
    return new Intl.DateTimeFormat('es-GT', { dateStyle: 'medium' }).format(d)
  } catch {
    return String(v)
  }
}

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
</script>
