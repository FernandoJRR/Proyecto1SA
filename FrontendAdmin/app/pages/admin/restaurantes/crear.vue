<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Header -->
    <header class="max-w-7xl mx-auto mb-6" role="banner">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link to="/admin/restaurantes">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver a Restaurantes" />
          </router-link>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Crear Restaurante</h1>
        </div>
      </div>
    </header>

    <!-- Form Card -->
    <main class="max-w-7xl mx-auto" role="main">
      <div class="rounded-2xl border border-slate-200 bg-white shadow">
        <div class="p-6 sm:p-8">
          <Form v-slot="$form" :initialValues="initialValues" :resolver="resolver" @submit="onFormSubmit">
            <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <!-- Datos del Restaurante -->
              <section class="lg:col-span-2">
                <h2 class="text-lg font-semibold mb-8">Datos del Restaurante</h2>

                <div class="grid grid-cols-1 sm:grid-cols-2 gap-8">
                  <div class="sm:col-span-2">
                    <FloatLabel>
                      <label for="name">Nombre</label>
                      <InputText id="name" name="name" type="text" class="w-full" autocomplete="off" />
                    </FloatLabel>
                    <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">{{ $form.name.error?.message }}</Message>
                  </div>

                  <div class="sm:col-span-2">
                    <FloatLabel>
                      <label for="address">Dirección</label>
                      <Textarea id="address" name="address" class="w-full" autoResize rows="3" autocomplete="off" />
                    </FloatLabel>
                    <Message v-if="$form.address?.invalid" severity="error" size="small" variant="simple">{{ $form.address.error?.message }}</Message>
                  </div>

                  <div class="sm:col-span-2">
                    <label class="block text-xs uppercase tracking-wide text-slate-500 mb-2" for="hotelId">Hotel (opcional)</label>
                    <Dropdown
                      id="hotelId"
                      name="hotelId"
                      v-model="selectedHotel"
                      :options="hotelOptions"
                      optionLabel="label"
                      optionValue="value"
                      placeholder="Selecciona un hotel o deja vacío"
                      class="w-full"
                    />
                    <p class="mt-1 text-xs text-slate-500">Si el restaurante no pertenece a un hotel, deja este campo sin seleccionar.</p>
                  </div>
                </div>
              </section>
            </div>

            <div class="mt-8 flex items-center justify-end gap-2">
              <router-link to="/admin/restaurantes">
                <Button label="Cancelar" severity="secondary" outlined />
              </router-link>
              <Button type="submit" label="Crear restaurante" icon="pi pi-save" :loading="submitting" />
            </div>
          </Form>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { zodResolver } from '@primevue/forms/resolvers/zod'
import { FloatLabel, InputText, Textarea } from 'primevue'
import Dropdown from 'primevue/dropdown'
import { toast } from 'vue-sonner'
import { z } from 'zod'
import { createRestaurant, type CreateRestaurantPayload } from '~/lib/api/establishments/restaurants'
import { getAllHotels } from '~/lib/api/establishments/hotels'

const initialValues = reactive({
  name: '',
  address: '',
  hotelId: '' as string | null | undefined,
})

const resolver = ref(
  zodResolver(
    z.object({
      name: z.string().min(1, 'El nombre es obligatorio.'),
      address: z.string().min(1, 'La dirección es obligatoria.'),
      // hotelId es opcional: permitir string no vacía o undefined/null
      hotelId: z.union([z.string().min(1), z.literal(''), z.null(), z.undefined()]).optional(),
    })
  )
)

const submitting = ref(false)
const selectedHotel = ref<string | '' | null>('')

// cargar hoteles para el dropdown
const { state: hotels } = useCustomQuery({
  key: ['optionsHotels'],
  query: () => getAllHotels(),
})

const hotelOptions = computed(() => {
  const opts = (hotels.value.data ?? []).map((h: any) => ({ label: h.name, value: h.id }))
  return [{ label: '— Sin hotel —', value: '' }, ...opts]
})

const { mutate } = useMutation({
  mutation: (payload: CreateRestaurantPayload) => createRestaurant(payload),
  onError(error: any) {
    submitting.value = false
    toast.error('Ocurrió un error al crear el restaurante', { description: error?.message })
  },
  onSuccess() {
    submitting.value = false
    toast.success('Restaurante creado correctamente')
    navigateTo('/admin/restaurantes')
  }
})

const onFormSubmit = (e: any) => {
  if (!e.valid) return
  submitting.value = true

  const v = e.values
  const hotelId = (selectedHotel.value || v.hotelId) as string | '' | null | undefined

  const payload: CreateRestaurantPayload = {
    name: v.name,
    address: v.address,
    hotelId: hotelId ? String(hotelId) : null,
  }

  mutate(payload)
}
</script>
