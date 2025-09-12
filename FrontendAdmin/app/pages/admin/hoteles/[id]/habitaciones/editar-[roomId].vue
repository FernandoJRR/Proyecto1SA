<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Header -->
    <header class="max-w-7xl mx-auto mb-6" role="banner">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link :to="backTo">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" :aria-label="`Volver a ${backLabel}`" />
          </router-link>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Editar Habitación</h1>
        </div>
      </div>
      <div v-if="!hotelId || !roomId" class="max-w-7xl mx-auto mt-3">
        <Message severity="warn" variant="simple">No se encontró <strong>hotelId</strong> o <strong>roomId</strong>. Abre esta vista desde el detalle del hotel.</Message>
      </div>
    </header>

    <!-- Context: Hotel info could be added similarly to crear.vue if needed -->

    <!-- Form Card -->
    <main class="max-w-7xl mx-auto" role="main">
      <div class="rounded-2xl border border-slate-200 bg-white shadow">
        <div class="p-6 sm:p-8">
          <Form :key="formKey" v-slot="$form" :initialValues :resolver @submit="onFormSubmit">
            <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
              <section class="lg:col-span-2">
                <h2 class="text-lg font-semibold mb-8">Datos de la Habitación</h2>
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-8">
                  <div class="sm:col-span-2">
                    <FloatLabel>
                      <label for="name">Número / Nombre</label>
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
                </div>
              </section>

              <section>
                <h2 class="text-lg font-semibold mb-4">Ayuda</h2>
                <ul class="text-sm text-slate-600 space-y-2">
                  <li>Actualiza el número/nombre, capacidad y precio por noche.</li>
                  <li>El precio se formatea como <strong>GTQ</strong> y debe ser mayor a 0.</li>
                  <li>La capacidad debe ser un número entero mayor o igual a 1.</li>
                </ul>
              </section>
            </div>

            <div class="mt-8 flex items-center justify-end gap-2">
              <router-link :to="backTo">
                <Button label="Cancelar" severity="secondary" outlined />
              </router-link>
              <Button type="submit" label="Guardar cambios" icon="pi pi-save" :loading="submitting" :disabled="!hotelId || !roomId" />
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
import { toast } from 'vue-sonner'
import { z } from 'zod'
import { getHotelRoom, updateHotelRoom, type UpdateRoomPayload } from '~/lib/api/establishments/hotels'

const route = useRoute()
const router = useRouter()

const hotelId = computed(() => (route.params.id as string) || '')
const roomId = computed(() => ((route.params as any).roomId ?? (route.params as any).rid) as string || '')
const backTo = computed(() => (hotelId.value ? `/admin/hoteles/${hotelId.value}` : '/admin/hoteles'))
const backLabel = computed(() => (hotelId.value ? 'Hotel' : 'Hoteles'))

// Initial values to be filled from fetch
const initialValues = reactive({
  name: '',
  capacity: 1,
  pricePerNight: 0,
})

const resolver = ref(
  zodResolver(
    z.object({
      name: z.string().min(1, 'El nombre es obligatorio.'),
      capacity: z.number({ message: 'La capacidad es obligatoria.' }).int('Debe ser un número entero.').min(1, 'Debe ser al menos 1.'),
      pricePerNight: z.number({ message: 'El precio por noche es obligatorio.' }).min(0.01, 'Debe ser mayor a 0.'),
    })
  )
)

const submitting = ref(false)
const formKey = ref(0)

// Load room and pre-fill
const { state } = useCustomQuery({
  key: ['hotel-room-edit', () => `${hotelId.value}:${roomId.value}`],
  query: async () => {
    if (!hotelId.value || !roomId.value) return null as any
    const room = await getHotelRoom(hotelId.value, roomId.value)
    // name can be returned as number or name depending on backend
    initialValues.name = (room as any)?.number ?? (room as any)?.name ?? ''
    initialValues.capacity = room?.capacity != null ? Number(room.capacity as any) : 1
    initialValues.pricePerNight = room?.pricePerNight != null ? Number(room.pricePerNight as any) : 0
    formKey.value++
    return room
  }
})

const { mutate } = useMutation({
  mutation: (payload: UpdateRoomPayload) => updateHotelRoom(hotelId.value, roomId.value, payload),
  onError(error: any) {
    submitting.value = false
    toast.error('Ocurrió un error al actualizar la habitación', { description: error?.message })
  },
  onSuccess() {
    submitting.value = false
    toast.success('Habitación actualizada correctamente')
    if (hotelId.value) router.push(`/admin/hoteles/${hotelId.value}`)
    else router.push('/admin/hoteles')
  }
})

const onFormSubmit = (e: any) => {
  if (!e.valid || !hotelId.value || !roomId.value) return
  submitting.value = true

  const v = e.values
  const payload: UpdateRoomPayload = {
    number: v.name,
    capacity: Number(v.capacity),
    pricePerNight: Number(v.pricePerNight),
  }

  mutate(payload)
}
</script>

