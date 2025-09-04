<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Page Header -->
    <header class="max-w-7xl mx-auto mb-6" role="banner">
    <NuxtLink to="/" class="inline-block mb-2 sm:mb-0">
        <Button size="small" icon="pi pi-arrow-left" label="Volver"
            class="text-slate-700 hover:text-slate-900 mb-4" />
    </NuxtLink>
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Pagos</h1>
        <router-link to="/facturacion/crear">
          <Button icon="pi pi-plus" label="Nuevo pago" rounded raised />
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

          <Column header="Cliente">
            <template #body="{ data }">
              <span class="font-mono text-xs">{{ data.clientId || '—' }}</span>
            </template>
          </Column>

          <Column header="Establecimiento">
            <template #body="{ data }">
              <p class="font-mono text-xs font-semibold">{{ data.hotel ? `Hotel`: `Restaurante` }}</p>
              <p class="font-mono text-xs">{{ data.hotel ? data.hotel.name : data.restaurant.name }}</p>
            </template>
          </Column>

          <Column header="Origen">
            <template #body="{ data }">
              <span class="text-xs">{{ sourceLabel(data.sourceType) }}</span>
              <span class="ml-1 font-mono text-xs">{{ data.sourceId }}</span>
            </template>
          </Column>

          <Column header="Subtotal">
            <template #body="{ data }">{{ formatGTQ(data.subtotal) }}</template>
          </Column>

          <Column header="Descuento">
            <template #body="{ data }">{{ formatGTQ(data.discount) }}</template>
          </Column>

          <Column header="Promo">
            <template #body="{ data }">
              <template v-if="data.promotionApplied">
                <Tag :value="promoLabel(data.promotionApplied)" severity="info" class="text-xs px-2 py-0.5 truncate max-w-[8rem]" />
              </template>
              <span v-else>—</span>
            </template>
          </Column>

          <Column header="Total">
            <template #body="{ data }">{{ formatGTQ(data.total) }}</template>
          </Column>

          <Column header="Acciones">
            <template #body="{ data }">
              <RouterLink :to="`/facturacion/${data.id}`">
                <Button icon="pi pi-search" text rounded label="Ver" />
              </RouterLink>
            </template>
          </Column>

          <template #empty>
            <div class="py-10 text-center text-slate-600">
              <i class="pi pi-inbox text-3xl mb-2 text-slate-400" aria-hidden="true"></i>
              <div>No hay pagos registrados.</div>
            </div>
          </template>

          <template #loading>
            <div class="py-10 text-center text-slate-600">
              Cargando pagos…
            </div>
          </template>

          <template #footer>
            Hay en total {{ state.data ? (state.data as any[]).length : 0 }} pagos.
          </template>
        </DataTable>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import Tag from 'primevue/tag'
import { RouterLink } from 'vue-router'
import { getAllPayments } from '~/lib/api/payments/payments'

const { state, asyncStatus } = useCustomQuery({
  key: ['paymentsIndex'],
  query: () => getAllPayments(),
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

function methodLabel(m?: string) {
  if (!m) return '—'
  const map: Record<string, string> = {
    CASH: 'Efectivo',
    CARD: 'Tarjeta',
    TRANSFER: 'Transferencia',
    CHECK: 'Cheque',
  }
  return map[m] || m
}

function methodSeverity(m?: string) {
  const map: Record<string, any> = {
    CASH: 'success',
    CARD: 'info',
    TRANSFER: 'help',
    CHECK: 'warn',
  }
  return map[m || ''] || 'secondary'
}

function statusLabel(s?: string) {
  if (!s) return '—'
  const map: Record<string, string> = {
    PAID: 'Pagado',
    PENDING: 'Pendiente',
    FAILED: 'Fallido',
    REFUNDED: 'Reembolsado',
  }
  return map[s] || s
}

function statusSeverity(s?: string) {
  const map: Record<string, any> = {
    PAID: 'success',
    PENDING: 'warn',
    FAILED: 'danger',
    REFUNDED: 'help',
  }
  return map[s || ''] || 'secondary'
}

function sourceLabel(t?: string) {
  if (!t) return '—'
  const map: Record<string, string> = {
    ORDER: 'Orden',
    RESERVATION: 'Reservación',
  }
  return map[t] || t
}
</script>
