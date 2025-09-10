<template>
  <div class="min-h-screen bg-slate-50">
    <!-- Header -->
    <header class="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
      <div class="flex items-center justify-between gap-4">
        <div class="flex items-center gap-3">
          <router-link :to="backLink">
            <Button icon="pi pi-arrow-left" label="Regresar" size="small" />
          </router-link>
          <div>
            <h1 class="text-2xl sm:text-3xl font-extrabold tracking-tight text-slate-900">Detalle de reservación</h1>
            <p class="text-slate-600 text-sm sm:text-base">Revisa la información de tu reserva.</p>
          </div>
        </div>
      </div>
    </header>

    <!-- Content -->
    <main class="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 pb-16">
      <div v-if="loading" class="rounded-2xl border border-slate-200 bg-white p-8 text-center text-slate-600">
        <i class="pi pi-spin pi-spinner mr-2"></i> Cargando detalle…
      </div>

      <div v-else-if="error" class="rounded-2xl border border-red-200 bg-red-50 p-4 text-red-800">
        {{ error }}
      </div>

      <div v-else-if="reservation" class="space-y-4">
        <!-- Summary card -->
        <section class="rounded-2xl border border-slate-200 bg-white p-4 sm:p-6 shadow-sm">
          <div class="flex items-start justify-between gap-6">
            <div>
              <div class="text-xs uppercase tracking-wide text-slate-500">Hotel</div>
              <div class="text-xl font-semibold text-slate-900">{{ reservation.hotel?.name || reservation.hotelId }}</div>
              <div class="text-sm text-slate-600" v-if="reservation.hotel?.address">{{ reservation.hotel.address }}</div>
            </div>
            <div class="text-right">
              <div class="text-xs uppercase tracking-wide text-slate-500">Total</div>
              <div class="text-2xl font-extrabold text-slate-900">{{ formatGTQ(reservation.totalCost) }}</div>
            </div>
          </div>

          <div class="mt-4 grid grid-cols-1 sm:grid-cols-3 gap-3">
            <div class="rounded-lg border border-slate-200 p-3">
              <div class="text-xs uppercase tracking-wide text-slate-500">Fecha de entrada</div>
              <div class="font-medium">{{ formatDate(reservation.startDate) }}</div>
            </div>
            <div class="rounded-lg border border-slate-200 p-3">
              <div class="text-xs uppercase tracking-wide text-slate-500">Fecha de salida</div>
              <div class="font-medium">{{ formatDate(reservation.endDate) }}</div>
            </div>
            <div class="rounded-lg border border-slate-200 p-3">
              <div class="text-xs uppercase tracking-wide text-slate-500">Habitación</div>
              <div class="font-medium">{{ roomLabel(reservation) }}</div>
            </div>
          </div>

          <div class="mt-4 grid grid-cols-1 sm:grid-cols-2 gap-3">
            <div class="rounded-lg border border-slate-200 p-3">
              <div class="text-xs uppercase tracking-wide text-slate-500">CUI del cliente</div>
              <div class="font-medium">{{ reservation.clientCui }}</div>
            </div>
            <div class="rounded-lg border border-slate-200 p-3">
              <div class="text-xs uppercase tracking-wide text-slate-500">ID de reservación</div>
              <div class="font-medium truncate">{{ reservation.id }}</div>
            </div>
          </div>

          <div class="mt-4 text-sm text-slate-700 space-y-1">
            <div>Subtotal: <strong>{{ formatGTQ(reservation.subtotal) }}</strong></div>
            <div v-if="reservation.promotionApplied" class="text-emerald-700">
              Promoción aplicada: {{ reservation.promotionApplied.name }} ({{ reservation.promotionApplied.percentOff }}%)
            </div>
          </div>
        </section>

        <!-- Existing reviews -->
        <section class="rounded-2xl border border-slate-200 bg-white p-4 sm:p-6 shadow-sm">
          <div class="flex items-center justify-between mb-4">
            <h2 class="text-lg font-semibold text-slate-900">Reseñas de la Habitacion</h2>
          </div>

          <div v-if="reviewsLoading" class="text-slate-600"><i class="pi pi-spin pi-spinner mr-2"></i> Cargando reseñas…</div>
          <div v-else>
            <div v-if="reviewsError" class="text-red-700 text-sm mb-2">{{ reviewsError }}</div>
            <div v-if="!roomReviews.length && !reviewsError" class="text-slate-600 text-sm">Aún no hay reseñas para esta habitacion.</div>
            <div v-else class="space-y-4">
              <div class="flex items-center gap-3 text-slate-800">
                <Rating :modelValue="Math.round(averageRating)" :readonly="true" :cancel="false"/>
                <span class="text-sm">{{ averageRating }}/5 ({{ roomReviews.length }})</span>
              </div>
              <ul class="divide-y divide-slate-200">
                <li v-for="r in roomReviews" :key="r.id" class="py-3">
                  <div class="flex items-start justify-between">
                    <Rating :modelValue="r.rating" :readonly="true" :cancel="false"/>
                  </div>
                  <p class="text-slate-700 text-sm mt-2 whitespace-pre-line">{{ r.comment }}</p>
                </li>
              </ul>
            </div>
          </div>
        </section>

        <!-- Review form -->
        <section class="rounded-2xl border border-slate-200 bg-white p-4 sm:p-6 shadow-sm">
          <div class="flex items-center justify-between mb-4">
            <h2 class="text-lg font-semibold text-slate-900">Deja tu reseña</h2>
          </div>

          <div class="space-y-4">
            <div class="flex items-center gap-3">
              <div class="text-sm text-slate-700 w-32">Calificación</div>
              <Rating v-model="reviewRating" :cancel="false" :stars="5" />
              <span class="text-xs text-slate-600">{{ reviewRating }}/5</span>
            </div>

            <div class="flex flex-col gap-2">
              <label class="text-sm text-slate-700">Comentario</label>
              <Textarea v-model="reviewComment" auto-resize rows="3" maxlength="500" placeholder="Comparte tu experiencia (máx. 500 caracteres)"/>
              <div class="text-xs text-slate-500 text-right">{{ reviewComment.length }}/500</div>
            </div>

            <div class="flex items-center justify-end gap-2">
              <Button
                :disabled="!canSubmitReview || submittingReview"
                :loading="submittingReview"
                icon="pi pi-send"
                label="Enviar reseña"
                @click="submitReview"
              />
            </div>
            <p v-if="reviewError" class="text-xs text-red-600">{{ reviewError }}</p>
          </div>
        </section>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import Button from 'primevue/button'
