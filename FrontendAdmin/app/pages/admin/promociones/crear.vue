<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Header -->
    <header class="max-w-7xl mx-auto mb-6" role="banner">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link to="/admin/promociones">
            <Button icon="pi pi-arrow-left" label="Volver" size="small" aria-label="Volver a Promociones" />
          </router-link>
          <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Crear Promoción</h1>
        </div>
      </div>
    </header>

    <!-- Form Card -->
    <main class="max-w-7xl mx-auto" role="main">
      <div class="rounded-2xl border border-slate-200 bg-white shadow">
        <div class="p-6 sm:p-8">
          <Form v-slot="$form" :initialValues="initialValues" :resolver="resolver" @submit="onFormSubmit">
            <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
              <!-- Datos de la Promoción -->
              <section class="lg:col-span-2">
                <h2 class="text-lg font-semibold mb-6">Datos de la Promoción</h2>

                <div class="grid grid-cols-1 sm:grid-cols-2 gap-8">
                  <!-- Nombre -->
                  <div class="sm:col-span-2">
                    <FloatLabel>
                      <label for="name">Nombre</label>
                      <InputText id="name" name="name" type="text" class="w-full" autocomplete="off" />
                    </FloatLabel>
                    <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">{{ $form.name.error?.message }}</Message>
                  </div>

                  <!-- Porcentaje -->
                  <div>
                      <label class="block text-xs uppercase tracking-wide text-slate-500 mb-2" for="establishmentType">Porcentaje de Descuento</label>
                      <InputNumber id="percentage" name="percentage" :min="0" :max="100" :useGrouping="false" suffix="%" class="w-full" />
                    <Message v-if="$form.percentage?.invalid" severity="error" size="small" variant="simple">{{ $form.percentage.error?.message }}</Message>
                  </div>

                  <!-- Ámbito -->
                  <div>
                    <label class="block text-xs uppercase tracking-wide text-slate-500 mb-2" for="establishmentType">Tipo de establecimiento</label>
                    <Dropdown
                      id="establishmentType"
                      name="establishmentType"
                      :options="estTypeOptions"
                      optionLabel="label"
                      optionValue="value"
                      placeholder="Selecciona un tipo"
                      class="w-full"
                      v-model="selectedEstType"
                      @change="onEstTypeChange"
                    />
                    <Message v-if="$form.establishmentType?.invalid" severity="error" size="small" variant="simple">{{ $form.establishmentType.error?.message }}</Message>
                    <p class="mt-1 text-xs text-slate-500">Los tipos de promoción mostrados arriba se filtran según el establecimiento seleccionado.</p>
                  </div>

                  <!-- Tipo de promoción -->
                  <div>
                    <label class="block text-xs uppercase tracking-wide text-slate-500 mb-2" for="promotionType">Tipo</label>
                    <Dropdown
                      id="promotionType"
                      name="promotionType"
                      :options="promotionTypeOptions"
                      optionLabel="label"
                      optionValue="value"
                      placeholder="Selecciona un tipo"
                      class="w-full"
                      v-model="selectedPromotionType"
                      :disabled="!selectedEstType"
                    />
                    <Message v-if="$form.promotionType?.invalid" severity="error" size="small" variant="simple">{{ $form.promotionType.error?.message }}</Message>
                    <p class="mt-1 text-xs text-slate-500">Primero selecciona el tipo de establecimiento para ver los tipos compatibles.</p>
                  </div>

                  <div>
                    <label class="block text-xs uppercase tracking-wide text-slate-500 mb-2" for="establishmentId">Establecimiento</label>
                    <Dropdown
                      id="establishmentId"
                      name="establishmentId"
                      :options="establishmentOptions"
                      optionLabel="label"
                      optionValue="value"
                      :disabled="!selectedEstType"
                      placeholder="Selecciona un establecimiento"
                      class="w-full"
                    />
                    <Message v-if="$form.establishmentId?.invalid" severity="error" size="small" variant="simple">{{ $form.establishmentId.error?.message }}</Message>
                  </div>

                  <!-- Fechas -->
                  <div>
                    <label class="block text-xs uppercase tracking-wide text-slate-500 mb-2" for="startDate">Inicio</label>
                    <Calendar id="startDate" name="startDate" dateFormat="yy-mm-dd" :manualInput="true" class="w-full" />
                    <Message v-if="$form.startDate?.invalid" severity="error" size="small" variant="simple">{{ $form.startDate.error?.message }}</Message>
                  </div>
                  <div>
                    <label class="block text-xs uppercase tracking-wide text-slate-500 mb-2" for="endDate">Fin</label>
                    <Calendar id="endDate" name="endDate" dateFormat="yy-mm-dd" :manualInput="true" class="w-full" />
                    <Message v-if="$form.endDate?.invalid" severity="error" size="small" variant="simple">{{ $form.endDate.error?.message }}</Message>
                  </div>

                  <!-- topCount -->
                  <div class="sm:col-span-2">
                    <label for="topCount" class="block text-xs uppercase tracking-wide text-slate-500 mb-2">Top Count</label>
                    <InputNumber
                      id="topCount"
                      name="topCount"
                      :min="1"
                      :max="10"
                      :step="1"
                      :useGrouping="false"
                      :showButtons="true"
                      buttonLayout="horizontal"
                      decrementIcon="pi pi-minus"
                      incrementIcon="pi pi-plus"
                      class="w-full"
                    />
                    <p class="mt-1 text-xs text-slate-500">Número de elementos a considerar (obligatorio para todos los tipos).</p>
                    <Message v-if="$form.topCount?.invalid" severity="error" size="small" variant="simple">{{ $form.topCount.error?.message }}</Message>
                  </div>
                </div>
              </section>
            </div>

            <div class="mt-8 flex items-center justify-end gap-2">
              <router-link to="/admin/promociones">
                <Button label="Cancelar" severity="secondary" outlined />
              </router-link>
              <Button type="submit" label="Crear promoción" icon="pi pi-save" :loading="submitting" />
            </div>
          </Form>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { zodResolver } from '@primevue/forms/resolvers/zod'
