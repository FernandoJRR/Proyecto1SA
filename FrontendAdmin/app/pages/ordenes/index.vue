<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Page Header -->
    <header class="max-w-7xl mx-auto mb-6" role="banner">
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div class="flex items-center gap-3">
          <RouterLink to="/">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver a Administración" />
          </RouterLink>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Órdenes</h1>
        </div>
        <RouterLink to="/ordenes/crear">
          <Button icon="pi pi-plus" label="Nueva orden" rounded raised />
        </RouterLink>
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

          <Column header="CUI">
            <template #body="{ data }">{{ data.clientCui || '—' }}</template>
          </Column>

          <Column header="Restaurante">
            <template #body="{ data }">
              <span class="font-mono text-xs">{{ data.restaurant.name || '—' }}</span>
            </template>
          </Column>

          <Column header="Ítems">
            <template #body="{ data }">
              <span class="text-sm text-slate-700">{{ itemsSummary(data.items) }}</span>
            </template>
          </Column>

          <Column header="Subtotal">
            <template #body="{ data }">{{ formatGTQ(data.subtotal) }}</template>
          </Column>

          <Column header="Promo">
            <template #body="{ data }">
              <template v-if="data.promotionApplied">
                <Tag severity="info" class="text-xs text-center px-2 py-0.5 truncate max-w-[10rem]">
                - Q.{{ data.promotionApplied.amountOff }}<br/>
                {{ data.promotionApplied.percentOff }}%
                </Tag>
              </template>
              <template v-else><Tag :value="'No'" severity="contrast"/></template>
            </template>
          </Column>

          <Column header="Total">
            <template #body="{ data }">{{ formatGTQ(data.total) }}</template>
          </Column>

          <Column header="Acciones">
            <template #body="{ data }">
              <div class="flex flex-wrap items-center gap-1">
                <RouterLink :to="`/ordenes/${data.id}`">
                  <Button label="Ver" severity="info" variant="text" rounded aria-label="Ver orden" />
                </RouterLink>
              </div>
            </template>
          </Column>

          <template #empty>
            <div class="py-10 text-center text-slate-600">
              <i class="pi pi-inbox text-3xl mb-2 text-slate-400" aria-hidden="true"></i>
              <div>No hay órdenes registradas.</div>
            </div>
          </template>

          <template #loading>
            <div class="py-10 text-center text-slate-600">
              Cargando órdenes…
            </div>
          </template>

          <template #footer>
            Hay en total {{ state.data ? (state.data as any[]).length : 0 }} órdenes.
          </template>
        </DataTable>
      </div>
    </main>
  </div>
</template>
<script setup lang="ts">
import { Tag } from 'primevue'
import { RouterLink } from 'vue-router'
import { getAllOrders } from '~/lib/api/orders/orders'

const { state, asyncStatus } = useCustomQuery({
  key: ['ordenes'],
  query: () => getAllOrders(),
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

function promoLabel(p: any) {
  if (!p) return ''
  const parts: string[] = []
  if (p.name) parts.push(p.name)
  if (p.percentOff) parts.push(`${p.percentOff}% off`)
  if (p.amountOff) parts.push(`- ${formatGTQ(p.amountOff)}`)
  return parts.join(' · ')
}

function itemsSummary(items: any[]) {
  if (!Array.isArray(items) || items.length === 0) return '—'
  const totalQty = items.reduce((acc, it) => acc + Number(it.quantity || 0), 0)
  return `${items.length} ítem(s) · ${totalQty} uds.`
}
</script>
