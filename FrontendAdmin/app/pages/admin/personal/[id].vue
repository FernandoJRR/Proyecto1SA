<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Page Header -->
    <header class="max-w-7xl mx-auto mb-6" role="banner">
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link to="/admin/personal">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver a Empleados" />
          </router-link>
          <div>
            <div class="flex items-center gap-3">
              <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">
                {{ fullName }}
              </h1>
              <Tag v-if="state.data?.employee?.employeeType" :value="state.data.employee.employeeType.name" />
              <Tag v-if="isDisabled" severity="danger" value="Desactivado" />
            </div>
            <p class="text-slate-600 text-sm">ID: <span class="font-mono">{{ state.data?.employee?.id }}</span></p>
          </div>
        </div>
        <div class="flex items-center gap-2">
          <router-link :to="`/admin/personal/editar-${state.data?.employee?.id}`">
            <Button icon="pi pi-pencil" label="Editar" severity="warn" outlined />
          </router-link>
          <router-link v-if="!isDisabled" :to="`/admin/personal/deshabilitar-${state.data?.employee?.id}`">
            <Button icon="pi pi-ban" label="Deshabilitar" severity="danger" outlined />
          </router-link>
          <router-link v-else :to="`/admin/personal/reactivar-${state.data?.employee?.id}`">
            <Button icon="pi pi-refresh" label="Reactivar" severity="help" outlined />
          </router-link>
        </div>
      </div>
    </header>

    <!-- Content -->
    <main class="max-w-7xl mx-auto" role="main">
      <div v-if="state.status === 'pending'" class="py-16 text-center text-slate-600">Cargando empleado…</div>
      <div v-else-if="state.status === 'error'" class="py-16 text-center text-red-600">Ocurrió un error inesperado.</div>

      <div v-else class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <!-- Card: Información principal -->
        <section class="lg:col-span-2 rounded-xl border border-slate-200 bg-white shadow">
          <div class="p-6">
            <h2 class="text-lg font-semibold mb-4">Información</h2>
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <div>
                <p class="text-xs uppercase tracking-wide text-slate-500">CUI</p>
                <p class="font-medium">{{ state.data.employee.cui || '—' }}</p>
              </div>
              <div>
                <p class="text-xs uppercase tracking-wide text-slate-500">Salario Semanal</p>
                <p class="font-medium">{{ salaryFormatted }}</p>
              </div>
              <div>
                <p class="text-xs uppercase tracking-wide text-slate-500">Estado</p>
                <div class="flex items-center gap-2">
                  <Tag v-if="!isDisabled" value="Activo" severity="success" />
                  <Tag v-else value="Desactivado" severity="danger" />
                </div>
              </div>
              <div>
                <p class="text-xs uppercase tracking-wide text-slate-500">Tipo de empleado</p>
                <p class="font-medium">{{ state.data.employee.employeeType?.name || '—' }}</p>
              </div>
            </div>
          </div>
        </section>

        <!-- Card: Asignación -->
        <aside class="rounded-xl border border-slate-200 bg-white shadow">
          <div class="p-6">
            <h2 class="text-lg font-semibold mb-4">Asignación</h2>
            <div v-if="assignedLabel">
              <p class="text-sm text-slate-600">Asignado a:</p>
              <div class="mt-2 flex items-center gap-2">
                <i class="pi pi-building text-slate-500" aria-hidden="true"></i>
                <span class="font-medium">{{ assignedLabel }}</span>
              </div>
            </div>
            <div v-else class="text-slate-600">No asignado a ningún establecimiento.</div>
          </div>
        </aside>
      </div>
    </main>
  </div>
</template>
<script setup lang="ts">
import { toast } from "vue-sonner";
import { getEmployeeById } from "~/lib/api/admin/employee";
import { useQueryCache } from "@pinia/colada";
import { computed } from "vue";
import { getRestaurantById } from "~/lib/api/establishments/restaurants";
import { getHotelById } from "~/lib/api/establishments/hotels";
import { ref, watchEffect } from 'vue'

const queryCache = useQueryCache();

const { state } = useCustomQuery({
  key: ["employee", useRoute().params.id as string],
  query: () =>
    getEmployeeById(useRoute().params.id as string).then((res) => {
      return {
        employee: res,
      };
    }),
});

// --- Establishment fetch depending on type ---
const assignedEstablishment = ref<any | null>(null)

watchEffect(async () => {
  const e: any = state.value.data?.employee
  if (!e?.establishmentId || !e?.establishmentType) {
    assignedEstablishment.value = null
    return
  }
  try {
    if (e.establishmentType === 'HOTEL') {
      assignedEstablishment.value = await getHotelById(e.establishmentId)
    } else if (e.establishmentType === 'RESTAURANT') {
      assignedEstablishment.value = await getRestaurantById(e.establishmentId)
    } else {
      assignedEstablishment.value = null
    }
  } catch {
    assignedEstablishment.value = null
  }
})

const isDisabled = computed(() => !!state.value.data?.employee?.desactivatedAt)

const fullName = computed(() => {
  const e = state.value.data?.employee
  if (!e) return ''
  return `${e.firstName ?? ''} ${e.lastName ?? ''}`.trim()
})

const salaryFormatted = computed(() => {
  const v = state.value.data?.employee?.salary
  if (v === null || v === undefined) return '—'
  // Q = Quetzales (GTQ)
  try {
    return new Intl.NumberFormat('es-GT', { style: 'currency', currency: 'GTQ', minimumFractionDigits: 2 }).format(Number(v))
  } catch {
    return `Q.${Number(v).toFixed(2)}`
  }
})

// Establishment / business assignment (supports multiple possible shapes)
const assignedLabel = computed(() => {
  if (assignedEstablishment.value) {
    const name = assignedEstablishment.value.name || assignedEstablishment.value.title
    const type = state.value.data?.employee?.establishmentType == "RESTAURANT" ? "RESTAURANTE" : "HOTEL"
    return [name, type].filter(Boolean).join(' · ')
  }
  return ''
})
</script>