import Rating from 'primevue/rating'
import Textarea from 'primevue/textarea'
import { toast } from 'vue-sonner'
import { getReservationById, type Reservation } from '~/lib/api/reservations/reservations'
import { createReview, getReviews, type Review as RoomReview } from '~/lib/api/reviews/reviews'

const route = useRoute()
const router = useRouter()

const id = computed(() => route.params.id as string)
const backLink = computed(() => {
  const cui = route.query.cui as string | undefined
  return cui ? { path: '/mis-reservaciones', query: { cui } } : { path: '/mis-reservaciones' }
})

const loading = ref(true)
const error = ref('')
const reservation = ref<Reservation | null>(null)
const submittingReview = ref(false)
const reviewRating = ref<number>(0)
const reviewComment = ref('')
const reviewError = ref('')
const canSubmitReview = computed(() => reviewRating.value >= 1 && reviewRating.value <= 5 && reviewComment.value.trim().length > 0 && !!reservation.value)

// Room reviews state
const reviewsLoading = ref(false)
const reviewsError = ref('')
const roomReviews = ref<RoomReview[]>([])
const averageRating = computed(() => {
  if (!roomReviews.value.length) return 0
  const sum = roomReviews.value.reduce((acc, r) => acc + Number(r.rating || 0), 0)
  return Math.round((sum / roomReviews.value.length) * 10) / 10
})

onMounted(async () => {
  if (!id.value) {
    error.value = 'ID de reservación no proporcionado'
    loading.value = false
    return
  }
  try {
    const data = await getReservationById(id.value)
    reservation.value = (data as any) || null
    if (reservation.value?.roomId) {
      fetchRoomReviews(reservation.value.roomId)
    }
  } catch (e: any) {
    error.value = e?.message || 'No se pudo cargar la reservación'
  } finally {
    loading.value = false
  }
})

function formatGTQ(v: number | null | undefined) {
  if (v === null || v === undefined || isNaN(Number(v))) return '—'
  try {
    return new Intl.NumberFormat('es-GT', { style: 'currency', currency: 'GTQ', minimumFractionDigits: 2 }).format(Number(v))
  } catch {
    return `Q ${Number(v).toFixed(2)}`
  }
}

function toDate(d: any): Date | null {
  if (!d) return null
  try {
    if (d instanceof Date) return d
    const parsed = new Date(d)
    return isNaN(parsed.getTime()) ? null : parsed
  } catch { return null }
}

function formatDate(d: any) {
  const dt = toDate(d)
  if (!dt) return '—'
  try {
    return new Intl.DateTimeFormat('es-GT', { year: 'numeric', month: '2-digit', day: '2-digit' }).format(dt)
  } catch {
    const y = dt.getFullYear()
    const m = String(dt.getMonth() + 1).padStart(2, '0')
    const day = String(dt.getDate()).padStart(2, '0')
    return `${y}-${m}-${day}`
  }
}

function roomLabel(r: any) {
  const room = r?.room
  if (room?.number) return room.number
  if (room?.name) return room.name
  return r?.roomId?.slice?.(0, 6) || '—'
}

async function fetchRoomReviews(roomId: string) {
  reviewsError.value = ''
  reviewsLoading.value = true
  try {
    const res = await getReviews({ sourceId: roomId })
    roomReviews.value = Array.isArray(res) ? res : []
  } catch (e: any) {
    reviewsError.value = e?.message || 'No se pudieron cargar las reseñas'
    roomReviews.value = []
  } finally {
    reviewsLoading.value = false
  }
}

async function submitReview() {
  reviewError.value = ''
  if (!reservation.value) return
  if (!canSubmitReview.value) {
    reviewError.value = 'Por favor ingresa calificación (1–5) y comentario.'
    return
  }
  try {
    submittingReview.value = true
    await createReview({
      clientCui: reservation.value.clientCui,
      establishmentId: reservation.value.hotelId,
      establishmentType: "HOTEL",
      sourceId: reservation.value.roomId,
      comment: reviewComment.value.trim(),
      rating: reviewRating.value,
    })
    toast.success('¡Gracias! Tu reseña fue enviada.')
    // Reset form
    reviewRating.value = 0
    reviewComment.value = ''
    if (reservation.value?.roomId) {
      fetchRoomReviews(reservation.value.roomId)
    }
  } catch (e: any) {
    reviewError.value = e?.message || 'No se pudo enviar la reseña'
    toast.error(reviewError.value)
  } finally {
    submittingReview.value = false
  }
}
</script>

<style scoped>
</style>
