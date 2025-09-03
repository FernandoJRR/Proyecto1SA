<template>
  <div class="p-8 max-w-4xl mx-auto bg-white shadow-md rounded-2xl border border-gray-200">
    <div class="mb-6 flex justify-between items-center">
      <h1 class="text-3xl font-bold">Detalle de Reservación</h1>
      <router-link to="/reservaciones/">
        <Button label="Volver al listado" icon="pi pi-arrow-left" text />
      </router-link>
    </div>

    <div v-if="reservation" class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div>
        <p class="font-medium text-gray-600">ID de la Reservacion</p>
        <p class="text-lg font-semibold">{{ reservation.id }}</p>
      </div>

      <div>
        <p class="font-medium text-gray-600">ID del Juego</p>
        <p class="text-lg font-semibold">{{ reservation.gameId }}</p>
      </div>

      <div>
        <p class="font-medium text-gray-600">Fecha</p>
        <p class="text-lg">{{ reservation.date }}</p>
      </div>

      <div>
        <p class="font-medium text-gray-600">Hora de inicio</p>
        <p class="text-lg">{{ reservation.startTime }}</p>
      </div>

      <div>
        <p class="font-medium text-gray-600">Hora de fin</p>
        <p class="text-lg">{{ reservation.endTime }}</p>
      </div>

      <div>
        <p class="font-medium text-gray-600">Nombre del cliente</p>
        <p class="text-lg">{{ reservation.customerFullName }}</p>
      </div>

      <div>
        <p class="font-medium text-gray-600">Nit del cliente</p>
        <p class="text-lg">{{ reservation.customerNIT }}</p>
      </div>


      <div>
        <p class="font-medium text-gray-600">Estado de pago</p>
        <Tag v-if="!reservation.notShow && reservation.paid" severity="info" value="Asistió" />
        <Tag v-else-if="!reservation.notShow && !reservation.paid" severity="warn" value="Programada" />
        <Tag v-else-if="reservation.notShow" severity="danger" value="Cancelada" />
      </div>

      <div>
        <p class="font-medium text-gray-600">El usuario se ha asistido?</p>
        <Tag :severity="reservation.notShow ? 'danger' : 'info'"
          :value="reservation.notShow ? 'No se presentó' : 'Asistió'" />
      </div>
    </div>

    <div v-else class="text-center text-gray-500">
      Cargando reservación...
    </div>
  </div>
  <div v-if="!reservation?.paid && !reservation?.notShow"
    class="p-8 mt-4 max-w-4xl mx-auto bg-white shadow-md rounded-2xl border border-gray-200">
    <div class="mb-6 flex justify-between items-center">
      <h1 class="text-3xl font-bold">Completar Reservacion</h1>
    </div>

    <div class="">
      <Stepper v-model:value="stepIndex" class="p-10" linear>
        <StepList>
          <Step :value="1">Agrega Items</Step>
          <Step :value="2">Facturacion</Step>
        </StepList>

        <StepPanels>
          <!-- Paso 1 -->
          <StepPanel :value="1" v-slot="{ activateCallback }">
            <div class="space-y-6">
              <h2 class="text-2xl font-semibold">Agrega Items a tu Juego</h2>
              <div class="flex flex-row gap-x-2 items-center justify-center">
                <div class="w-full">
                  <label class="block text-sm font-medium mb-1">Paquete</label>
                  <Dropdown v-model="selectedPackage" :options="packageOptions" optionLabel="label" optionValue="value"
                    placeholder="Elegir paquete" class="w-full" />
                </div>
                <Button class="mt-6 w-full" label="Agregar" icon="pi pi-plus" outlined @click="addDetailPackage" />
              </div>

              <div v-if="selectedPackage?.packageDetail?.length">
                <h3 class="text-xl font-semibold mb-2">Precio: Q{{ selectedPackage.price }} - Productos incluidos:</h3>
                <ul class="list-disc list-inside space-y-1">
                  <li v-for="(detail, index) in selectedPackage.packageDetail" :key="index">
                    {{ detail.quantity }} x {{ detail.product.name }} (Q{{ detail.product.price }})
                  </li>
                </ul>
              </div>
              <div class="flex flex-row gap-x-2 items-center justify-center">
                <div class="w-full">
                  <label class="block text-sm font-medium mb-1">Producto</label>
                  <Dropdown v-model="tempDetail.itemId" :options="itemOptions" optionLabel="name" optionValue="id"
                    placeholder="Seleccionar producto" class="w-full" @change="updatePrice" />
                </div>
                <div>
                  <label class="block text-sm font-medium mb-1">Cantidad</label>
                  <InputNumber v-model="tempDetail.quantity" :min="1" class="w-full" />
                </div>
                <Button class="mt-6 w-full" label="Agregar" icon="pi pi-plus" outlined @click="addDetail" />
              </div>
              <div class="max-h-40 overflow-y-auto">
                <DataTable :value="extraDetails" class="">
                  <Column header="Producto">
                    <template #body="{ data }">
                      {{ data.itemName }}
                    </template>
                  </Column>
                  <Column header="Cantidad" field="quantity" />
                  <Column header="Precio Unitario">
                    <template #body="{ data }">
                      Q{{ data.unitPrice.toFixed(2) }}
                    </template>
                  </Column>
                  <Column header="Total">
                    <template #body="{ data }">
                      Q{{ (data.quantity * data.unitPrice).toFixed(2) }}
                    </template>
                  </Column>
                  <Column header="Acciones">
                    <template #body="{ index }">
                      <Button icon="pi pi-trash" text severity="danger" @click="extraDetails.splice(index, 1)" />
                    </template>
                  </Column>
                </DataTable>
              </div>

              <div class="pt-4 flex justify-between">
                <Button label="Siguiente" icon="pi pi-arrow-right" @click="activateCallback(2)" />
              </div>
            </div>
          </StepPanel>



          <!-- Paso 4 -->
          <StepPanel :value="2" v-slot="{ activateCallback }">
            <div class="space-y-6">
              <h2 class="text-2xl font-semibold">Resumen y Facturación</h2>

              <!-- Paquete -->
              <div v-if="selectedPackage">
                <h3 class="text-lg font-medium">Paquete seleccionado:</h3>
                <p class="text-gray-700">{{ selectedPackage.name }} - Q{{ selectedPackage.price.toFixed(2) }}</p>
              </div>

              <!-- Extras -->
              <div v-if="extraDetails.length > 0">
                <h3 class="text-lg font-medium">Productos adicionales:</h3>
                <ul class="list-disc list-inside">
                  <li v-for="(item, index) in extraDetails" :key="index">
                    {{ item.quantity }} x {{ itemMap[item.itemId]?.name ?? '—' }} (Q{{ item.unitPrice.toFixed(2) }}) =
                    <strong>Q{{ (item.quantity * item.unitPrice).toFixed(2) }}</strong>
                  </li>
                </ul>
              </div>

              <!-- Total -->
              <div class="text-right text-xl font-semibold border-t pt-4">
                Total: Q{{ totalInvoice.toFixed(2) }}
              </div>

              <!-- Método de pago -->
              <div>
                <label class="block text-sm font-medium mb-1">Método de pago</label>
                <Dropdown v-model="selectedPaymentMethod" :options="paymentOptions" optionLabel="name"
                  optionValue="paymentMethod" placeholder="Seleccionar método de pago" class="w-full" />
              </div>

              <!-- Acciones -->
              <div class="pt-4 flex justify-between">
                <Button label="Atrás" icon="pi pi-arrow-left" @click="activateCallback(1)" severity="secondary" />
                <Button label="Completar Reservación" icon="pi pi-save" @click="onFormSubmit" />
              </div>
            </div>
          </StepPanel>

        </StepPanels>
      </Stepper>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'
