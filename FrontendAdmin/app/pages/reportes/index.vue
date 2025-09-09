<template>
  <div class="p-4 rounded-2xl bg-white shadow mb-5 border border-slate-200">
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-4 mb-4">
      <Dropdown v-model="reportType" :options="availableReports" optionLabel="label" optionValue="value" placeholder="Seleccionar reporte" class="w-full" />

      <!-- Establishment type + selector (optional by report; excludes MOST_POPULAR_ROOM) -->
      <div class="flex gap-2" v-if="needsEstablishment">
        <Dropdown
          v-model="establishmentType"
          :options="establishmentTypeOptions"
          optionLabel="label"
          optionValue="value"
          placeholder="Tipo de establecimiento"
          class="w-full"
        />
        <Dropdown
          v-if="establishmentType"
          v-model="establishmentId"
          :options="establishmentOptions"
          :loading="establishmentsLoading"
          optionLabel="label"
          optionValue="value"
          placeholder="Selecciona el establecimiento"
          class="w-full"
          showClear
          filter
        />
      </div>

      <!-- ONLY for MOST_POPULAR_ROOM: a single optional Hotel selector -->
      <div class="flex gap-2" v-else-if="needsHotelOnly">
        <Dropdown
          v-model="popularHotelId"
          :options="popularHotelOptions"
          :loading="popularHotelsLoading"
          optionLabel="label"
          optionValue="value"
          placeholder="Selecciona un hotel (opcional)"
          class="w-full"
          showClear
          filter
        />
      </div>

      <!-- Client filter (optional by report) -->
      <div v-if="needsClient" class="flex">
        <InputText v-model.trim="clientId" placeholder="ID o CUI de cliente" class="w-full" />
      </div>

      <!-- Date range (optional by report) -->
      <div class="flex gap-2">
        <DatePicker v-model="startDate" placeholder="Fecha de inicio" dateFormat="dd/mm/yy" class="w-full" />
        <DatePicker v-model="endDate" placeholder="Fecha de fin" dateFormat="dd/mm/yy" class="w-full" />
      </div>
    </div>

    <!-- Buttons -->
    <div class="flex flex-wrap gap-3 mt-4">
      <Button icon="pi pi-file-pdf" label="Exportar PDF" @click="exportReports" rounded outlined />
      <Button icon="pi pi-search" label="Filtrar" @click="filtrar" rounded outlined severity="info" />
      <Button icon="pi pi-refresh" label="Quitar Filtros" @click="recargarDatos" rounded outlined severity="help" />
    </div>
  </div>

  <div v-if="summary" class="p-4 mb-4 rounded-xl border border-slate-300 bg-slate-50 shadow-sm">
    <h3 class="text-slate-800 text-lg font-semibold mb-3">{{ summary.title }}</h3>
    <div class="grid grid-cols-1 sm:grid-cols-3 gap-4 text-sm text-slate-700">
      <div v-for="card in summary.cards" :key="card.label" class="p-3 rounded-lg bg-white border border-slate-200 shadow-sm">
        <span class="font-medium">{{ card.label }}:</span>
        <div class="font-semibold" :class="card.class || ''">{{ card.value ?? '-' }}</div>
      </div>
    </div>
  </div>

  <!-- tabla de resultados -->
  <DataTable :value="reportData.data" v-model:expandedRows="expandedRows" rowExpansionMode="single"
      :dataKey="tableConfig.dataKey" v-if="tableConfig" class="shadow-md border border-slate-200 rounded-xl " :loading="reportLoading">
      <template #header>
          <div class=" px-4 py-2">
              <span class="text-lg font-bold text-slate-800">
                  {{ tableConfig.reportHeader }}
              </span>
              <p class="text-xs text-slate-500 mt-1" v-if="tableConfig.reportSubheader">
                {{ tableConfig.reportSubheader }}
              </p>
          </div>
      </template>



      <!-- columnas dinámicas -->
      <Column v-for="col in tableConfig.columns" :key="col.field" :field="col.field" :header="col.header">
          <template #body="slotProps">
              {{ col.render ? col.render(slotProps.data) : slotProps.data[col.field] }}
          </template>
      </Column>


  </DataTable>
</template>


