<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Page Header -->
    <header class="max-w-7xl mx-auto mb-6" role="banner">
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div class="flex items-center gap-3">
          <RouterLink to="/admin">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver a Administración" />
          </RouterLink>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Promociones</h1>
        </div>
        <RouterLink to="/admin/promociones/crear">
          <Button icon="pi pi-plus" label="Nueva promoción" rounded raised />
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

          <Column header="Nombre">
            <template #body="{ data }">
              <div class="flex flex-col items-center gap-2">
                <span class="font-medium">{{ data.name }}</span>
                <Tag :value="data.promotionType?.name || data.promotionType?.code" severity="info" class="text-[11px] px-2 py-0.5" />
              </div>
            </template>
          </Column>

          <Column header="%">
            <template #body="{ data }">
              <span class="font-mono text-sm">{{ (Number(data.percentage) || 0) }}%</span>
            </template>
          </Column>

          <Column header="Vigencia">
            <template #body="{ data }">
              <div class="flex items-center gap-2">
                <span class="text-sm">{{ formatDate(data.startDate) }} → {{ formatDate(data.endDate) }}</span>
                <Tag :value="statusLabel(data)" :severity="statusSeverity(data)" class="text-[11px] px-2 py-0.5" />
              </div>
            </template>
          </Column>

          <Column header="Establecimiento">
            <template #body="{ data }">
              <div class="flex flex-col items-center gap-2">
                <Tag :value="estTypeLabel(data.establishmentType)" :severity="estTypeSeverity(data.establishmentType)" class="text-[11px] px-2 py-0.5" />
                <span class="font-mono text-xs">{{ data.establishmentId }}</span>
              </div>
            </template>
          </Column>

          <Column header="Acciones">
            <template #body="{ data }">
              <div class="flex flex-wrap items-center gap-1">
                <RouterLink :to="`/admin/promociones/${data.id}`">
                  <Button label="Ver" severity="info" variant="text" rounded aria-label="Ver promoción" />
                </RouterLink>
                <RouterLink :to="`/admin/promociones/editar-${data.id}`">
                  <Button label="Editar" severity="warn" variant="text" rounded aria-label="Editar promoción" />
                </RouterLink>
              </div>
            </template>
          </Column>

          <template #empty>
            <div class="py-10 text-center text-slate-600">
              <i class="pi pi-inbox text-3xl mb-2 text-slate-400" aria-hidden="true"></i>
              <div>No hay promociones registradas.</div>
            </div>
          </template>

          <template #loading>
            <div class="py-10 text-center text-slate-600">Cargando promociones…</div>
          </template>

          <template #footer>
            Hay en total {{ state.data ? (state.data as any[]).length : 0 }} promociones.
          </template>
        </DataTable>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import Tag from 'primevue/tag'
import { RouterLink } from 'vue-router'
import { getAllPromotions, type Promotion } from '~/lib/api/promotions/promotions'

const { state, asyncStatus } = useCustomQuery({
  key: ['promotionsIndex'],
  query: () => getAllPromotions(),
})

function formatDate(v?: string) {
  if (!v) return '—'
  try {
    const d = new Date(v)
    return new Intl.DateTimeFormat('es-GT', { dateStyle: 'medium' }).format(d)
  } catch { return String(v) }
}

function statusLabel(p: Promotion) {
  const now = new Date()
  const s = new Date(p.startDate)
  const e = new Date(p.endDate)
  if (now < s) return 'Próxima'
  if (now > e) return 'Finalizada'
  return 'Activa'
}

function statusSeverity(p: Promotion) {
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
</script>