import { getReservationById, payReservation, type PayReservationPayload, type Reservation } from '~/lib/api/reservation/reservation'
import { toast } from 'vue-sonner'
import { getAllSchedules, type Schedule } from '~/lib/api/schedules/schedule'
import { getAllPackages, type Package } from '~/lib/api/package/package'
import { getPaymentMethods, type PaymentMethod } from '~/lib/api/invoices/invoice'
import { getAllProducts } from '~/lib/api/products/product'

const route = useRoute()
const reservation = ref<Reservation | null>(null)

const stepIndex = ref(1);

const selectedSchedule = ref<Schedule | null>(null);
const selectedPackage = ref<Package | null>(null);

const form = reactive({
  date: "",
  startTime: "",
  endTime: "",
  customerNIT: "",
  customerFullName: "",
  selectedPackageId: "",
});

const { data: packages } = useCustomQuery({
  key: ['getAllPackages'],
  query: () => getAllPackages()
});

const packageOptions = computed(() =>
  (packages.value ?? []).map((p: Package) => ({
    label: `${p.name} - Q${p.price}`,
    value: p
  }))
);

const { data: schedules, asyncStatus } = useCustomQuery({
  key: ['getAllSchedules'],
  query: () => getAllSchedules()
});

const scheduleOptions = computed(() =>
  (schedules.value ?? []).map((s: Schedule) => ({
    label: `${s.startTime} - ${s.endTime}`,
    value: s
  }))
);