import { Form } from '@primevue/forms'
import { FloatLabel } from 'primevue'
import InputText from 'primevue/inputtext'
import InputNumber from 'primevue/inputnumber'
import Calendar from 'primevue/calendar'
import Dropdown from 'primevue/dropdown'
import Message from 'primevue/message'
import { toast } from 'vue-sonner'
import { z } from 'zod'
import { createPromotion, getPromotionTypes, type CreatePromotionPayload } from '~/lib/api/promotions/promotions'
import { getAllHotels } from '~/lib/api/establishments/hotels'
import { getAllRestaurants } from '~/lib/api/establishments/restaurants'

const initialValues = reactive({
  name: '',
  percentage: 0 as number | null,
  startDate: '' as string | Date,
  endDate: '' as string | Date,
  promotionType: '' as string,
  establishmentType: '' as 'HOTEL' | 'RESTAURANT' | '',
  establishmentId: '' as string,
  topCount: undefined as number | undefined,
})

const resolver = ref(
  zodResolver(
    z.object({
      name: z.string().min(1, 'El nombre es obligatorio.'),
      percentage: z.coerce.number().min(0).max(100),
      startDate: z.union([z.string().min(1), z.date()]),
      endDate: z.union([z.string().min(1), z.date()]),
      promotionType: z.string().min(1, 'Selecciona un tipo de promoción.'),
      establishmentType: z.enum(['HOTEL', 'RESTAURANT'], { required_error: 'Selecciona el tipo.' }),
      establishmentId: z.string().min(1, 'Selecciona un establecimiento.'),
      topCount: z.coerce.number({ invalid_type_error: 'Ingresa un número válido.' }).int('Debe ser entero.').min(1, 'Debe ser al menos 1.').max(10, 'Debe ser maximo 10.'),
    }).superRefine((v, ctx) => {
      // Fecha: start < end
      const s = typeof v.startDate === 'string' ? new Date(v.startDate) : v.startDate
      const e = typeof v.endDate === 'string' ? new Date(v.endDate) : v.endDate
      if (s && e && !(s < e)) {
        ctx.addIssue({ code: z.ZodIssueCode.custom, path: ['endDate'], message: 'La fecha de fin debe ser posterior a la de inicio.' })
      }

      // Compatibilidad tipo ↔ establecimiento
      const type = v.promotionType
      const est = v.establishmentType
      const isHotelOnly = type === 'ROOM_MOST_POPULAR'
      const isRestaurantOnly = type === 'DISH_MOST_POPULAR'
      const isBoth = type === 'CLIENT_MOST_FRECUENT'

      if (isHotelOnly && est !== 'HOTEL') {
        ctx.addIssue({ code: z.ZodIssueCode.custom, path: ['establishmentType'], message: 'Este tipo solo es compatible con Hotel.' })
      }
      if (isRestaurantOnly && est !== 'RESTAURANT') {
        ctx.addIssue({ code: z.ZodIssueCode.custom, path: ['establishmentType'], message: 'Este tipo solo es compatible con Restaurante.' })
      }
      if (!isHotelOnly && !isRestaurantOnly && !isBoth) {
        // Para otros tipos, no forzamos, pero mantenemos la validación por si se amplía.
      }
    })
  )
)

