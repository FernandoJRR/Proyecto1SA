<template>
  <div class="p-8 max-w-4xl mx-auto">
    <router-link to="/reservaciones/" class="mb-6 block">
      <Button label="Ver Todas" icon="pi pi-arrow-left" text />
    </router-link>

    <h1 class="text-4xl font-bold mb-6">Crear una reservación</h1>

    <Stepper v-model:value="stepIndex" class="p-10" linear>
      <StepList>
        <Step :value="1">Datos</Step>
        <Step :value="2">Agrega Items</Step>
        <Step :value="3">Facturacion</Step>
      </StepList>

      <StepPanels>
        <!-- Paso 1 -->
        <StepPanel :value="1" v-slot="{ activateCallback }">
          <div class="space-y-6">
            <FloatLabel>
              <label for="date">Fecha</label>
              <InputText id="date" v-model="form.date" type="date" class="w-full" />
            </FloatLabel>

            <FloatLabel>
              <label for="fullName">Nombre del cliente</label>
              <InputText id="fullName" v-model="form.customerFullName" type="text" class="w-full" />
            </FloatLabel>

            <FloatLabel>
              <label for="nit">NIT del cliente</label>
              <InputText id="nit" v-model="form.customerNIT" type="text" class="w-full" />
            </FloatLabel>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Horario</label>
              <Dropdown v-model="selectedSchedule" :options="scheduleOptions" optionLabel="label" optionValue="value"
                placeholder="Seleccionar horario" class="w-full" />
            </div>

            <div class="pt-4 flex justify-end">
              <Button label="Siguiente" icon="pi pi-arrow-right" @click="validateStep1(activateCallback)" />
            </div>
          </div>
        </StepPanel>

        <!-- Paso 2 -->
        <StepPanel :value="2" v-slot="{ activateCallback }">
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
              <Button label="Atrás" icon="pi pi-arrow-left" @click="activateCallback(1)" severity="secondary" />
              <Button label="Siguiente" icon="pi pi-arrow-right" @click="activateCallback(3)" />
            </div>
          </div>
        </StepPanel>



        <!-- Paso 4 -->
        <StepPanel :value="3" v-slot="{ activateCallback }">
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
              <Button label="Atrás" icon="pi pi-arrow-left" @click="activateCallback(2)" severity="secondary" />
              <Button label="Crear Reservación" icon="pi pi-save" @click="onFormSubmit" />
            </div>
          </div>
        </StepPanel>

      </StepPanels>
    </Stepper>
  </div>
</template>


<script setup lang="ts">
import Stepper from 'primevue/stepper';
import StepList from 'primevue/steplist';
import StepPanels from 'primevue/steppanels';
import Step from 'primevue/step';
import StepPanel from 'primevue/steppanel';

import { toast } from "vue-sonner";
import Dropdown from "primevue/dropdown";
import InputText from "primevue/inputtext";
import { createReservation } from "~/lib/api/reservation/reservation";
import {
  getAllSchedules,
  type Schedule,
} from "~/lib/api/schedules/schedule";
import type { CreateReservation } from "~/lib/api/reservation/reservation";
import { getAllPackages, type Package } from "~/lib/api/package/package";
import { getAllProducts } from '~/lib/api/products/product';
import { getPaymentMethods, type PaymentMethod } from '~/lib/api/invoices/invoice';


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


const validateStep1 = (next: Function) => {
  if (!form.date || !form.customerNIT || !form.customerFullName || !selectedSchedule.value) {
    toast.error("Debe completar todos los campos");
    return;
  }
  next(2); // avanzar al paso 2
};


const onFormSubmit = async () => {
  if (!form.date || !form.customerNIT || !form.customerFullName || !selectedSchedule.value) {
    toast.error("Debe completar todos los campos");
    return;
  }

  const { startTime, endTime } = selectedSchedule.value;

  const payload: CreateReservation = {
    ...form,
    startTime,
    endTime,
    createInvoiceRequestDTO: {
      paymentMethod: selectedPaymentMethod.value,
      clientDocument: form.customerNIT,
      //Primero viene el paquete en el detalle
      details: extraDetails.value
    }
  };

  try {
    console.log("PAYLOAD")
    console.log(payload)
    console.log(extraDetails.value)
    const response = await createReservation(payload);

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

/**pAra el paso 4 */


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


onMounted(async () => {
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
