<template>
  <div class="p-8 max-w-4xl mx-auto">
    <div class="mb-6">
      <router-link to="/facturacion/">
        <Button label="Ver Todas" icon="pi pi-arrow-left" text />
      </router-link>
    </div>

    <h1 class="text-4xl font-bold mb-6">Crear factura</h1>

    <Form
      v-slot="$form"
      :initialValues="initialValues"
      :resolver="resolver"
      @submit="onFormSubmit"
      class="space-y-8 bg-white shadow-md rounded-2xl p-6 border border-gray-200"
    >
      <div class="space-y-6">
        <h2 class="text-2xl font-semibold">Datos de la factura</h2>

        <FloatLabel>
          <label for="clientDocument">Documento del cliente</label>
          <InputText
            id="clientDocument"
            name="clientDocument"
            type="text"
            class="w-full"
          />
        </FloatLabel>
        <Message
          v-if="$form.clientDocument?.invalid"
          severity="error"
          size="small"
          variant="simple"
        >
          {{ $form.clientDocument.error?.message }}
        </Message>

        <FloatLabel>
          <label for="paymentMethod">Método de pago</label>
          <Dropdown
            id="paymentMethod"
            name="paymentMethod"
            :options="paymentOptions"
            optionLabel="name"
            optionValue="paymentMethod"
            placeholder="Seleccionar método de pago"
            class="w-full"
          />
        </FloatLabel>
        <Message
          v-if="$form.paymentMethod?.invalid"
          severity="error"
          size="small"
          variant="simple"
        >
          {{ $form.paymentMethod.error?.message }}
        </Message>

        <h2 class="text-2xl font-semibold pt-4">Detalles</h2>
        <Dropdown
          v-model="selectedWarehouseId"
          :options="state.data"
          optionLabel="name"
          optionValue="id"
          placeholder="Seleccionar bodega"
          class="w-full"
        />
        <Message
          v-if="formTouched && !selectedWarehouseId"
          severity="error"
          size="small"
          variant="simple"
        >
          Debe seleccionar una bodega
        </Message>

        <p class="text-sm text-gray-500 mb-2">
          Agrega los ítems (productos o paquetes) que deseas incluir en la
          factura.
        </p>

        <div class="grid md:grid-cols-2 gap-4 items-end">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1"
              >Producto o Paquete</label
            >
            <Dropdown
              v-model="tempDetail.itemId"
              :options="groupedItemOptions"
              optionLabel="name"
              optionValue="id"
              optionGroupLabel="label"
              optionGroupChildren="items"
              placeholder="Seleccionar ítem"
              class="w-full"
              @change="updateItemType"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1"
              >Cantidad</label
            >
            <InputNumber
              v-model="tempDetail.quantity"
              class="w-full"
              :min="1"
              :disabled="tempDetail.itemType === InvoiceItemType.SERVICE"
            />
          </div>
          <div class="md:col-span-2 text-right">
            <Button
              label="Agregar detalle"
              icon="pi pi-plus"
              outlined
              @click="addDetail"
            />
          </div>
        </div>

        <DataTable
          :value="form.details"
          class="mt-6"
          tableStyle="min-width: 100%"
        >
          <Column header="Ítem">
            <template #body="{ data }">
              {{ itemMap[data.itemId]?.name ?? "—" }}
            </template>
          </Column>
          <Column header="Cantidad" field="quantity" />
          <Column header="Precio unitario">
            <template #body="{ data }">
              Q{{ data.unitPrice.toFixed(2) }}
            </template>
          </Column>
          <Column header="Total">
            <template #body="{ data }">
              Q{{ (data.unitPrice * data.quantity).toFixed(2) }}
            </template>
          </Column>
          <Column header="Acciones">
            <template #body="{ index }">
              <Button
                icon="pi pi-trash"
                text
                severity="danger"
                @click="form.details.splice(index, 1)"
              />
            </template>
          </Column>
        </DataTable>
      </div>

      <div class="text-right text-lg font-semibold mt-4">
        Total: Q{{
          form.details
            .reduce((sum, d) => sum + d.quantity * d.unitPrice, 0)
            .toFixed(2)
        }}
      </div>

      <div class="pt-4">
        <Button
          type="submit"
          label="Crear"
          icon="pi pi-save"
          severity="secondary"
        />
      </div>
    </Form>
  </div>
</template>