const submitting = ref(false)

// Promotion types
const { state: typesState } = useCustomQuery({ key: ['promotionTypes'], query: () => getPromotionTypes() })

// All types from API (unfiltered)
const promotionTypeOptionsAll = computed(() => (typesState.value.data ?? []).map((t: any) => ({ label: t.name || t.code, value: t.code })))

// Compatibility helper: restrict types per establishment type
function isTypeCompatible(typeCode: string, estType: 'HOTEL' | 'RESTAURANT' | '' ) {
  if (!typeCode) return false
  if (!estType) return true // sin tipo seleccionado aún → muestra todos
  if (typeCode === 'ROOM_MOST_POPULAR') return estType === 'HOTEL'
  if (typeCode === 'DISH_MOST_POPULAR') return estType === 'RESTAURANT'
  if (typeCode === 'CLIENT_MOST_FRECUENT') return estType === 'HOTEL' || estType === 'RESTAURANT'
  // default: permitir tipos futuros para ambos
  return true
}

// Filtered options depending on selected establishment type
const promotionTypeOptions = computed(() =>
  promotionTypeOptionsAll.value.filter(opt => isTypeCompatible(opt.value, selectedEstType.value))
)

const selectedPromotionType = ref<string>('')

// Establishment type and options
const estTypeOptions = [
  { label: 'Hotel', value: 'HOTEL' },
  { label: 'Restaurante', value: 'RESTAURANT' },
]
const selectedEstType = ref<'HOTEL' | 'RESTAURANT' | ''>('')

const { state: hotels } = useCustomQuery({ key: ['promoHotels'], query: () => getAllHotels() })
const { state: restaurants } = useCustomQuery({ key: ['promoRestaurants'], query: () => getAllRestaurants() })

const establishmentOptions = computed(() => {
  if (selectedEstType.value === 'HOTEL') {
    return (hotels.value.data ?? []).map((h: any) => ({ label: h.name, value: h.id }))
  }
  if (selectedEstType.value === 'RESTAURANT') {
    return (restaurants.value.data ?? []).map((r: any) => ({ label: r.name, value: r.id }))
  }
  return []
})

function onEstTypeChange() {
  // reset establishment when type changes
  (initialValues as any).establishmentId = ''
}

// If user picks a type incompatible with the current establishment type, clear establishment selection
watch(() => selectedPromotionType.value, (code) => {
  if (!code) return
  if (!isTypeCompatible(code, selectedEstType.value)) {
    selectedEstType.value = ''
    ;(initialValues as any).establishmentType = ''
    ;(initialValues as any).establishmentId = ''
  }
})

// If user changes establishment type and the currently selected type is incompatible, clear the type
watch(() => selectedEstType.value, (est) => {
  if (!selectedPromotionType.value) return
  if (!isTypeCompatible(selectedPromotionType.value, est)) {
    selectedPromotionType.value = ''
    ;(initialValues as any).promotionType = ''
  }
})

function fmt(d: string | Date): string {
  if (!d) return ''
  const dd = typeof d === 'string' ? new Date(d) : d
  const y = dd.getFullYear()
  const m = String(dd.getMonth() + 1).padStart(2, '0')
  const day = String(dd.getDate()).padStart(2, '0')
  return `${y}-${m}-${day}`
}

const { mutate } = useMutation({
  mutation: (payload: CreatePromotionPayload) => createPromotion(payload),
  onError(error: any) {
    submitting.value = false
    toast.error('Ocurrió un error al crear la promoción', { description: error?.message })
  },
  onSuccess() {
    submitting.value = false
    toast.success('Promoción creada correctamente')
    navigateTo('/admin/promociones')
  }
})

const onFormSubmit = (e: any) => {
  if (!e.valid) return
  submitting.value = true
  const v = e.values
  const payload: CreatePromotionPayload = {
    name: v.name,
    percentage: Number(v.percentage),
    startDate: fmt(v.startDate),
    endDate: fmt(v.endDate),
    promotionType: selectedPromotionType.value || v.promotionType,
    establishmentType: selectedEstType.value || v.establishmentType,
    establishmentId: v.establishmentId,
    topCount: v.topCount ? Number(v.topCount) : undefined,
  }
  mutate(payload)
}
</script>