<script setup lang="ts">
import { ref, onMounted, render, computed, watch } from 'vue'
import { toast } from 'vue-sonner';
import { boolean } from 'zod';
import InputText from 'primevue/inputtext'
import Dropdown from 'primevue/dropdown'
import { getIncomeReport } from '~/lib/api/reportes/reporte';
import { getIncomeClientReport } from '~/lib/api/reportes/reporte';
import { getIncomeOutcomeReport } from '~/lib/api/reportes/reporte';
import { getMostPopularRoomReport } from '~/lib/api/reportes/reporte';
import { getMostPopularRestaurantReport } from '~/lib/api/reportes/reporte';
import { getAllHotels } from '~/lib/api/establishments/hotels'
import { getAllRestaurants } from '~/lib/api/establishments/restaurants'


//esto indica que las rows seran reactivas, se podran modificar, y por lo tanto se puede cambiar el valor y vue lo detectara
const expandedRows = ref({});
//esto indica que el tipo de reporte sera reactivo, y por lo tanto se puede cambiar el valor y vue lo detectara
const reportType = ref('ESTABLISHMENT_INCOME');

//inputs que siven para el filtrado de los reportes
const startDate = ref<Date | null>(null)
const endDate = ref<Date | null>(null)

/** Filtros opcionales controlados por el tipo de reporte */
const establishmentType = ref<'HOTEL' | 'RESTAURANT' | ''>('')
const establishmentId = ref<string>('') // id opcional (seleccionado desde dropdown)
const establishmentOptions = ref<Array<{ label: string; value: string }>>([])
const establishmentsLoading = ref(false)
const clientId = ref<string>('')        // id o CUI de cliente

const establishmentTypeOptions = [
  { value: '', label: '— Tipo —' },
  { value: 'HOTEL', label: 'Hotel' },
  { value: 'RESTAURANT', label: 'Restaurante' },
]

watch(establishmentType, async (t) => {
  establishmentId.value = ''
  establishmentOptions.value = []
  if (!t) return
  establishmentsLoading.value = true
  try {
    if (t === 'HOTEL') {
      const hotels = await getAllHotels()
      establishmentOptions.value = (hotels ?? []).map((h: any) => ({ label: h.name ?? h.id, value: h.id }))
    } else if (t === 'RESTAURANT') {
      const restaurants = await getAllRestaurants()
      establishmentOptions.value = (restaurants ?? []).map((r: any) => ({ label: r.name ?? r.id, value: r.id }))
    }
  } catch (e: any) {
    toast.error('No se pudieron cargar los establecimientos', { description: e?.message })
  } finally {
    establishmentsLoading.value = false
  }
})

/** Bandera de UI: qué filtros mostrar para cada reporte */
const needsEstablishment = computed(() => ['ESTABLISHMENT_INCOME', 'CLIENT_ACTIVITY'].includes(reportType.value))
const needsHotelOnly = computed(() => reportType.value === 'MOST_POPULAR_ROOM')
const needsClient = computed(() => ['CLIENT_ACTIVITY'].includes(reportType.value))
// For MOST_POPULAR_ROOM: single optional hotel selector
const popularHotelId = ref<string>('')
const popularHotelOptions = ref<Array<{ label: string; value: string }>>([])
const popularHotelsLoading = ref(false)

// Load hotels when switching to MOST_POPULAR_ROOM
watch(reportType, async (rt) => {
  if (rt === 'MOST_POPULAR_ROOM') {
    if (popularHotelOptions.value.length === 0) {
      popularHotelsLoading.value = true
      try {
        const hotels = await getAllHotels()
        popularHotelOptions.value = (hotels ?? []).map((h: any) => ({ label: h.name ?? h.id, value: h.id }))
      } catch (e: any) {
        toast.error('No se pudieron cargar los hoteles', { description: e?.message })
      } finally {
        popularHotelsLoading.value = false
      }
    }
  }
})

/** Resumen genérico */
const summary = ref<null | { title: string; cards: Array<{ label: string; value: string | number | null; class?: string }> }>(null)

const availableReports = [
  { value: 'ESTABLISHMENT_INCOME', label: 'Ingresos por establecimiento (rango de fechas)' },
  { value: 'CLIENT_ACTIVITY', label: 'Alojamientos y consumos por cliente (rango de fechas, opcional por establecimiento)' },
  { value: 'PROFIT_REPORT', label: 'Ganancias (costos vs ingresos)' },
  { value: 'MOST_POPULAR_ROOM', label: 'Habitación más popular (listado de alojamientos)' },
  { value: 'MOST_POPULAR_RESTAURANT', label: 'Restaurante más popular (listado de consumos)' },
]

