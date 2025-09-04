<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Page Header -->
    <header class="max-w-5xl mx-auto mb-6" role="banner">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link to="/ordenes">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver al listado" />
          </router-link>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Detalle de Orden</h1>
        </div>
      </div>
    </header>

    <main class="max-w-5xl mx-auto" role="main">
      <div v-if="state.status === 'pending'" class="py-16 text-center text-slate-600">Cargando orden…</div>
      <div v-else-if="state.status === 'error'" class="py-16 text-center text-red-600">Ocurrió un error inesperado.</div>

      <div v-else class="rounded-2xl border border-slate-200 bg-white shadow p-6 sm:p-8">
        <!-- Summary grid -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <p class="font-medium text-slate-600">ID de la orden</p>
            <p class="text-sm font-mono">{{ order.id }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">CUI del cliente</p>
            <p class="text-base">{{ order.clientCui }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Restaurante</p>
            <p class="text-sm font-mono">{{ order.restaurant.name || '—' }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Ítems</p>
            <p class="text-base">{{ order.items?.length ?? 0 }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Subtotal</p>
            <p class="text-base">{{ formatGTQ(order.subtotal) }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Promoción aplicada</p>
            <div>
              <Tag v-if="order.promotionApplied" :value="promoLabel(order.promotionApplied)" severity="info" class="text-xs px-2 py-0.5 truncate max-w-[12rem]" />
              <span v-else class="text-slate-500">—</span>
            </div>
          </div>

          <div class="md:col-span-2">
            <p class="font-medium text-slate-600">Total</p>
            <p class="text-lg font-semibold">{{ formatGTQ(order.total) }}</p>
          </div>
        </div>

        <!-- Items table -->
        <div class="mt-6">
          <h2 class="text-lg font-semibold mb-3">Ítems</h2>
          <div class="overflow-x-auto rounded-lg border border-slate-200">
            <DataTable :value="order.items as any[]" tableStyle="min-width: 48rem" stripedRows rowHover>
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
                <template #body="{ data }">{{ formatGTQ(data.price) }}</template>
              </Column>
              <Column header="Importe">
                <template #body="{ data }">{{ formatGTQ(lineTotal(data)) }}</template>
              </Column>
              <template #empty>
                <div class="py-8 text-center text-slate-600">No hay ítems en esta orden.</div>
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
import { getOrderById } from '~/lib/api/orders/orders'

const route = useRoute()

const { state } = useCustomQuery({
  key: ['order', () => route.params.id as string],
  query: () => getOrderById(route.params.id as string),
})

const order = computed(() => state.value.data as any)

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

function lineTotal(item: any) {
  const q = Number(item?.quantity ?? 0)
  const p = Number(item?.price ?? 0)
  return q * p
}

function promoLabel(p: any) {
  if (!p) return ''
  const parts: string[] = []
  if (p.name) parts.push(p.name)
  if (p.percentOff) parts.push(`${p.percentOff}% off`)
  if (p.amountOff) parts.push(`- ${formatGTQ(p.amountOff)}`)
  return parts.join(' · ')
}
</script>
