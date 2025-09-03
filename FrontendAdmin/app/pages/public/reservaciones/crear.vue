<template>
  <div class="p-8 max-w-4xl mx-auto">
    <h1 class="text-4xl font-bold mb-6">Crear una reservación en linea</h1>

    <Stepper v-model:value="stepIndex" class="p-10" linear>
      <StepList>
        <Step :value="1">Datos</Step>
        <Step :value="2">Ingresa a los Jugadores</Step>
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
            <div v-for="(name, index) in newPlayers" :key="index"
              class="border rounded-xl p-4 shadow-sm bg-white hover:shadow-md transition-shadow">
              <div class="flex items-center gap-3 w-full">
                <div
                  class="bg-green-100 text-main-green rounded-full h-10 w-10 flex items-center justify-center font-bold">
                  {{ index + 1 }}
                </div>
                <input v-model="newPlayers[index]" type="text" class="flex-1 px-3 py-2 rounded w-full"
                  :placeholder="`Nombre del Jugador ${index + 1}`" required />
              </div>
            </div>

            <div class="flex flex-row gap-x-2 mt-4">
              <Button class="w-full" label="Agregar" @click="addPlayer" :disabled="newPlayers.length >= 4"
                icon="pi pi-plus" />
              <Button class="w-full" label="Quitar" @click="removePlayer" :disabled="newPlayers.length <= 1"
                icon="pi pi-minus" />
            </div>

            <div class="pt-4 flex justify-between">
              <Button label="Atrás" icon="pi pi-arrow-left" @click="activateCallback(1)" severity="secondary" />
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
import { createReservation, createReservationOnline } from "~/lib/api/reservation/reservation";
import {
  getAllSchedules,
  type Schedule,
} from "~/lib/api/schedules/schedule";
import type { CreateReservation, CreateReservationOnline } from "~/lib/api/reservation/reservation";
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
  if (!canSubmitPlayers) {
    toast.error("Debe de ingresar jugadores validos");
    return;
  }
  if (!form.date || !form.customerNIT || !form.customerFullName || !selectedSchedule.value) {
    toast.error("Debe completar todos los campos");
    return;
  }

  const { startTime, endTime } = selectedSchedule.value;

  const registeredPlayers = newPlayers.value.map((name, i) => ({
    name,
    playerNumber: i + 1,
  }))
  const payload: CreateReservationOnline = {
    ...form,
    startTime,
    endTime,
    players: registeredPlayers
  };

  try {
    console.log("PAYLOAD")
    console.log(payload)
    console.log(extraDetails.value)
    const response = await createReservationOnline(payload);

    const blob = new Blob([response as any], { type: "application/pdf" });
    const url = URL.createObjectURL(blob);
    window.open(url, "_blank");

    toast.success("Reservación creada correctamente");
    navigateTo("/public/reservaciones/crear");
  } catch (error: any) {
    toast.error("Error al crear la reservación", {
      description: error.message,
    });
  }
};

const newPlayers = ref<string[]>([''])

const addPlayer = () => {
  if (newPlayers.value.length < 4) newPlayers.value.push('')
}

const removePlayer = () => {
  if (newPlayers.value.length > 1) newPlayers.value.pop()
}

const canSubmitPlayers = computed(() =>
  newPlayers.value.length >= 1 &&
  newPlayers.value.length <= 4 &&
  newPlayers.value.every(name => name.trim().length > 0)
)

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
