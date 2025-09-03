<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Header -->
    <header class="max-w-7xl mx-auto mb-6" role="banner">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link to="/admin/personal">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver a Empleados" />
          </router-link>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Crear Empleado</h1>
        </div>
      </div>
    </header>

    <!-- Form Card -->
    <main class="max-w-7xl mx-auto" role="main">
      <div class="rounded-2xl border border-slate-200 bg-white shadow">
        <div class="p-6 sm:p-8">
          <Form v-slot="$form" :initialValues :resolver @submit="onFormSubmit">
            <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
              <!-- Datos del Empleado -->
              <section class="lg:col-span-2">
                <h2 class="text-lg font-semibold mb-4">Datos del Empleado</h2>

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
                      <label for="cui">CUI</label>
                      <InputText id="cui" name="cui" type="text" class="w-full" inputmode="numeric" autocomplete="off" />
                    </FloatLabel>
                    <Message v-if="$form.cui?.invalid" severity="error" size="small" variant="simple">{{ $form.cui.error?.message }}</Message>
                  </div>
                  <div>
                    <FloatLabel>
                      <label for="salary">Salario semanal (GTQ)</label>
                      <InputNumber id="salary" name="salary" :min="0.01" :minFractionDigits="2" :maxFractionDigits="2" mode="currency" currency="GTQ" class="w-full" />
                    </FloatLabel>
                    <Message v-if="$form.salary?.invalid" severity="error" size="small" variant="simple">{{ $form.salary.error?.message }}</Message>
                    <p class="mt-1 text-xs text-slate-500">Este salario es <span class="font-semibold">semanal</span>.</p>
                  </div>
                  <div class="mt-6">
                    <template v-if="userTypes.status === 'success'">
                      <FloatLabel>
                        <label for="employeeTypeId">Tipo de Empleado</label>
                        <Select id="employeeTypeId" name="employeeTypeId" v-model="selectedType" optionLabel="name" optionValue="id" :options="userTypes.data" placeholder="Selecciona un tipo" class="w-full" />
                      </FloatLabel>
                      <Message v-if="$form.employeeTypeId?.invalid" severity="error" size="small" variant="simple">{{ $form.employeeTypeId.error?.message }}</Message>
                    </template>
                  </div>
                  <div>
                    <template v-if="establishments.status === 'success' || restaurants.status === 'success'">
                      <label class="block text-xs tracking-wide text-slate-500 mb-2" for="establishmentId">Establecimiento</label>
                      <Dropdown
                        id="establishmentId"
                        name="establishmentId"
                        v-model="selectedEstablishment"
                        :options="establishmentGroups"
                        optionGroupLabel="label"
                        optionGroupChildren="items"
                        optionLabel="label"
                        optionValue="value"
                        placeholder="Selecciona un establecimiento"
                        class="w-full"
                      />
                      <Message v-if="$form.establishmentId?.invalid" severity="error" size="small" variant="simple">{{ $form.establishmentId.error?.message }}</Message>
                    </template>
                  </div>
                  <div>
                    <FloatLabel>
                      <label for="hiredAt">Fecha de contratación</label>
                      <DatePicker id="hiredAt" name="hiredAt" class="w-full" />
                    </FloatLabel>
                    <Message v-if="$form.hiredAt?.invalid" severity="error" size="small" variant="simple">{{ $form.hiredAt.error?.message }}</Message>
                  </div>
                </div>
              </section>

              <!-- Usuario de acceso -->
              <section>
                <h2 class="text-lg font-semibold mb-4">Usuario de acceso</h2>
                <div class="space-y-4">
                  <div>
                    <FloatLabel>
                      <label for="username">Username</label>
                      <InputText id="username" name="username" type="text" class="w-full" autocomplete="username" />
                    </FloatLabel>
                    <Message v-if="$form.username?.invalid" severity="error" size="small" variant="simple">{{ $form.username.error?.message }}</Message>
                  </div>
                  <div class="mt-8">
                    <FloatLabel>
                      <Password id="password" name="password" :feedback="false" toggleMask class="w-full" :inputProps="{ autocomplete: 'new-password' }" />
                      <label for="password">Password</label>
                    </FloatLabel>
                    <Message v-if="$form.password?.invalid" severity="error" size="small" variant="simple">{{ $form.password.error?.message }}</Message>
                  </div>
                </div>
              </section>
            </div>

            <div class="mt-8 flex items-center justify-end gap-2">
              <router-link to="/admin/personal">
                <Button label="Cancelar" severity="secondary" outlined />
              </router-link>
              <Button type="submit" label="Crear empleado" icon="pi pi-save" :loading="submitting" />
            </div>
          </Form>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { zodResolver } from '@primevue/forms/resolvers/zod'
