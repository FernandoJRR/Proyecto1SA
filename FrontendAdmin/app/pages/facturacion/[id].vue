<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Page Header -->
    <header class="max-w-5xl mx-auto mb-6" role="banner">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link to="/facturacion">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver al listado" />
          </router-link>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Detalle de Pago</h1>
        </div>
      </div>
    </header>

    <main class="max-w-5xl mx-auto" role="main">
      <div v-if="state.status === 'pending'" class="py-16 text-center text-slate-600">Cargando pago…</div>
      <div v-else-if="state.status === 'error'" class="py-16 text-center text-red-600">Ocurrió un error al cargar el pago.</div>

      <div v-else class="rounded-2xl border border-slate-200 bg-white shadow p-6 sm:p-8">
        <!-- Summary grid -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <p class="font-medium text-slate-600">ID del pago</p>
            <p class="text-sm font-mono">{{ payment.id }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Cliente</p>
            <p class="text-sm font-mono">{{ payment.clientId }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Establecimiento</p>
            <p class="text-sm font-mono">{{ payment.establishmentId }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Origen</p>
            <p class="text-sm"><span class="text-xs">{{ sourceLabel(payment.sourceType) }}</span> · <span class="font-mono">{{ payment.sourceId }}</span></p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Subtotal</p>
            <p class="text-base">{{ formatGTQ(payment.subtotal) }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Descuento</p>
            <p class="text-base">{{ formatGTQ(payment.discount) }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Promoción aplicada</p>
            <div>
              <Tag v-if="payment.promotionApplied" :value="promoLabel(payment.promotionApplied)" severity="info" class="text-xs px-2 py-0.5 truncate max-w-[12rem]" />
              <span v-else class="text-slate-500">—</span>
            </div>
          </div>

          <div>
            <p class="font-medium text-slate-600">Total</p>
            <p class="text-lg font-semibold">{{ formatGTQ(payment.total) }}</p>
          </div>

          <!--
          <div>
            <p class="font-medium text-slate-600">Método</p>
            <Tag :value="methodLabel(payment?.method)" :severity="methodSeverity(payment?.method)" class="text-xs px-2 py-0.5" />
          </div>
          -->

          <div>
            <p class="font-medium text-slate-600">Estado</p>
            <Tag :value="statusLabel(payment?.status)" :severity="statusSeverity(payment?.status)" class="text-xs px-2 py-0.5" />
          </div>

          <!--
          <div class="md:col-span-2">
            <p class="font-medium text-slate-600">Tarjeta</p>
            <p class="text-sm font-mono">{{ payment?.cardNumber ? `•••• ${payment.cardNumber}` : '—' }}</p>
          </div>
          -->
        </div>
      </div>

      <!-- Establecimiento (hotel o restaurante, mutuamente excluyentes) -->
      <div v-if="payment && (payment.hotel || payment.restaurant)" class="mt-6 rounded-2xl border border-slate-200 bg-white shadow p-6 sm:p-8">
        <div class="flex items-center justify-between mb-4">
          <h2 class="text-lg font-semibold">
            {{ payment?.hotel ? 'Hotel' : 'Restaurante' }}
          </h2>
          <div class="flex items-center gap-2">
            <router-link v-if="payment?.hotel?.id" :to="`/admin/hoteles/${payment.hotel.id}`">
              <Button icon="pi pi-external-link" label="Ver hotel" rounded />
            </router-link>
            <router-link v-if="payment?.restaurant?.id" :to="`/admin/restaurantes/${payment.restaurant.id}`">
              <Button icon="pi pi-external-link" label="Ver restaurante" rounded />
            </router-link>
          </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div v-if="payment?.hotel">
            <p class="font-medium text-slate-600">Hotel</p>
            <div class="space-y-1">
              <p class="text-base">{{ payment?.hotel?.name || '—' }}</p>
              <p class="text-sm text-slate-600">{{ payment?.hotel?.address || '—' }}</p>
            </div>
          </div>

          <div v-if="payment?.restaurant">
            <p class="font-medium text-slate-600">Restaurante</p>
            <div class="space-y-1">
              <p class="text-base">{{ payment?.restaurant?.name || '—' }}</p>
              <p class="text-sm text-slate-600">{{ payment?.restaurant?.address || '—' }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Origen del pago (orden o reservación) -->
      <div v-if="payment && payment.sourceType && payment.sourceId" class="mt-6 rounded-2xl border border-slate-200 bg-white shadow p-6 sm:p-8">
        <div class="flex items-center justify-between mb-4">
          <h2 class="text-lg font-semibold">Origen</h2>
          <div class="flex items-center gap-2">
            <router-link v-if="payment.sourceType === 'ORDER'" :to="`/ordenes/${payment.sourceId}`">
              <Button icon="pi pi-external-link" label="Ver orden" rounded />
            </router-link>
            <router-link v-else-if="payment.sourceType === 'RESERVATION'" :to="`/reservaciones/ver/${payment.sourceId}`">
              <Button icon="pi pi-external-link" label="Ver reservación" rounded />
            </router-link>
          </div>
        </div>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <p class="font-medium text-slate-600">Tipo</p>
            <p class="text-sm">{{ sourceLabel(payment.sourceType) }}</p>
          </div>
          <div>
            <p class="font-medium text-slate-600">ID origen</p>
            <p class="text-sm font-mono">{{ payment.sourceId }}</p>
          </div>
        </div>
      </div>

      <!-- Cliente -->
      <div v-if="payment && payment.clientId" class="mt-6 rounded-2xl border border-slate-200 bg-white shadow p-6 sm:p-8">
        <div class="flex items-center justify-between mb-4">
          <h2 class="text-lg font-semibold">Cliente</h2>
          <div class="flex items-center gap-2">
            <router-link :to="`/clientes/${payment.clientId}`">
              <Button icon="pi pi-external-link" label="Ver cliente" rounded />
            </router-link>
          </div>
        </div>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <p class="font-medium text-slate-600">ID cliente</p>
            <p class="text-sm font-mono">{{ payment.clientId }}</p>
          </div>
          <div>
            <p class="font-medium text-slate-600">Establecimiento</p>
            <p class="text-sm font-mono">{{ payment.establishmentId }}</p>
          </div>
        </div>
      </div>

      <!-- Metadatos -->
      <div v-if="payment && (payment.createdAt || payment.updatedAt)" class="mt-6 rounded-2xl border border-slate-200 bg-white shadow p-6 sm:p-8">
        <h2 class="text-lg font-semibold mb-4">Metadatos</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div v-if="payment.createdAt">
            <p class="font-medium text-slate-600">Creado</p>
            <p class="text-sm">{{ formatDate(payment.createdAt) }}</p>
          </div>
          <div v-if="payment.updatedAt">
            <p class="font-medium text-slate-600">Actualizado</p>
            <p class="text-sm">{{ formatDate(payment.updatedAt) }}</p>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import Tag from 'primevue/tag'
import { useRoute } from 'vue-router'
import { getPaymentById } from '~/lib/api/payments/payments'

const route = useRoute()

const { state } = useCustomQuery({
  key: ['payment', () => route.params.id as string],
  query: () => getPaymentById(route.params.id as string),
})

const payment = computed(() => state.value.data as any)

function copyId(id?: string) {
  if (!id) return
  try { navigator.clipboard?.writeText(String(id)) } catch {}
}

function formatDate(v: string | Date | null | undefined) {
  if (!v) return '—'
  const d = typeof v === 'string' ? new Date(v) : v
  try {
    return new Intl.DateTimeFormat('es-GT', { dateStyle: 'medium', timeStyle: 'short' }).format(d)
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