<script setup lang="ts">
import { zodResolver } from "@primevue/forms/resolvers/zod";
import { FloatLabel } from "primevue";
import { toast } from "vue-sonner";
import { z } from "zod";
import Dropdown from "primevue/dropdown";
import InputText from "primevue/inputtext";
import InputNumber from "primevue/inputnumber";
import {
  createInvoiceByWarehouseId,
  getPaymentMethods,
  mapModelCreateInvoiceToCreateInvoice,
} from "~/lib/api/invoices/invoice";
import { getAllProducts, getStates } from "~/lib/api/products/product";
import { getAllPackages } from "~/lib/api/package/package";
import type {
  ModelCreateInvoice,
  CreateInvoiceDetail,
  PaymentMethod,
  CreateInvoice,
} from "~/lib/api/invoices/invoice";
import { InvoiceItemType } from "~/lib/api/invoices/invoice";
import { getAllWarehouses } from "~/lib/api/warehouse/warehouse";
import type { SpecWarehouse } from "~/lib/api/warehouse/warehouse";

const paymentOptions = ref<PaymentMethod[]>([]);
const itemMap = ref<
  Record<string, { name: string; price: number; itemType: InvoiceItemType }>
>({});
const groupedItemOptions = ref<any[]>([]);

const selectedWarehouseId = ref<string | null>(null);
const formTouched = ref(false);

const tempDetail = reactive<CreateInvoiceDetail>({
  itemId: "",
  itemName: "",
  itemType: InvoiceItemType.GOOD,
  quantity: 1,
  unitPrice: 0,
});

const form = reactive<ModelCreateInvoice>({
  paymentMethod: "",
  clientDocument: "",
  details: [],
});

const specWarehouse = reactive<SpecWarehouse>({
  id: null,
  name: null,
  active: true,
  ubication: null,
});

const { state, asyncStatus, refetch } = useCustomQuery({
  key: ["getAllWarehouses", specWarehouse],
  query: () => getAllWarehouses(specWarehouse),
});

const initialValues = form;

onMounted(async () => {
  paymentOptions.value = await getPaymentMethods();
  const statesProducts = await getStates();
  const activeState = statesProducts.find((state) => state.name === "Activo");

  const products = await getAllProducts({
    id: null,
    name: null,
    code: null,
    barCode: null,
    type: null,
    state: activeState?.id || null,
  });
  const packages = (await getAllPackages()).filter((p) => p.active);

  const productItems = products.map((p) => ({
    id: p.id,
    name: p.name,
    price: Number(p.price),
    itemType: InvoiceItemType.GOOD,
  }));
  const packageItems = packages.map((p) => ({
    id: p.id,
    name: p.name,
    price: Number(p.price),
    itemType: InvoiceItemType.SERVICE,
  }));

  groupedItemOptions.value = [
    { label: "Productos", items: productItems },
    { label: "Paquetes", items: packageItems },
  ];

  itemMap.value = Object.fromEntries(
    [...productItems, ...packageItems].map((i) => [i.id, i])
  );
});

const resolver = zodResolver(
  z.object({
    paymentMethod: z.string().min(1, "Debe seleccionar un método de pago"),
    clientDocument: z.string().min(1, "Debe ingresar un documento"),
  })
);

const updateItemType = () => {
  const item = itemMap.value[tempDetail.itemId];
  if (item) {
    tempDetail.itemName = item.name;
    tempDetail.itemType = item.itemType;
    tempDetail.unitPrice = item.price;
    if (item.itemType === InvoiceItemType.SERVICE) {
      tempDetail.quantity = 1;
    }
  }
};

const addDetail = () => {
  if (!tempDetail.itemId || tempDetail.quantity < 1) {
    toast.warning("Seleccione un ítem y cantidad válida");
    return;
  }
  form.details.push({
    ...tempDetail,
    total: tempDetail.quantity * tempDetail.unitPrice,
    id: crypto.randomUUID(),
  });
  tempDetail.itemId = "";
  tempDetail.itemName = "";
  tempDetail.itemType = InvoiceItemType.GOOD;
  tempDetail.quantity = 1;
  tempDetail.unitPrice = 0;
};

interface SendInvoice {
  payload: CreateInvoice;
  warehouseId: string;
}

const onFormSubmit = (e: any) => {
  formTouched.value = true;

  if (e.valid && form.details.length > 0 && selectedWarehouseId.value) {
    const modelPayload = {
      ...e.values,
      details: [...form.details],
    };
    const payload: CreateInvoice =
      mapModelCreateInvoiceToCreateInvoice(modelPayload);
    const sendPayload: SendInvoice = {
      payload,
      warehouseId: selectedWarehouseId.value,
    };
    mutate(sendPayload);
  } else if (!selectedWarehouseId.value) {
    toast.error("Debe seleccionar una bodega");
  } else {
    toast.error(
      "Debe completar todos los campos y agregar al menos un detalle"
    );
  }
};

const { mutate } = useMutation({
  mutation: (sendPayload: SendInvoice) =>
    createInvoiceByWarehouseId(sendPayload.payload, sendPayload.warehouseId),
  onError(error) {
    toast.error("Error al crear la factura", { description: error.message });
  },
  onSuccess() {
    toast.success("Factura creada correctamente");
    navigateTo("/facturacion/");
  },
});
</script>
