<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Header -->
    <header class="max-w-7xl mx-auto mb-6" role="banner">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link :to="backTo">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" :aria-label="`Volver a ${backLabel}`" />
          </router-link>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Crear Platillo</h1>
        </div>
      </div>
      <div v-if="!restaurantId" class="max-w-7xl mx-auto mt-3">
        <Message severity="warn" variant="simple">No se encontró <strong>restaurantId</strong>. Abre esta vista desde el detalle del restaurante o agrega <code>?restaurantId=ID</code> a la URL.</Message>
      </div>
    </header>

    <!-- Context: Restaurant info -->
    <div class="max-w-7xl mx-auto mb-4">
      <div class="rounded-xl border border-slate-200 bg-white shadow p-4">
        <div v-if="restaurantAsyncStatus === 'loading'" class="text-slate-600">Cargando información del restaurante…</div>
        <div v-else-if="!restaurant || !restaurant.name" class="text-slate-600">No se pudo cargar el restaurante.</div>
        <div v-else class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2">
          <div>
            <p class="text-xs uppercase tracking-wide text-slate-500">Nuevo platillo para</p>
            <h2 class="text-lg font-semibold text-slate-900">{{ restaurant.name }}</h2>
            <p class="text-sm text-slate-600">{{ restaurant.address || '—' }}</p>
          </div>
          <router-link :to="`/admin/restaurantes/${restaurantId}`">
            <Button icon="pi pi-external-link" label="Ver restaurante" rounded />
          </router-link>
        </div>
      </div>
    </div>

    <!-- Form Card -->
    <main class="max-w-7xl mx-auto" role="main">
      <div class="rounded-2xl border border-slate-200 bg-white shadow">
        <div class="p-6 sm:p-8">
          <Form v-slot="$form" :initialValues="initialValues" :resolver="resolver" @submit="onFormSubmit">
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
              <Button type="submit" label="Crear platillo" icon="pi pi-save" :loading="submitting" :disabled="!restaurantId" />
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
import { createDish, type CreateDishPayload, getRestaurantById } from '~/lib/api/establishments/restaurants'

const route = useRoute()
const router = useRouter()

// restaurantId puede venir como query (?restaurantId=...) porque esta vista no está anidada bajo /:id
const restaurantId = computed(() => (route.params.id as string) || '')
const backTo = computed(() => (restaurantId.value ? `/admin/restaurantes/${restaurantId.value}` : '/admin/restaurantes'))
const backLabel = computed(() => (restaurantId.value ? 'Restaurante' : 'Restaurantes'))

// Restaurant context (for header card)
const { state: restaurantState, asyncStatus: restaurantAsyncStatus } = useCustomQuery({
  key: ['restaurant', () => restaurantId.value],
  query: () => getRestaurantById(restaurantId.value),
})

const restaurant = computed(() => (restaurantState.value.data as any) || null)

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

const { mutate } = useMutation({
  mutation: (payload: { restaurantId: string; data: CreateDishPayload }) => createDish(payload.restaurantId, payload.data),
  onError(error: any) {
    submitting.value = false
    toast.error('Ocurrió un error al crear el platillo', { description: error?.message })
  },
  onSuccess() {
    submitting.value = false
    toast.success('Platillo creado correctamente')
    if (restaurantId.value) router.push(`/admin/restaurantes/${restaurantId.value}`)
    else router.push('/admin/restaurantes')
  }
})

const onFormSubmit = (e: any) => {
  if (!e.valid || !restaurantId.value) return
  submitting.value = true

  const v = e.values
  const payload: CreateDishPayload = {
    name: v.name,
    price: Number(v.price),
  }

  mutate({ restaurantId: restaurantId.value, data: payload })
}
</script>