import { DatePicker, FloatLabel, InputNumber, Password, Select } from 'primevue'
import Dropdown from 'primevue/dropdown'
import { toast } from 'vue-sonner'
import { z } from 'zod'
import { createEmployee, type EmployeePayload } from '~/lib/api/admin/employee'
import { getAllEmployeeTypes } from '~/lib/api/admin/employee-type'
import { getAllHotels } from '~/lib/api/establishments/hotels'
import { getAllRestaurants } from '~/lib/api/establishments/restaurants'

// ---- Initial values (flat for Form) ----
const initialValues = reactive({
  firstName: '',
  lastName: '',
  salary: 0,
  cui: '',
  employeeTypeId: '',
  establishmentId: '',
  hiredAt: new Date(),
  username: '',
  password: ''
})

const submitting = ref(false)
const selectedType = ref('')
const selectedEstablishment = ref('')

// ---- Validation schema ----
const resolver = ref(
  zodResolver(
    z.object({
      firstName: z.string().min(1, 'Los nombres son obligatorios.'),
      lastName: z.string().min(1, 'Los apellidos son obligatorios.'),
      salary: z.number({ message: 'El salario semanal es obligatorio.' }).min(0.01, 'Debe ser mayor a 0.'),
      cui: z.string().regex(/^\d{13}$/, 'El CUI debe tener 13 dígitos.'),
      employeeTypeId: z.string().min(1, 'Selecciona un tipo de empleado.'),
      establishmentId: z.string().min(1, 'Selecciona un establecimiento.'),
      hiredAt: z.date({ required_error: 'La fecha de contratación es obligatoria.' }),
      username: z.string().min(8, 'El username debe tener al menos 8 caracteres.'),
      password: z.string().min(8, 'El password debe tener al menos 8 caracteres.'),
    })
  )
)

// ---- Data sources ----
const { state: userTypes } = useCustomQuery({
  key: ['optionsTypes'],
  query: () => getAllEmployeeTypes(),
})

watch(
  () => userTypes.value.data,
  (data) => {
    if (data && data.length > 0 && !selectedType.value) {
      selectedType.value = data[0].id
      initialValues.employeeTypeId = data[0].id
    }
  },
  { immediate: true }
)

const { state: establishments } = useCustomQuery({
  key: ['optionsHotels'],
  query: () => getAllHotels(),
})

const { state: restaurants } = useCustomQuery({
  key: ['optionsRestaurants'],
  query: () => getAllRestaurants(),
})

const establishmentGroups = computed(() => {
  const hotels = (establishments.value.data ?? []).map((h: any) => ({ label: h.name, value: h.id }))
  const rests = (restaurants.value.data ?? []).map((r: any) => ({ label: r.name, value: r.id }))
  const groups: any[] = []
  if (hotels.length) groups.push({ label: 'Hoteles', items: hotels })
  if (rests.length) groups.push({ label: 'Restaurantes', items: rests })
  return groups
})

watch(
  [() => establishments.value.data, () => restaurants.value.data],
  ([hotels, rests]) => {
    if (!selectedEstablishment.value) {
      const first = (hotels && hotels[0]) || (rests && rests[0])
      if (first) {
        selectedEstablishment.value = first.id
        initialValues.establishmentId = first.id
      }
    }
  },
  { immediate: true }
)

// ---- Submit ----
const { mutate } = useMutation({
  mutation: (employeeData: EmployeePayload) => createEmployee(employeeData),
  onError(error: any) {
    submitting.value = false
    toast.error('Ocurrió un error al crear el empleado', { description: error?.message })
  },
  onSuccess() {
    submitting.value = false
    toast.success('Empleado creado correctamente')
    navigateTo('/admin/personal')
  }
})

const onFormSubmit = (e: any) => {
  if (!e.valid) return
  submitting.value = true

  // Map values to the API contract exactly as required
  const values = e.values
  const payload: any = {
    firstName: values.firstName,
    lastName: values.lastName,
    salary: String(values.salary), // API expects string
    cui: Number(values.cui),
    employeeTypeId: values.employeeTypeId,
    establishmentId: values.establishmentId,
    hiredAt: formatDate(values.hiredAt),
    createUserRequest: {
      username: values.username,
      password: values.password,
    },
  }
  if (payload.establishmentId) {
    const id = payload.establishmentId
    const hotelList = (establishments.value?.data ?? []) as any[]
    const restaurantList = (restaurants.value?.data ?? []) as any[]

    const isHotel = hotelList.some((h) => h?.id === id)
    const isRestaurant = restaurantList.some((r) => r?.id === id)

    if (isHotel) {
      (payload as any).establishmentType = 'HOTEL'
    } else if (isRestaurant) {
      (payload as any).establishmentType = 'RESTAURANT'
    }
  }

  mutate(payload as EmployeePayload)
}

function formatDate(d: Date | string) {
  try {
    const date = typeof d === 'string' ? new Date(d) : d
    const yyyy = date.getFullYear()
    const mm = String(date.getMonth() + 1).padStart(2, '0')
    const dd = String(date.getDate()).padStart(2, '0')
    return `${yyyy}-${mm}-${dd}`
  } catch {
    return ''
  }
}
</script>
