<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Page Header -->
    <header class="max-w-7xl mx-auto mb-6" role="banner">
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div class="flex items-center gap-3">
          <RouterLink to="/admin">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver a Administración" />
          </RouterLink>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Empleados</h1>
        </div>
        <RouterLink to="/admin/personal/crear">
          <Button icon="pi pi-plus" label="Crear Empleado" rounded raised />
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
            <div class="flex flex-wrap items-center justify-between gap-3">
              <div class="flex items-center gap-2">
                <span class="text-sm text-slate-600">Total: <span class="font-semibold">{{ state.data ? (state.data as any[]).length : 0 }}</span></span>
              </div>
              <div class="flex items-center gap-2">
                <Dropdown
                  v-model="establishmentType"
                  :options="establishmentTypeOptions"
                  optionLabel="label"
                  optionValue="value"
                  placeholder="Tipo de establecimiento"
                  class="w-56"
                />
                <Dropdown
                  v-if="establishmentType"
                  v-model="establishmentId"
                  :options="establishmentOptions"
                  :loading="establishmentsLoading"
                  optionLabel="label"
                  optionValue="value"
                  placeholder="Selecciona establecimiento"
                  class="w-64"
                  showClear
                  filter
                />
                <Button icon="pi pi-search" label="Filtrar" size="small" @click="applyFilter" />
                <Button icon="pi pi-times" label="Limpiar" size="small" severity="secondary" text @click="clearFilter" />
              </div>
            </div>
          </template>

          <Column header="Nombre Completo">
            <template #body="slotProps">
              <template v-if="slotProps.data.firstName !== null">
                {{ `${slotProps.data.firstName} ${slotProps.data.lastName}` }}
              </template>
              <template v-else>
                {{ `Admin` }}
              </template>
            </template>
          </Column>

          <Column header="Salario semanal">
            <template #body="slotProps">
              <Tag>Q. {{ slotProps.data.salary }}</Tag>
            </template>
          </Column>

          <Column header="Área">
            <template #body="slotProps">
              <Tag :value="slotProps.data.employeeType.name" />
              <Tag v-if="slotProps.data.desactivatedAt !== null" class="ml-2" value="Desactivado" severity="danger" />
            </template>
          </Column>

          <Column header="Acciones">
            <template #body="slotProps">
              <div v-if="slotProps.data.id !== employee?.id" class="flex flex-wrap items-center gap-1">
                <RouterLink :to="`/admin/personal/${slotProps.data.id}`">
                  <Button label="Ver" severity="info" variant="text" rounded aria-label="Ver empleado" />
                </RouterLink>
                <RouterLink :to="`/admin/personal/editar-${slotProps.data.id}`">
                  <Button label="Editar" severity="warn" variant="text" rounded aria-label="Editar empleado" />
                </RouterLink>
                <RouterLink v-if="slotProps.data.firstName && !slotProps.data.desactivatedAt" :to="`/admin/personal/deshabilitar-${slotProps.data.id}`">
                  <Button label="Deshabilitar" severity="danger" variant="text" rounded aria-label="Deshabilitar empleado" />
                </RouterLink>
                <RouterLink v-if="slotProps.data.firstName && slotProps.data.desactivatedAt" :to="`/admin/personal/reactivar-${slotProps.data.id}`">
                  <Button label="Reactivar" severity="help" variant="text" rounded aria-label="Reactivar empleado" />
                </RouterLink>
              </div>
              <div v-else class="font-semibold">Eres este usuario</div>
            </template>
          </Column>

          <template #empty>
            <div class="py-10 text-center text-slate-600">
              <i class="pi pi-inbox text-3xl mb-2 text-slate-400" aria-hidden="true"></i>
              <div>No hay empleados registrados.</div>
            </div>
          </template>

          <template #loading>
            <div class="py-10 text-center text-slate-600">
              Cargando empleados…
            </div>
          </template>

          <template #footer>
            Hay en total {{ state.data ? (state.data as any[]).length : 0 }} usuarios.
          </template>
        </DataTable>
      </div>
    </main>
  </div>
</template>
<script setup lang="ts">
import { RouterLink } from 'vue-router';
import { getAllEmployees, type Employee } from '~/lib/api/admin/employee';
import InputText from 'primevue/inputtext'
import Dropdown from 'primevue/dropdown'
import { getAllHotels } from '~/lib/api/establishments/hotels';
import { getAllRestaurants } from '~/lib/api/establishments/restaurants';

const establishmentType = ref<'' | 'HOTEL' | 'RESTAURANT'>('')
const establishmentId = ref<string>('') // seleccionado en dropdown
const establishmentOptions = ref<Array<{ label: string; value: string }>>([])
const establishmentsLoading = ref(false)

const establishmentTypeOptions = [
  { value: '', label: '— Tipo —' },
  { value: 'HOTEL', label: 'Hotel' },
  { value: 'RESTAURANT', label: 'Restaurante' },
]

watch(establishmentType, async (t) => {
  establishmentId.value = ''
  establishmentOptions.value = []
  if (!t) return
  establishmentsLoading.value = true
  try {
    if (t === 'HOTEL') {
      const hotels = await getAllHotels()
      establishmentOptions.value = (hotels ?? []).map((h: any) => ({ label: h.name ?? h.id, value: h.id }))
    } else if (t === 'RESTAURANT') {
      const restaurants = await getAllRestaurants()
      establishmentOptions.value = (restaurants ?? []).map((r: any) => ({ label: r.name ?? r.id, value: r.id }))
    }
  } catch (e: any) {
    // opcional: mostrar toast si lo tienes disponible globalmente
    console.error('No se pudieron cargar los establecimientos', e?.message || e)
  } finally {
    establishmentsLoading.value = false
  }
})

function applyFilter() {
  refetch()
}
function clearFilter() {
  establishmentType.value = ''
  establishmentId.value = ''
  establishmentOptions.value = []
  establishmentsLoading.value = false
  refetch()
}

const { employee } = storeToRefs(useAuthStore())

const { state, asyncStatus, refetch } = useCustomQuery({
  key: ['empleados', establishmentType, establishmentId],
  query: () => getAllEmployees(establishmentId.value ? { establishmentId: establishmentId.value } : {}),
})

</script>