const itemOptions = ref<{ id: string; name: string; price: number }[]>([])
const itemMap = ref<Record<string, { name: string; price: number }>>({})

const tempDetail = reactive({
  itemId: '',
  itemName: '',
  quantity: 1,
  unitPrice: 0
})

const extraDetails = ref<{ itemId: string; itemName: string; itemType: string; quantity: number; unitPrice: number }[]>([])

const addDetail = () => {
  const item = itemMap.value[tempDetail.itemId]
  if (!item || tempDetail.quantity < 1) {
    toast.warning('Seleccione un ítem y una cantidad válida')
    return
  }

  //agregamos la seleccion al array
  extraDetails.value.push({
    itemId: tempDetail.itemId,
    itemName: itemMap.value[tempDetail.itemId]?.name ?? '—',
    itemType: "GOOD",
    quantity: tempDetail.quantity,
    unitPrice: item.price
  })

  //reiniciamos valores
  tempDetail.itemId = ''
  tempDetail.quantity = 1
  tempDetail.unitPrice = 0
}

const addDetailPackage = () => {
  if (!selectedPackage) {
    toast.warning('Seleccione un paquete')
    return
  }

  //agregamos la seleccion al array
  extraDetails.value.push({
    itemId: selectedPackage.value!.id,
    itemName: selectedPackage.value!.name,
    itemType: "SERVICE",
    quantity: 1,
    unitPrice: selectedPackage.value!.price
  })

  //reiniciamos valores
  selectedPackage.value = null
}

const updatePrice = () => {
  const item = itemMap.value[tempDetail.itemId]
  tempDetail.unitPrice = item?.price ?? 0
}

//metodos de pago
const selectedPaymentMethod = ref<string>('')

const paymentOptions = ref<PaymentMethod[]>([])


//calculadora
const totalExtras = computed(() =>
  extraDetails.value.reduce((sum, d) => sum + d.unitPrice * d.quantity, 0)
)

const totalInvoice = computed(() =>
  (selectedPackage.value?.price ?? 0) + totalExtras.value
)

watch(selectedPackage, (newPackage) => {
  form.selectedPackageId = newPackage?.id ?? '';
});

const onFormSubmit = async () => {
  const payload: PayReservationPayload = {
    reservationId: reservation.value!.id,
    createInvoiceRequestDTO: {
      paymentMethod: selectedPaymentMethod.value,
      clientDocument: reservation.value!.customerNIT,
      details: extraDetails.value
    }
  };

  try {
    console.log("PAYLOAD")
    console.log(payload)
    console.log(extraDetails.value)
    const response = await payReservation(payload);

    const blob = new Blob([response as any], { type: "application/pdf" });
    const url = URL.createObjectURL(blob);
    window.open(url, "_blank");

    toast.success("Reservación creada correctamente");
    navigateTo("/reservaciones/");
  } catch (error: any) {
    toast.error("Error al crear la reservación", {
      description: error.message,
    });
  }
};

onMounted(async () => {
  try {
    reservation.value = await getReservationById(route.params.id as string)
  } catch (error: any) {
    toast.error('No se pudo cargar la reservación', { description: error.message })
  }

  const products = await getAllProducts({
    id: null,
    name: null,
    code: null,
    barCode: null,
    type: null,
    state: null
  })


  itemOptions.value = products.map(p => ({
    id: p.id,
    name: p.name,
    price: Number(p.price)
  }))
  itemMap.value = Object.fromEntries(itemOptions.value.map(p => [p.id, p]))

  const methods = await getPaymentMethods()
  paymentOptions.value = methods
})

</script>
