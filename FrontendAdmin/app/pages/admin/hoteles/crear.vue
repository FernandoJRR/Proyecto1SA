<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Header -->
    <header class="max-w-7xl mx-auto mb-6" role="banner">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link to="/admin/hoteles">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver a Hoteles" />
          </router-link>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Crear Hotel</h1>
        </div>
      </div>
    </header>

    <!-- Form Card -->
    <main class="max-w-7xl mx-auto" role="main">
      <div class="rounded-2xl border border-slate-200 bg-white shadow">
        <div class="p-6 sm:p-8">
          <Form v-slot="$form" :initialValues="initialValues" :resolver="resolver" @submit="onFormSubmit">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div class="md:col-span-2">
                <FloatLabel>
                  <label for="name">Nombre del hotel</label>
                  <InputText id="name" name="name" type="text" class="w-full" autocomplete="off" />
                </FloatLabel>
                <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">{{ $form.name.error?.message }}</Message>
              </div>

              <div class="md:col-span-2">
                <FloatLabel>
                  <label for="address">Dirección</label>
                  <Textarea id="address" name="address" class="w-full" autoResize rows="3" autocomplete="off" />
                </FloatLabel>
                <Message v-if="$form.address?.invalid" severity="error" size="small" variant="simple">{{ $form.address.error?.message }}</Message>
              </div>

              <div>
                <FloatLabel>
                  <label for="maintenanceCostPerWeek">Costo de mantenimiento (semanal)</label>
                  <InputNumber id="maintenanceCostPerWeek" name="maintenanceCostPerWeek" :min="0.01" :minFractionDigits="2" :maxFractionDigits="2" mode="currency" currency="GTQ" class="w-full" />
                </FloatLabel>
                <Message v-if="$form.maintenanceCostPerWeek?.invalid" severity="error" size="small" variant="simple">{{ $form.maintenanceCostPerWeek.error?.message }}</Message>
                <p class="mt-1 text-xs text-slate-500">Este costo es <span class="font-semibold">semanal</span>.</p>
              </div>
            </div>

            <div class="mt-8 flex items-center justify-end gap-2">
              <router-link to="/admin/hoteles">
                <Button label="Cancelar" severity="secondary" outlined />
              </router-link>
              <Button type="submit" label="Crear hotel" icon="pi pi-save" :loading="submitting" />
            </div>
          </Form>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { zodResolver } from '@primevue/forms/resolvers/zod'
import { FloatLabel, InputNumber, InputText, Textarea } from 'primevue'
import { toast } from 'vue-sonner'
import { z } from 'zod'
import { createHotel, type CreateHotelPayload } from '~/lib/api/establishments/hotels'

const initialValues = reactive({
  name: '',
  address: '',
  maintenanceCostPerWeek: 0,
})

const resolver = ref(
  zodResolver(
    z.object({
      name: z.string().min(1, 'El nombre es obligatorio.'),
      address: z.string().min(1, 'La dirección es obligatoria.'),
      maintenanceCostPerWeek: z.number({ message: 'El costo de mantenimiento es obligatorio.' }).min(0.01, 'Debe ser mayor a 0.'),
    })
  )
)

const submitting = ref(false)

const { mutate } = useMutation({
  mutation: (payload: CreateHotelPayload) => createHotel(payload),
  onError(error: any) {
    submitting.value = false
    toast.error('Ocurrió un error al crear el hotel', { description: error?.message })
  },
  onSuccess() {
    submitting.value = false
    toast.success('Hotel creado correctamente')
    navigateTo('/admin/hoteles')
  }
})

const onFormSubmit = (e: any) => {
  if (!e.valid) return
  submitting.value = true

  const v = e.values
  const payload: CreateHotelPayload = {
    name: v.name,
    address: v.address,
    maintenanceCostPerWeek: Number(v.maintenanceCostPerWeek),
  }

  mutate(payload)
}
</script>