type TableColumn = { field: string; header: string; render?: (row: any) => string | VNode }

const tableConfig = ref<{ dataKey: string; reportHeader: string; reportSubheader?: string; columns: TableColumn[] }>({
  dataKey: 'id',
  reportHeader: 'Seleccione un reporte',
  reportSubheader: '',
  columns: [],
})

/** Data del reporte */
const reportData = ref<{ data: any[] }>({ data: [] })
const reportLoading = ref(false)

function formatGTQ(v: number | null | undefined) {
  if (v === null || v === undefined || isNaN(Number(v))) return '—'
  try {
    return new Intl.NumberFormat('es-GT', { style: 'currency', currency: 'GTQ', minimumFractionDigits: 2 }).format(Number(v))
  } catch {
    return `Q ${Number(v).toFixed(2)}`
  }
}

/**
 * Metodo que se ejecuta al cargar el componente, se encarga de cargar automarticamente el reporte seleccionado
 * en el dropdown.
 */
onMounted(async () => {
    await cargarReporteActual()
})

/**
 * Configura la distribucion de la tabla, manda a trear la data segun e ltipo de reporte seleccionado
 * y la setea en el objeto reportData
 */
const cargarReporteActual = async () => {
  try {
    // Reset base data
    reportData.value.data = []
    summary.value = null

    switch (reportType.value) {
      case 'ESTABLISHMENT_INCOME': {
        tableConfig.value = {
          dataKey: 'id',
          reportHeader: 'Ingresos por establecimiento',
          reportSubheader: 'Listado de ingresos por fecha',
          columns: [
            { field: 'date', header: 'Fecha' },
            { field: 'establishmentId', header: 'Establecimiento' },
            { field: 'description', header: 'Descripcion' },
            { field: 'amount', header: 'Monto', render: (r:any) => r.amount != null ? `Q ${r.amount}` : '-' },
          ],
        }
        // Placeholder summary
        summary.value = {
          title: 'Resumen de ingresos',
          cards: [
            { label: 'Total de ingresos', value: null, class: 'text-green-700' },
            { label: 'Establecimiento', value: (establishmentOptions.value.find(o => o.value === establishmentId.value)?.label) || (establishmentId.value || 'Todos') },
            { label: 'Rango de fechas', value: (startDate.value && endDate.value) ? 'Aplicado' : '—' },
          ],
        }

        // Guard: require establishment type and id to fetch
        if (!establishmentType.value) {
          toast.info('Selecciona el tipo de establecimiento para continuar')
          break
        }
        if (!establishmentId.value) {
          toast.info('Selecciona un establecimiento para ver sus ingresos')
          break
        }

        reportLoading.value = true
        try {
          const res: any = await getIncomeReport(
            establishmentId.value,
            establishmentType.value,
            startDate.value,
            endDate.value
          )

          // Normalize list of payments
          const list: any[] = Array.isArray(res)
            ? res
            : (res?.payments ?? res?.items ?? res?.data ?? [])

          // Map to table rows
          reportData.value.data = (list || []).map((p: any) => ({
            id: p.id ?? p.paymentId ?? `${(p.reference || p.invoiceNumber || 'doc')}-${p.createdAt || p.paidAt || p.date || ''}`,
            date: p.date ?? p.paidAt ?? p.createdAt ?? p.updatedAt ?? null,
            establishmentId: establishmentId.value,
            amount: p.total ?? 0,
            description: p.description,
          }))

          // Compute total (fallback if API doesn't provide it)
          const computedTotal = reportData.value.data.reduce((acc, r) => acc + (Number(r.amount) || 0), 0)
          const apiTotal = Number(res?.total ?? res?.sum ?? res?.amount ?? NaN)
          const totalToShow = isNaN(apiTotal) ? computedTotal : apiTotal

          // Update summary cards with actual values
          summary.value = {
            title: 'Resumen de ingresos',
            cards: [
              { label: 'Total de ingresos', value: formatGTQ(totalToShow), class: 'text-green-700' },
              { label: 'Establecimiento', value: (establishmentOptions.value.find(o => o.value === establishmentId.value)?.label) || (establishmentId.value || '—') },
              { label: 'Rango de fechas', value: (startDate.value && endDate.value) ? 'Aplicado' : '—' },
            ],
          }
        } catch (err: any) {
            console.log("ERR")
            console.log(err)
          toast.error('No se pudo cargar el reporte de ingresos', { description: err?.message })
          reportData.value.data = []
        } finally {
          reportLoading.value = false
        }

        break
      }

      case 'CLIENT_ACTIVITY': {
        tableConfig.value = {
          dataKey: 'id',
          reportHeader: 'Ingresos por cliente',
          reportSubheader: 'Listado de ingresos (alojamientos y consumos) del cliente en el rango seleccionado',
          columns: [
            { field: 'date', header: 'Fecha' },
            { field: 'type', header: 'Tipo (Alojamiento/Consumo)' },
            { field: 'establishmentId', header: 'Establecimiento' },
            { field: 'document', header: 'Documento' },
            { field: 'amount', header: 'Monto', render: (r:any) => r.amount != null ? formatGTQ(r.amount) : '-' },
          ],
        }

        // Summary base
        summary.value = {
          title: 'Resumen por cliente',
          cards: [
            { label: 'Cliente', value: clientId.value || '—' },
            { label: 'Total registros', value: null },
            { label: 'Total monto', value: null },
          ],
        }

        // Guard: require client
        if (!clientId.value) {
          toast.info('Ingresa el ID/CUI del cliente para continuar')
          break
        }

        reportLoading.value = true
        try {
          const res: any = await getIncomeClientReport(
            clientId.value,
            establishmentId.value || '',
            startDate.value,
            endDate.value
          )

          // Normalize list (support several shapes)
          const list: any[] = Array.isArray(res)
            ? res
            : (res?.items ?? res?.data ?? res?.payments ?? [])

          // Map to table rows
          reportData.value.data = (list || []).map((it: any, idx: number) => {
            const date = it.date ?? it.paidAt ?? it.createdAt ?? it.updatedAt ?? null
            const doc = it.document ?? it.reference ?? it.invoiceNumber ?? it.orderId ?? it.reservationId ?? '—'
            // Heuristics to detect type
            const type =
              it.type ??
              (it.reservationId || it.stayId ? 'Alojamiento' :
               it.orderId ? 'Consumo' : (it.category || '—'))

            return {
              id: it.id ?? it.paymentId ?? doc ?? String(idx + 1),
              date,
              type,
              establishmentId: it.establishmentId ?? (establishmentId.value || '—'),
              document: doc,
              amount: Number(it.amount ?? it.totalAmount ?? it.total ?? 0),
            }
          })

          // Totals
          const totalRows = reportData.value.data.length
          const totalAmount = reportData.value.data.reduce((acc, r) => acc + (Number(r.amount) || 0), 0)
          summary.value = {
            title: 'Resumen por cliente',
            cards: [
              { label: 'Cliente', value: clientId.value || '—' },
              { label: 'Total registros', value: totalRows },
              { label: 'Total monto', value: formatGTQ(totalAmount) },
            ],
          }
        } catch (err: any) {
          toast.error('No se pudo cargar el reporte de ingresos por cliente', { description: err?.message })
          reportData.value.data = []
        } finally {
          reportLoading.value = false
        }
        break
      }

      case 'PROFIT_REPORT': {
        tableConfig.value = {
          dataKey: 'id',
          reportHeader: 'Ingresos vs Egresos',
          reportSubheader: 'Listado de ingresos (pagos) y resumen de egresos por rango de fechas',
          columns: [
            { field: 'date', header: 'Fecha' },
            { field: 'sourceType', header: 'Fuente' },
            { field: 'establishmentId', header: 'Establecimiento' },
            { field: 'clientId', header: 'Cliente' },
            { field: 'total', header: 'Monto', render: (r:any) => r.total != null ? formatGTQ(r.total) : '-' },
          ],
        }
        // Resumen base (se completará tras la consulta)
        summary.value = {
          title: 'Resumen de ganancias',
          cards: [
            { label: 'Ingresos', value: null, class: 'text-emerald-700' },
            { label: 'Egresos', value: null, class: 'text-rose-700' },
            { label: 'Ganancia Neta', value: null, class: 'text-slate-900' },
          ],
        }

        // Guard: este reporte requiere rango de fechas
        if (!startDate.value || !endDate.value) {
          toast.info('Selecciona un rango de fechas (inicio y fin) para ver ingresos y egresos')
          break
        }

        reportLoading.value = true
        try {
          const res: any = await getIncomeOutcomeReport(
            startDate.value,
            endDate.value
          )

          const payments: any[] = Array.isArray(res?.payments) ? res.payments : []
          // Map a filas de tabla (ingresos)
          reportData.value.data = payments.map((p: any) => ({
            id: p.id ?? `${p.sourceType || 'PAGO'}-${p.sourceId || ''}-${p.paidAt || ''}`,
            date: p.paidAt ?? p.date ?? p.createdAt ?? null,
            sourceType: p.sourceType ?? '—',
            establishmentId: p.establishmentId ?? '—',
            clientId: p.clientId ?? '—',
            total: Number(p.total ?? p.amount ?? 0),
          }))

          const totalIncome = Number(res?.totalIncome ?? reportData.value.data.reduce((acc, r) => acc + (Number(r.total) || 0), 0))
          const totalOutcome = Number(res?.totalOutcome ?? 0)
          const outcomeHotels = Number(res?.outcome?.totalOutcomeHotels ?? 0)
          const outcomeRestaurants = Number(res?.outcome?.totalOutcomeRestaurants ?? 0)
          const net = totalIncome - totalOutcome

          summary.value = {
            title: 'Resumen de ganancias',
            cards: [
              { label: 'Ingresos', value: formatGTQ(totalIncome), class: 'text-emerald-700' },
              { label: 'Egresos', value: formatGTQ(totalOutcome), class: 'text-rose-700' },
              { label: 'Ganancia Neta', value: formatGTQ(net), class: 'text-slate-900' },
              { label: 'Egresos Hoteles', value: formatGTQ(outcomeHotels) },
              { label: 'Egresos Restaurantes', value: formatGTQ(outcomeRestaurants) },
              { label: 'Pagos (ingresos) listados', value: payments.length },
            ],
          }
        } catch (err:any) {
          toast.error('No se pudo cargar el reporte de ingresos vs egresos', { description: err?.message })
          reportData.value.data = []
        } finally {
          reportLoading.value = false
        }
        break
      }

      case 'MOST_POPULAR_ROOM': {
        tableConfig.value = {
          dataKey: 'stayId',
          reportHeader: 'Habitación más popular',
          reportSubheader: 'Listado de alojamientos de la habitación con más reservas',
          columns: [
            { field: 'room', header: 'Habitación' },
            { field: 'hotel', header: 'Hotel' },
            { field: 'clientCui', header: 'Cliente (CUI)' },
            { field: 'startDate', header: 'Inicio' },
            { field: 'endDate', header: 'Fin' },
            { field: 'totalCost', header: 'Total', render: (r:any) => r.totalCost != null ? formatGTQ(r.totalCost) : '-' },
          ],
        }
        // Resumen base (se actualiza tras la consulta)
        summary.value = {
          title: 'Resumen de popularidad de habitación',
          cards: [
            { label: 'Habitación más popular', value: null },
            { label: 'Total alojamientos', value: null },
            { label: 'Hotel', value: (popularHotelOptions.value.find(o => o.value === popularHotelId.value)?.label) || (popularHotelId.value || 'Todos') },
          ],
        }

        reportLoading.value = true
        try {
          const res: any = await getMostPopularRoomReport(popularHotelId.value || null)
          const hotelName = res?.hotelName ?? (popularHotelOptions.value.find(o => o.value === popularHotelId.value)?.label) ?? '—'
          const roomNumber = res?.roomNumber ?? '—'
          const reservations: any[] = Array.isArray(res?.reservations) ? res.reservations : []

          // Mapear a filas de tabla (cada reserva)
          reportData.value.data = reservations.map((r: any) => ({
            stayId: r.id ?? `${r.roomId || roomNumber}-${r.startDate || ''}`,
            room: roomNumber !== '—' ? `#${roomNumber}` : (r.roomId || '—'),
            hotel: hotelName,
            clientCui: r.clientCui ?? '—',
            startDate: r.startDate ?? '—',
            endDate: r.endDate ?? '—',
            totalCost: Number(r.totalCost ?? r.total ?? 0),
          }))

          // Actualizar resumen con valores reales
          summary.value = {
            title: 'Resumen de popularidad de habitación',
            cards: [
              { label: 'Habitación más popular', value: roomNumber !== '—' ? `${roomNumber}` : (reservations[0]?.roomId ?? '—') },
              { label: 'Total alojamientos', value: reservations.length },
              { label: 'Hotel', value: hotelName || 'Todos' },
            ],
          }
        } catch (err:any) {
          toast.error('No se pudo cargar el reporte de habitación más popular', { description: err?.message })
          reportData.value.data = []
        } finally {
          reportLoading.value = false
        }
        break
      }

      case 'MOST_POPULAR_RESTAURANT': {
        tableConfig.value = {
          dataKey: 'orderId',
          reportHeader: 'Restaurante más popular',
          reportSubheader: 'Listado de consumos del restaurante con más ingresos',
          columns: [
            { field: 'restaurant', header: 'Restaurante' },
            { field: 'clientCui', header: 'Cliente (CUI)' },
            { field: 'items', header: 'Ítems' },
            { field: 'total', header: 'Total', render: (r:any) => r.total != null ? formatGTQ(r.total) : '-' },
          ],
        }
        summary.value = {
          title: 'Resumen de popularidad de restaurante',
          cards: [
            { label: 'Restaurante más popular', value: null },
            { label: 'Ingresos totales', value: null },
            { label: 'Rango de fechas', value: (startDate.value && endDate.value) ? 'Aplicado' : '—' },
          ],
        }

        reportLoading.value = true
        try {
          const res: any = await getMostPopularRestaurantReport()
          const restaurantName = res?.restaurantName ?? '—'
          const orders: any[] = Array.isArray(res?.orders) ? res.orders : []

          // Map orders to table rows
          reportData.value.data = orders.map((o: any, idx: number) => ({
            orderId: o.id ?? String(idx + 1),
            restaurant: restaurantName,
            clientCui: o.clientCui ?? '—',
            items: Array.isArray(o.items) && o.items.length
              ? o.items.map((it: any) => `${Number(it.quantity ?? 0)} x ${it.name ?? it.dishId ?? ''}`).join(', ')
              : '—',
            total: Number(o.total ?? 0),
          }))

          const totalIncome = reportData.value.data.reduce((acc, r) => acc + (Number(r.total) || 0), 0)

          summary.value = {
            title: 'Resumen de popularidad de restaurante',
            cards: [
              { label: 'Restaurante más popular', value: restaurantName },
              { label: 'Ingresos totales', value: formatGTQ(totalIncome) },
              { label: 'Rango de fechas', value: (startDate.value && endDate.value) ? 'Aplicado' : '—' },
            ],
          }
        } catch (err:any) {
          toast.error('No se pudo cargar el reporte de restaurante más popular', { description: err?.message })
          reportData.value.data = []
        } finally {
          reportLoading.value = false
        }
        break
      }

      default: {
        tableConfig.value = { dataKey: 'id', reportHeader: 'Seleccione un reporte', columns: [] }
        summary.value = null
      }
    }
  } catch (error:any) {
    toast.error('Error', { description: `${(error.message)}` })
  }
}

/**
watch(reportType, async () => {
  totalReservations.value = null as any
  totalAverange.value = null as any
  await cargarReporteActual()
})
 */

const exportReports = async () => {
  try {
    toast.info('Exportación en construcción', { description: 'Cuando el backend esté listo, este botón exportará el PDF del reporte actual.' })
  } catch (error:any) {
    toast.error('Error', { description: `${(error.message)}` })
  }
}

const recargarDatos = async () => {
  //elimina cualquier filtro y manda a cargar el reporte seleccionado de nuevo
  startDate.value = null;
  endDate.value = null;
  establishmentType.value = ''
  establishmentOptions.value = []
  establishmentsLoading.value = false
  establishmentId.value = ''
  clientId.value = ''
  popularHotelId.value = ''
  popularHotelOptions.value = []
  popularHotelsLoading.value = false
  // volvemos a cargar el reporte con filtros vacíos
  await cargarReporteActual();
};


/**
 * Manda a recargar el reporte para que tome en cuenta los filtros
 */
const filtrar = async () => {
    await cargarReporteActual();
};




</script>