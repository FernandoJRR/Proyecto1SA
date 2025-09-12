<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Header -->
    <header class="max-w-7xl mx-auto mb-6" role="banner">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link :to="backTo">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver al restaurante" />
          </router-link>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Editar Platillo</h1>
        </div>
      </div>
    </header>

    <!-- Form Card -->
    <main class="max-w-7xl mx-auto" role="main">
      <div class="rounded-2xl border border-slate-200 bg-white shadow">
        <div class="p-6 sm:p-8">
          <Form :key="formKey" v-slot="$form" :initialValues :resolver @submit="onFormSubmit">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <FloatLabel>
                  <label for="name">Nombre del platillo</label>
                  <InputText id="name" name="name" type="text" class="w-full" autocomplete="off" />
                </FloatLabel>
                <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">{{ $form.name.error?.message }}</Message>
              </div>

              <div>
                <FloatLabel>
                  <label for="price">Precio (GTQ)</label>
                  <InputNumber id="price" name="price" :min="0.01" :minFractionDigits="2" :maxFractionDigits="2" mode="currency" currency="GTQ" class="w-full" />
                </FloatLabel>
                <Message v-if="$form.price?.invalid" severity="error" size="small" variant="simple">{{ $form.price.error?.message }}</Message>
              </div>
            </div>

            <div class="mt-8 flex items-center justify-end gap-2">
              <router-link :to="backTo">
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
import { FloatLabel, InputNumber, InputText, Message } from 'primevue'
import { toast } from 'vue-sonner'
import { z } from 'zod'
import { getRestaurantDish, updateDish, type UpdateDishPayload } from '~/lib/api/establishments/restaurants'

const route = useRoute()
const router = useRouter()

const restaurantId = computed(() => route.params.id as string)
const dishId = computed(() => (route.params as any).dishId as string)
const backTo = computed(() => `/admin/restaurantes/${restaurantId.value}`)

// Initial values populated after fetch
const initialValues = reactive({
  name: '',
  price: 0,
})

const resolver = ref(
  zodResolver(
    z.object({
      name: z.string().min(1, 'El nombre es obligatorio.'),
      price: z.number({ message: 'El precio es obligatorio.' }).min(0.01, 'Debe ser mayor a 0.'),
    })
  )
)

const submitting = ref(false)
const formKey = ref(0)

// Load dish and pre-fill
useCustomQuery({
  key: ['dish-edit', () => `${restaurantId.value}:${dishId.value}`],
  query: async () => {
    const dish = await getRestaurantDish(restaurantId.value, dishId.value)
    initialValues.name = dish?.name ?? ''
    initialValues.price = dish?.price != null ? Number(dish.price as any) : 0
    formKey.value++
    return dish
  }
})

const { mutate } = useMutation({
  mutation: (payload: UpdateDishPayload) => updateDish(restaurantId.value, dishId.value, payload),
  onError(error: any) {
    submitting.value = false
    toast.error('Ocurrió un error al actualizar el platillo', { description: error?.message })
  },
  onSuccess() {
    submitting.value = false
    toast.success('Platillo actualizado correctamente')
    router.push(`/admin/restaurantes/${restaurantId.value}`)
  }
})

const onFormSubmit = (e: any) => {
  if (!e.valid) return
  submitting.value = true
  const v = e.values
  const payload: UpdateDishPayload = {
    name: v.name,
    price: Number(v.price),
  }
  mutate(payload)
}
</script>

