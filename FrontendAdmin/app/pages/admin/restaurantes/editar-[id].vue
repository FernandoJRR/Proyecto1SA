<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Header -->
    <header class="max-w-7xl mx-auto mb-6" role="banner">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link :to="`/admin/restaurantes/${route.params.id}`">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver a Restaurantes" />
          </router-link>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Editar Restaurante</h1>
        </div>
      </div>
    </header>

    <!-- Form Card -->
    <main class="max-w-7xl mx-auto" role="main">
      <div class="rounded-2xl border border-slate-200 bg-white shadow">
        <div class="p-6 sm:p-8">
          <Form :key="formKey" v-slot="$form" :initialValues :resolver @submit="onFormSubmit">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div class="md:col-span-2">
                <FloatLabel>
                  <label for="name">Nombre del restaurante</label>
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
            </div>

            <div class="mt-8 flex items-center justify-end gap-2">
              <router-link :to="`/admin/restaurantes/${route.params.id}`">
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
import { FloatLabel, InputText, Textarea } from 'primevue'
import { toast } from 'vue-sonner'
import { z } from 'zod'
import { getRestaurantById, updateRestaurant, type UpdateRestaurantPayload } from '~/lib/api/establishments/restaurants'

const route = useRoute()
const restaurantId = computed(() => route.params.id as string)

// Initial values populated after fetch
const initialValues = reactive({
  name: '',
  address: '',
})

const resolver = ref(
  zodResolver(
    z.object({
      name: z.string().min(1, 'El nombre es obligatorio.'),
      address: z.string().min(1, 'La dirección es obligatoria.'),
    })
  )
)

const submitting = ref(false)
const formKey = ref(0)

// Load and prefill form
const { state } = useCustomQuery({
  key: ['restaurant-edit', restaurantId.value],
  query: async () => {
    const r = await getRestaurantById(restaurantId.value)
    initialValues.name = r?.name ?? ''
    initialValues.address = r?.address ?? ''
    formKey.value++
    return r
  }
})

const { mutate } = useMutation({
  mutation: (payload: UpdateRestaurantPayload) => updateRestaurant(restaurantId.value, payload),
  onError(error: any) {
    submitting.value = false
    toast.error('Ocurrió un error al actualizar el restaurante', { description: error?.message })
  },
  onSuccess() {
    submitting.value = false
    toast.success('Restaurante actualizado correctamente')
    navigateTo(`/admin/restaurantes/${restaurantId.value}`)
  }
})

const onFormSubmit = (e: any) => {
  if (!e.valid) return
  submitting.value = true
  const v = e.values
  const payload: UpdateRestaurantPayload = {
    name: v.name,
    address: v.address,
  }
  mutate(payload)
}
</script>

