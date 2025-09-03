<template>
  <div class="m-6 overflow-x-auto">
    <div class="max-w-[1200px]">
      <DataTable :value="state.data as any[]" tableStyle="min-width: 50rem" stripedRows
        :loading="asyncStatus === 'loading'" filterDisplay="row">
        <template #header>
          <div class="flex flex-wrap items-center justify-between gap-2 w-full">
            <span class="text-xl font-bold">Reservaciones</span>
            <router-link to="/reservaciones/crear">
              <Button icon="pi pi-plus" label="Nueva Reservación" rounded raised />
            </router-link>
          </div>
        </template>

        <Column header="ID de Reservacion" field="id">
          <template #filter>
            <InputText v-model="spec.id" class="w-full" placeholder="Filtrar por usuario" @input="refetch()" />
          </template>
          <template #body="{ data }">{{ data.id ?? "error" }}</template>
        </Column>


        <Column header="ID del juego" field="gameId">
          <template #filter>
            <InputText v-model="spec.gameId" class="w-full" placeholder="Filtrar por usuario" @input="refetch()" />
          </template>
          <template #body="{ data }">{{ data.gameId ?? "error" }}</template>
        </Column>

        <Column header="Fecha" field="date">
          <template #filter>
            <InputText v-model="spec.date" class="w-full" placeholder="AAAA-MM-DD" @input="refetch()" />
          </template>
          <template #body="{ data }">{{ data.date }}</template>
        </Column>

        <Column header="Hora inicio" field="startTime">
          <template #filter>
            <InputText v-model="spec.startTime" class="w-full" placeholder="HH:MM" @input="refetch()" />
          </template>
          <template #body="{ data }">{{ data.startTime }}</template>
        </Column>

        <Column header="Hora fin" field="endTime">
          <template #filter>
            <InputText v-model="spec.endTime" class="w-full" placeholder="HH:MM" @input="refetch()" />
          </template>
          <template #body="{ data }">{{ data.endTime }}</template>
        </Column>

        <Column header="Usuario" field="customerFullname">
          <template #filter>
            <InputText v-model="spec.customerFullName" class="w-full" placeholder="Filtrar por usuario"
              @input="refetch()" />
          </template>
          <template #body="{ data }">{{ data.customerFullName ?? "error" }}</template>
        </Column>


        <Column header="Usuario NIT" field="customerNit">
          <template #filter>
            <InputText v-model="spec.customerNIT" class="w-full" placeholder="Filtrar por usuario" @input="refetch()" />
          </template>
          <template #body="{ data }">{{ data.customerNIT ?? "error" }}</template>
        </Column>

        <Column header="Pagado" field="paid">
          <template #filter>
            <Dropdown v-model="spec.paid" :options="booleanOptions" class="w-full" placeholder="Filtrar pago"
              optionLabel="label" optionValue="value" @change="refetch()" />
          </template>
          <template #body="{ data }">
            <Tag :severity="data.paid ? 'success' : 'warning'" :value="data.paid ? 'Sí' : 'No'" />
          </template>
        </Column>

        <Column header="Estado" field="notShow">
          <template #filter>
            <Dropdown v-model="spec.notShow" :options="booleanOptions" class="w-full" placeholder="Filtrar cancelación"
              optionLabel="label" optionValue="value" @change="refetch()" />
          </template>
          <template #body="{ data }">
            <Tag v-if="!data.notShow && data.paid" severity="info" value="Asistió" />
            <Tag v-else-if="!data.notShow && !data.paid" severity="warn" value="Programada" />
            <Tag v-else-if="data.notShow" severity="danger" value="Cancelada" />
          </template>
        </Column>

        <Column header="Acciones">
          <template #body="{ data }">
              <router-link :to="`/reservaciones/ver/${data.id}`">
                <Button icon="pi pi-search" text rounded label="Ver" />
              </router-link>
              <Button v-if="!data.notShow" label="Cancelar" icon="pi pi-times" severity="danger" text rounded
                :disabled="data.cancelled" @click="cancel(data.id)" />
          </template>
        </Column>

        <template #footer>
          Hay en total {{ state.data?.length || 0 }} reservaciones.
        </template>
      </DataTable>
    </div>
    <ConfirmDialog />
  </div>
</template>

<script setup lang="ts">
import { toast } from "vue-sonner";
import { useConfirm } from "primevue/useconfirm";
import ConfirmDialog from "primevue/confirmdialog";
import {
  getAllReservations,
  cancelReservation,
  payReservation,
  type Reservation,
  type SpecReservation,
} from "~/lib/api/reservation/reservation";

const queryCache = useQueryCache();
const confirm = useConfirm();

const spec = reactive<SpecReservation>({

  id: null,
  userId: null,
  gameId: null,
  startTime: null,
  endTime: null,
  customerFullName: null,
  customerNIT: null,
  date: null,
  notShow: null,
  paid: null,
});

const booleanOptionsModalidad = [
  { label: "Todos", value: null },
  { label: "Virtual", value: true },
  { label: "Presencial", value: false },
];

const booleanOptions = [
  { label: "Todos", value: null },
  { label: "Sí", value: true },
  { label: "No", value: false },
];

const { state, asyncStatus, refetch } = useCustomQuery({
  key: ["getAllReservations", spec],
  query: () => getAllReservations(spec),
});

const cancel = (id: string) => {
  confirm.require({
    message: "¿Estás seguro de cancelar esta reservación?",
    header: "Confirmación",
    icon: "pi pi-exclamation-triangle",
    accept: () => mutateCancel(id),
    reject: () => toast.warning("Acción cancelada"),
  });
};

const { mutate: mutateCancel } = useMutation({
  mutation: cancelReservation,
  onSuccess() {
    toast.success("Reservación cancelada correctamente");
    queryCache.invalidateQueries({ key: ["getAllReservations"] });
  },
  onError(error) {
    toast.error("Error al cancelar la reservación", {
      description: error.message,
    });
  },
});

const { mutate: mutatePay } = useMutation({
  mutation: payReservation,
  onSuccess() {
    toast.success("Reservación marcada como pagada");
    queryCache.invalidateQueries({ key: ["getAllReservations"] });
  },
  onError(error) {
    toast.error("Error al marcar como pagada", { description: error.message });
  },
});
</script>
