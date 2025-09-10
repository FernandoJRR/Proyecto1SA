<template>
  <div class="min-h-screen bg-gradient-to-b from-sky-50 to-white">
    <!-- Hero -->
    <header class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <div class="rounded-3xl overflow-hidden bg-[url('/hero-hotel.jpg')] bg-cover bg-center">
        <div class="backdrop-brightness-90 bg-black/20">
          <div class="px-6 sm:px-10 py-10 sm:py-14">
            <h1 class="text-3xl sm:text-4xl md:text-5xl font-extrabold tracking-tight text-white drop-shadow">
              Encuentra tu próxima estadía
            </h1>
            <p class="mt-2 text-white/90 text-sm sm:text-base max-w-2xl">
              Busca hoteles, elige fechas y reserva en minutos.
            </p>

            <!-- Booking widget -->
            <div class="mt-6 sm:mt-8">
              <div class="bg-white rounded-2xl shadow-xl border border-slate-200 p-4 sm:p-5">
                <div class="grid grid-cols-1 md:grid-cols-5 gap-3 items-end">
                  <!-- Start Date -->
                  <div class="md:col-span-2">
                    <label class="block text-xs font-semibold tracking-wide text-slate-600 mb-1" for="start">Fecha de entrada</label>
                    <Calendar
                      id="start"
                      v-model="startDate"
                      dateFormat="yy-mm-dd"
                      :manualInput="true"
                      showIcon
                      iconDisplay="input"
                      class="w-full"
                    />
                  </div>
                  <!-- End Date -->
                  <div class="md:col-span-2">
                    <label class="block text-xs font-semibold tracking-wide text-slate-600 mb-1" for="end">Fecha de salida</label>
                    <Calendar
                      id="end"
                      v-model="endDate"
                      dateFormat="yy-mm-dd"
                      :manualInput="true"
                      showIcon
                      iconDisplay="input"
                      class="w-full"
                    />
                  </div>

                  <!-- CTA -->
                  <div class="md:col-span-5 flex justify-end">
                    <Button :disabled="!canSearch" size="large" icon="pi pi-search" label="Buscar" @click="onSearch" />
                  </div>
                </div>

                <!-- Inline validation and helper -->
                <div class="mt-2 text-xs text-slate-500 flex items-center gap-3">
                  <span>Selecciona una fecha de inicio y fin.</span>
                  <Tag v-if="nights > 0" :value="`${nights} ${nights === 1 ? 'noche' : 'noches'}`" severity="info" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </header>

    <!-- Content -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pb-14" role="main">
      <!-- Benefits -->
      <section class="grid grid-cols-1 sm:grid-cols-3 gap-4 sm:gap-6 mb-8">
        <div class="rounded-2xl bg-white border border-slate-200 p-4 shadow-sm">
          <div class="flex items-start gap-3">
            <i class="pi pi-check-circle text-xl"></i>
            <div>
              <h3 class="font-semibold">Reserva en minutos</h3>
              <p class="text-sm text-slate-600">Proceso simple, sin pasos innecesarios.</p>
            </div>
          </div>
        </div>
        <div class="rounded-2xl bg-white border border-slate-200 p-4 shadow-sm">
          <div class="flex items-start gap-3">
            <i class="pi pi-percentage text-xl"></i>
            <div>
              <h3 class="font-semibold">Promociones disponibles</h3>
              <p class="text-sm text-slate-600">Aplicamos descuentos elegibles automáticamente.</p>
            </div>
          </div>
        </div>
        <div class="rounded-2xl bg-white border border-slate-200 p-4 shadow-sm">
          <div class="flex items-start gap-3">
            <i class="pi pi-shield text-xl"></i>
            <div>
              <h3 class="font-semibold">Pago seguro</h3>
              <p class="text-sm text-slate-600">Transacciones protegidas y transparentes.</p>
            </div>
          </div>
        </div>
      </section>

      <!-- Recently viewed / tips -->
      <section class="grid grid-cols-1 lg:grid-cols-3 gap-4">
        <div class="lg:col-span-2 rounded-2xl bg-white border border-slate-200 p-4 shadow-sm">
          <h3 class="font-semibold mb-2">Sugerencias para tu búsqueda</h3>
          <ul class="list-disc list-inside text-sm text-slate-700 space-y-1">
            <li>Empieza con el destino y las fechas; podrás ajustar detalles más adelante.</li>
            <li>Revisa si tu reserva aplica a promociones activas.</li>
          </ul>
        </div>
        <div class="rounded-2xl bg-sky-50 border border-sky-200 p-4">
          <h3 class="font-semibold text-sky-900">¿Ya tienes una reservación?</h3>
          <p class="text-sm text-sky-800 mt-1">Consulta o modifica tus reservas existentes.</p>
          <router-link to="/mis-reservaciones">
            <Button label="Ver mis reservaciones" size="small" class="mt-3" />
          </router-link>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import Calendar from 'primevue/calendar'
import Tag from 'primevue/tag'

const router = useRouter()
const { user } = storeToRefs(useAuthStore())

// Booking state (only start and end dates)
const startDate = ref<Date | null>(null)
const endDate = ref<Date | null>(null)

const nights = computed(() => {
  if (!startDate.value || !endDate.value) return 0
  const ms = endDate.value.getTime() - startDate.value.getTime()
  const d = Math.ceil(ms / (1000 * 60 * 60 * 24))
  return Math.max(0, d)
})

const canSearch = computed(() => !!startDate.value && !!endDate.value)

// Helpers
function ymd(d: Date | null): string {
  if (!d) return ''
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${y}-${m}-${day}`
}

function onSearch() {
  const query: any = {
    start: ymd(startDate.value),
    end: ymd(endDate.value),
  }
  router.push({ path: '/hoteles', query })
}

// Popular destinations (static seed - can be fed by API later)
const popularDestinations = reactive([
  { name: 'Ciudad de Guatemala', q: 'Guatemala City', image: '/destinations/guatemala-city.jpg', tagline: 'Centro histórico y vida nocturna', link: { path: '/hoteles' } },
  { name: 'Antigua Guatemala', q: 'Antigua', image: '/destinations/antigua.jpg', tagline: 'Arquitectura colonial y volcanes', link: { path: '/hoteles' } },
  { name: 'Panajachel', q: 'Panajachel', image: '/destinations/panajachel.jpg', tagline: 'Lago Atitlán y naturaleza', link: { path: '/hoteles' } },
  { name: 'Quetzaltenango', q: 'Xela', image: '/destinations/xela.jpg', tagline: 'Cultura y montañas', link: { path: '/hoteles' } },
])

defineExpose({ startDate, endDate, nights })
</script>