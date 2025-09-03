<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Header -->
    <header class="max-w-7xl mx-auto mb-6" role="banner">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link :to="backTo">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" :aria-label="`Volver a ${backLabel}`" />
          </router-link>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Crear Habitación</h1>
        </div>
      </div>
      <div v-if="!hotelId" class="max-w-7xl mx-auto mt-3">
        <Message severity="warn" variant="simple">No se encontró <strong>hotelId</strong>. Abre esta vista desde el detalle del hotel.</Message>
      </div>
    </header>

    <!-- Context: Hotel info -->
    <div class="max-w-7xl mx-auto mb-4">
      <div class="rounded-xl border border-slate-200 bg-white shadow p-4">
        <div v-if="hotelAsyncStatus === 'loading'" class="text-slate-600">Cargando información del hotel…</div>
        <div v-else-if="!hotel || !hotel.name" class="text-slate-600">No se pudo cargar el hotel.</div>
        <div v-else class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2">
          <div>
            <p class="text-xs uppercase tracking-wide text-slate-500">Nueva habitación para</p>
            <h2 class="text-lg font-semibold text-slate-900">{{ hotel.name }}</h2>
            <p class="text-sm text-slate-600">{{ hotel.address || '—' }}</p>
          </div>
          <router-link :to="`/admin/hoteles/${hotelId}`">
            <Button icon="pi pi-external-link" label="Ver hotel" rounded />
          </router-link>
        </div>
      </div>
    </div>

    <!-- Form Card -->
    <main class="max-w-7xl mx-auto" role="main">
      <div class="rounded-2xl border border-slate-200 bg-white shadow">
        <div class="p-6 sm:p-8">
          <Form v-slot="$form" :initialValues="initialValues" :resolver="resolver" @submit="onFormSubmit">
            <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
              <section class="lg:col-span-2">
                <h2 class="text-lg font-semibold mb-8">Datos de la Habitación</h2>
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-8">
                  <div class="sm:col-span-2">
                    <FloatLabel>
                      <label for="name">Numero</label>
                      <InputText id="name" name="name" type="text" class="w-full" autocomplete="off" />
                    </FloatLabel>
                    <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">{{ $form.name.error?.message }}</Message>
                  </div>

                  <div>
                    <FloatLabel>
                      <label for="capacity">Capacidad (personas)</label>
                      <InputNumber id="capacity" name="capacity" :min="1" :useGrouping="false" class="w-full" />
                    </FloatLabel>
                    <Message v-if="$form.capacity?.invalid" severity="error" size="small" variant="simple">{{ $form.capacity.error?.message }}</Message>
                  </div>

                  <div>
                    <FloatLabel>
                      <label for="pricePerNight">Precio por noche (GTQ)</label>
                      <InputNumber id="pricePerNight" name="pricePerNight" :min="0.01" :minFractionDigits="2" :maxFractionDigits="2" mode="currency" currency="GTQ" class="w-full" />
                    </FloatLabel>
                    <Message v-if="$form.pricePerNight?.invalid" severity="error" size="small" variant="simple">{{ $form.pricePerNight.error?.message }}</Message>
                  </div>

                  <div class="sm:col-span-2">
                    <label class="block text-xs uppercase tracking-wide text-slate-500 mb-2" for="status">Estado</label>
                    <Dropdown
                      id="status"
                      name="status"
                      v-model="selectedStatus"
                      :options="statusOptions"
                      optionLabel="label"
                      optionValue="value"
                      placeholder="Selecciona un estado"
                      class="w-full"
                    />
                    <Message v-if="$form.status?.invalid" severity="error" size="small" variant="simple">{{ $form.status.error?.message }}</Message>
                  </div>
                </div>
              </section>

              <section>
                <h2 class="text-lg font-semibold mb-4">Ayuda</h2>
                <ul class="text-sm text-slate-600 space-y-2">
                  <li>Usa un nombre único para identificar esta habitación.</li>
                  <li>El precio se formatea como <strong>GTQ</strong> y debe ser mayor a 0.</li>
                  <li>La capacidad debe ser un número entero mayor o igual a 1.</li>
                </ul>
              </section>
            </div>

            <div class="mt-8 flex items-center justify-end gap-2">
              <router-link :to="backTo">
                <Button label="Cancelar" severity="secondary" outlined />
              </router-link>
              <Button type="submit" label="Crear habitación" icon="pi pi-save" :loading="submitting" :disabled="!hotelId" />
            </div>
          </Form>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { zodResolver } from '@primevue/forms/resolvers/zod'
import { FloatLabel, InputNumber, InputText, Message } from 'primevue'
import Dropdown from 'primevue/dropdown'
import { toast } from 'vue-sonner'
import { z } from 'zod'
import { getHotelById, createHotelRoom, type CreateRoomPayload } from '~/lib/api/establishments/hotels'

const route = useRoute()
const router = useRouter()

// id del hotel viene de params por estar anidado: /admin/hoteles/[id]/habitaciones/crear
const hotelId = computed(() => (route.params.id as string) || '')
const backTo = computed(() => (hotelId.value ? `/admin/hoteles/${hotelId.value}` : '/admin/hoteles'))
const backLabel = computed(() => (hotelId.value ? 'Hotel' : 'Hoteles'))

// Hotel context (for header card)
const { state: hotelState, asyncStatus: hotelAsyncStatus } = useCustomQuery({
  key: ['hotel', () => hotelId.value],
  query: () => getHotelById(hotelId.value),
})

const hotel = computed(() => (hotelState.value.data as any) || null)

const initialValues = reactive({
  name: '',
  capacity: 1,
  pricePerNight: 0,
  status: 'AVAILABLE'
})

const resolver = ref(
  zodResolver(
    z.object({
      name: z.string().min(1, 'El nombre es obligatorio.'),
      capacity: z.number({ message: 'La capacidad es obligatoria.' }).int('Debe ser un número entero.').min(1, 'Debe ser al menos 1.'),
      pricePerNight: z.number({ message: 'El precio por noche es obligatorio.' }).min(0.01, 'Debe ser mayor a 0.'),
      status: z.enum(['AVAILABLE','OCCUPIED','MAINTENANCE']).optional(),
    })
  )
)

const submitting = ref(false)
const selectedStatus = ref<'AVAILABLE' | 'OCCUPIED' | 'MAINTENANCE'>('AVAILABLE')

const statusOptions = [
  { label: 'Disponible', value: 'AVAILABLE' },
  { label: 'Ocupada', value: 'OCCUPIED' },
  { label: 'Mantenimiento', value: 'MAINTENANCE' },
]

const { mutate } = useMutation({
  mutation: (payload: { hotelId: string; data: CreateRoomPayload }) => createHotelRoom(payload.hotelId, payload.data),
  onError(error: any) {
    submitting.value = false
    toast.error('Ocurrió un error al crear la habitación', { description: error?.message })
  },
  onSuccess() {
    submitting.value = false
    toast.success('Habitación creada correctamente')
    if (hotelId.value) router.push(`/admin/hoteles/${hotelId.value}`)
    else router.push('/admin/hoteles')
  }
})

const onFormSubmit = (e: any) => {
  if (!e.valid || !hotelId.value) return
  submitting.value = true

  const v = e.values
  const payload: CreateRoomPayload = {
    number: v.name,
    capacity: Number(v.capacity),
    pricePerNight: Number(v.pricePerNight),
    status: selectedStatus.value,
  }

  mutate({ hotelId: hotelId.value, data: payload })
}
</script>
