<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Page Header -->
    <header class="max-w-7xl mx-auto mb-6" role="banner">
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div class="flex items-center gap-3">
          <RouterLink to="/admin">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver a Administración" />
          </RouterLink>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Restaurantes</h1>
        </div>
        <RouterLink to="/admin/restaurantes/crear">
          <Button icon="pi pi-plus" label="Nuevo Restaurante" rounded raised />
        </RouterLink>
      </div>
    </header>

    <!-- Table -->
    <main class="max-w-7xl mx-auto" role="main">
      <div class="overflow-x-auto rounded-xl border border-slate-200 bg-white shadow">
        <DataTable
          :value="state.data as any[]"
          tableStyle="min-width: 56rem"
          stripedRows
          rowHover
          :loading="asyncStatus == 'loading'"
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
            <template #body="slotProps">
              {{ slotProps.data.name }}
            </template>
          </Column>

          <Column header="En Hotel">
            <template #body="slotProps">
              <Tag>{{ slotProps.data.hotelId ? 'Si' : 'No' }}</Tag>
            </template>
          </Column>

          <Column header="Dirección">
            <template #body="slotProps">
              <span class="truncate max-w-[18rem] inline-block align-middle" :title="slotProps.data.address">{{ slotProps.data.address || '—' }}</span>
              <Tag v-if="slotProps.data.desactivatedAt" class="ml-2" value="Desactivado" severity="danger" />
            </template>
          </Column>

          <Column header="Acciones">
            <template #body="slotProps">
              <div class="flex flex-wrap items-center gap-1">
                <RouterLink :to="`/admin/restaurantes/${slotProps.data.id}`">
                  <Button label="Ver" severity="info" variant="text" rounded aria-label="Ver restaurante" />
                </RouterLink>
                <RouterLink :to="`/admin/restaurantes/editar-${slotProps.data.id}`">
                  <Button label="Editar" severity="warn" variant="text" rounded aria-label="Editar restaurante" />
                </RouterLink>
                <RouterLink v-if="!slotProps.data.desactivatedAt" :to="`/admin/restaurantes/deshabilitar-${slotProps.data.id}`">
                  <Button label="Deshabilitar" severity="danger" variant="text" rounded aria-label="Deshabilitar restaurante" />
                </RouterLink>
                <RouterLink v-else :to="`/admin/restaurantes/reactivar-${slotProps.data.id}`">
                  <Button label="Reactivar" severity="help" variant="text" rounded aria-label="Reactivar restaurante" />
                </RouterLink>
              </div>
            </template>
          </Column>

          <template #empty>
            <div class="py-10 text-center text-slate-600">
              <i class="pi pi-inbox text-3xl mb-2 text-slate-400" aria-hidden="true"></i>
              <div>No hay restaurantes registrados.</div>
            </div>
          </template>

          <template #loading>
            <div class="py-10 text-center text-slate-600">
              Cargando restaurantes…
            </div>
          </template>

          <template #footer>
            Hay en total {{ state.data ? (state.data as any[]).length : 0 }} restaurantes.
          </template>
        </DataTable>
      </div>
    </main>
  </div>
</template>
<script setup lang="ts">
import { Tag } from 'primevue';
import { RouterLink } from 'vue-router'
import { getAllRestaurants } from '~/lib/api/establishments/restaurants'

const { state, asyncStatus } = useCustomQuery({
  key: ['restaurantes'],
  query: () => getAllRestaurants(),
})
</script>
