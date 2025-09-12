<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Header -->
    <header class="max-w-7xl mx-auto mb-6" role="banner">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link :to="`/admin/personal/${route.params.id}`">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver a detalle" />
          </router-link>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Editar Empleado</h1>
        </div>
      </div>
    </header>

    <!-- Form Card -->
    <main class="max-w-7xl mx-auto" role="main">
      <div class="rounded-2xl border border-slate-200 bg-white shadow">
        <div class="p-6 sm:p-8">
          <Form :key="formKey" v-slot="$form" :initialValues :resolver @submit="onFormSubmit">
            <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
              <section class="lg:col-span-2">
                <h2 class="text-lg font-semibold mb-4">Datos Básicos</h2>
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-8">
                  <div>
                    <FloatLabel>
                      <label for="firstName">Nombres</label>
                      <InputText id="firstName" name="firstName" type="text" class="w-full" autocomplete="off" />
                    </FloatLabel>
                    <Message v-if="$form.firstName?.invalid" severity="error" size="small" variant="simple">{{ $form.firstName.error?.message }}</Message>
                  </div>
                  <div>
                    <FloatLabel>
                      <label for="lastName">Apellidos</label>
                      <InputText id="lastName" name="lastName" type="text" class="w-full" autocomplete="off" />
                    </FloatLabel>
                    <Message v-if="$form.lastName?.invalid" severity="error" size="small" variant="simple">{{ $form.lastName.error?.message }}</Message>
                  </div>
                  <div>
                    <FloatLabel>
                      <label for="salary">Salario semanal (GTQ)</label>
                      <InputNumber id="salary" name="salary" :min="0.01" :minFractionDigits="2" :maxFractionDigits="2" mode="currency" currency="GTQ" class="w-full" />
                    </FloatLabel>
                    <Message v-if="$form.salary?.invalid" severity="error" size="small" variant="simple">{{ $form.salary.error?.message }}</Message>
                    <p class="mt-1 text-xs text-slate-500">Este salario es <span class="font-semibold">semanal</span>.</p>
                  </div>
                </div>
              </section>
            </div>

            <div class="mt-8 flex items-center justify-end gap-2">
              <router-link :to="`/admin/personal/${route.params.id}`">
                <Button label="Cancelar" severity="secondary" outlined />
              </router-link>
              <Button type="submit" label="Guardar cambios" icon="pi pi-save" :loading="submitting" />
            </div>
          </Form>
        </div>
      </div>
    </main>
  </div>
  
</template>

<script setup lang="ts">
import { zodResolver } from '@primevue/forms/resolvers/zod'
import { FloatLabel, InputNumber } from 'primevue'
import { toast } from 'vue-sonner'
import { z } from 'zod'
import { getEmployeeById, updateEmployeeBasic, type EmployeeBasicUpdatePayload } from '~/lib/api/admin/employee'

const route = useRoute()
const employeeId = computed(() => route.params.id as string)

// ---- Initial values (filled after fetch) ----
const initialValues = reactive({
  firstName: '',
  lastName: '',
  salary: 0,
})

const submitting = ref(false)
const formKey = ref(0)

// ---- Validation schema ----
const resolver = ref(
  zodResolver(
    z.object({
      firstName: z.string().min(1, 'Los nombres son obligatorios.'),
      lastName: z.string().min(1, 'Los apellidos son obligatorios.'),
      salary: z.number({ message: 'El salario semanal es obligatorio.' }).min(0.01, 'Debe ser mayor a 0.'),
    })
  )
)

// ---- Load employee to pre-fill ----
const { state } = useCustomQuery({
  key: ['employee-edit', employeeId.value],
  query: async () => {
    const emp = await getEmployeeById(employeeId.value)
    // Pre-fill form
    initialValues.firstName = emp?.firstName ?? ''
    initialValues.lastName = emp?.lastName ?? ''
    // Ensure numeric value for InputNumber
    initialValues.salary = emp?.salary != null ? Number(emp.salary as any) : 0
    // Force Form to reinitialize with fetched values
    formKey.value++
    return emp
  }
})

// ---- Submit ----
const { mutate } = useMutation({
  mutation: (payload: EmployeeBasicUpdatePayload) => updateEmployeeBasic(payload, employeeId.value),
  onError(error: any) {
    submitting.value = false
    toast.error('Ocurrió un error al actualizar el empleado', { description: error?.message })
  },
  onSuccess() {
    submitting.value = false
    toast.success('Empleado actualizado correctamente')
    navigateTo(`/admin/personal/${employeeId.value}`)
  }
})

const onFormSubmit = (e: any) => {
  if (!e.valid) return
  submitting.value = true

  const values = e.values
  const payload: EmployeeBasicUpdatePayload = {
    firstName: values.firstName,
    lastName: values.lastName,
    // Keep consistent with create: backend may accept string
    salary: String(values.salary),
  }

  mutate(payload)
}
</script>
