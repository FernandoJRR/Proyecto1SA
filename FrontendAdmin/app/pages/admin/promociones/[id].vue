<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Page Header -->
    <header class="max-w-5xl mx-auto mb-6" role="banner">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link to="/admin/promociones">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver al listado" />
          </router-link>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Detalle de Promoción</h1>
        </div>
        <div class="flex items-center gap-2">
          <router-link :to="`/admin/promociones/editar-${promotion?.id}`">
            <Button icon="pi pi-pencil" label="Editar" rounded />
          </router-link>
        </div>
      </div>
    </header>

    <main class="max-w-5xl mx-auto" role="main">
      <div v-if="state.status === 'pending'" class="py-16 text-center text-slate-600">Cargando promoción…</div>
      <div v-else-if="state.status === 'error'" class="py-16 text-center text-red-600">Ocurrió un error al cargar la promoción.</div>

      <div v-else class="rounded-2xl border border-slate-200 bg-white shadow p-6 sm:p-8">
        <!-- Summary grid -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <p class="font-medium text-slate-600">ID</p>
            <p class="text-sm font-mono break-all">{{ promotion.id }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Nombre</p>
            <p class="text-base">{{ promotion.name }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Tipo</p>
            <div class="flex items-center gap-2">
              <Tag :value="promotion.promotionType?.name || promotion.promotionType?.code || promotionTypeLabel(promotionTypeCode)" severity="info" class="text-xs px-2 py-0.5" />
              <span class="text-xs text-slate-500 font-mono">{{ promotionTypeCode }}</span>
            </div>
          </div>

          <div>
            <p class="font-medium text-slate-600">Estado</p>
            <Tag :value="statusLabel(promotion)" :severity="statusSeverity(promotion)" class="text-xs px-2 py-0.5" />
          </div>

          <div>
            <p class="font-medium text-slate-600">Porcentaje</p>
            <p class="text-base font-semibold">{{ (Number(promotion.percentage) || 0) }}%</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Vigencia</p>
            <p class="text-sm">{{ formatDate(promotion.startDate) }} → {{ formatDate(promotion.endDate) }}</p>
          </div>

          <div>
            <p class="font-medium text-slate-600">Tipo de establecimiento</p>
            <Tag :value="estTypeLabel(promotion.establishmentType)" :severity="estTypeSeverity(promotion.establishmentType)" class="text-xs px-2 py-0.5" />
          </div>

          <div>
            <p class="font-medium text-slate-600">Establecimiento</p>
            <div class="flex items-center gap-2">
              <span class="text-sm font-mono">{{ promotion.establishmentId }}</span>
              <router-link v-if="promotion.establishmentId" :to="establishmentRoute">
                <Button icon="pi pi-external-link" label="Ver" rounded text />
              </router-link>
            </div>
          </div>

          <div v-if="promotion.topCount !== undefined">
            <p class="font-medium text-slate-600">Top Count</p>
            <p class="text-base">{{ promotion.topCount }}</p>
          </div>
        </div>
      </div>

      <!-- Metadatos
      <div v-if="promotion && (promotion.createdAt || promotion.updatedAt)" class="mt-6 rounded-2xl border border-slate-200 bg-white shadow p-6 sm:p-8">
        <h2 class="text-lg font-semibold mb-4">Metadatos</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div v-if="promotion.createdAt">
            <p class="font-medium text-slate-600">Creado</p>
            <p class="text-sm">{{ formatDateTime(promotion.createdAt) }}</p>
          </div>
          <div v-if="promotion.updatedAt">
            <p class="font-medium text-slate-600">Actualizado</p>
            <p class="text-sm">{{ formatDateTime(promotion.updatedAt) }}</p>
          </div>
        </div>
      </div>
       -->
    </main>
  </div>
</template>

<script setup lang="ts">
import Tag from 'primevue/tag'
import { useRoute } from 'vue-router'
import { getPromotionById, type Promotion } from '~/lib/api/promotions/promotions'

const route = useRoute()

const { state } = useCustomQuery({
  key: ['promotion', () => route.params.id as string],
  query: () => getPromotionById(route.params.id as string),
})

const promotion = computed(() => state.value.data as Promotion)
const promotionTypeCode = computed(() => (promotion.value?.promotionType as any)?.code || (promotion.value as any)?.promotionType)

const establishmentRoute = computed(() => {
  if (!promotion.value?.establishmentId) return '#'
  if (promotion.value.establishmentType === 'HOTEL') return `/admin/hoteles/${promotion.value.establishmentId}`
  if (promotion.value.establishmentType === 'RESTAURANT') return `/admin/restaurantes/${promotion.value.establishmentId}`
  return '#'
})

function formatDate(v?: string | Date | null) {
  if (!v) return '—'
  const d = typeof v === 'string' ? new Date(v) : v
  try { return new Intl.DateTimeFormat('es-GT', { dateStyle: 'medium' }).format(d) } catch { return String(v) }
}

function formatDateTime(v?: string | Date | null) {
  if (!v) return '—'
  const d = typeof v === 'string' ? new Date(v) : v
  try { return new Intl.DateTimeFormat('es-GT', { dateStyle: 'medium', timeStyle: 'short' }).format(d) } catch { return String(v) }
}

function statusLabel(p?: Promotion) {
  if (!p) return '—'
  const now = new Date()
  const s = new Date(p.startDate)
  const e = new Date(p.endDate)
  if (now < s) return 'Próxima'
  if (now > e) return 'Finalizada'
  return 'Activa'
}

function statusSeverity(p?: Promotion) {
  if (!p) return 'secondary'
  const now = new Date()
  const s = new Date(p.startDate)
  const e = new Date(p.endDate)
  if (now < s) return 'help'
  if (now > e) return 'secondary'
  return 'success'
}

function estTypeLabel(t?: string) {
  const map: Record<string, string> = { HOTEL: 'Hotel', RESTAURANT: 'Restaurante' }
  return map[t || ''] || t || '—'
}

function estTypeSeverity(t?: string) {
  const map: Record<string, string> = { HOTEL: 'warn', RESTAURANT: 'info' }
  return map[t || ''] || 'secondary'
}

function promotionTypeLabel(code?: string) {
  const map: Record<string, string> = {
    ROOM_MOST_POPULAR: 'Habitación más popular',
    DISH_MOST_POPULAR: 'Platillo más popular',
    CLIENT_MOST_FRECUENT: 'Cliente más frecuente',
  }
  return map[code || ''] || code || '—'
}
</script>
