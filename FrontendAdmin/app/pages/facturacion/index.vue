<template>
  <div class="m-6">
    <DataTable
      :value="state.data as any[]"
      tableStyle="min-width: 50rem"
      stripedRows
      :loading="asyncStatus === 'loading'"
      filterDisplay="row"
    >
      <template #header>
        <div class="flex flex-wrap items-center justify-between gap-2">
          <span class="text-xl font-bold">Facturas</span>
          <router-link to="/facturacion/crear">
            <Button icon="pi pi-plus" rounded raised />
          </router-link>
        </div>
      </template>

      <Column header="Cliente" field="clientDocument">
        <template #filter>
          <InputText
            v-model="specInvoice.clientDocument"
            placeholder="Filtrar por cliente"
            class="w-full"
            @input="refetch()"
          />
        </template>
        <template #body="{ data }">
          {{ data.clientDocument ?? 'No definido' }}
        </template>
      </Column>

      <Column header="Forma de pago" field="paymentMethod">
        <template #filter>
          <Dropdown
            v-model="specInvoice.paymentMethod"
            :options="paymentMethodOptions"
            optionLabel="name"
            optionValue="paymentMethod"
            placeholder="Filtrar forma de pago"
            class="w-full"
            @change="refetch()"
          />
        </template>
        <template #body="{ data }">
          {{ data.paymentMethod }}
        </template>
      </Column>

      <Column header="Subtotal">
        <template #body="{ data }">
          Q{{ data.subtotal.toFixed(2) }}
        </template>
      </Column>

      <Column header="Impuesto">
        <template #body="{ data }">
          Q{{ data.tax.toFixed(2) }}
        </template>
      </Column>

      <Column header="Total">
        <template #body="{ data }">
          <strong>Q{{ data.total.toFixed(2) }}</strong>
        </template>
      </Column>

      <Column header="Acciones">
        <template #body="{ data }">
          <RouterLink :to="`/facturacion/${data.id}`">
            <Button label="Ver" icon="pi pi-eye" severity="info" text />
          </RouterLink>
        </template>
      </Column>

      <template #footer>
        Hay en total {{ state.data?.length || 0 }} facturas.
      </template>
    </DataTable>
  </div>
</template>

<script setup lang="ts">
import { getAllInvoices, getPaymentMethods, type Invoice, type SpecInvoice, type PaymentMethod } from '~/lib/api/invoices/invoice'

const specInvoice = reactive<SpecInvoice>({
  clientDocument: null,
  paymentMethod: null
})

const paymentMethodOptions = ref<PaymentMethod[]>([])

onMounted(async () => {
  paymentMethodOptions.value = await getPaymentMethods()
  paymentMethodOptions.value.unshift({
    name: 'Todos',
    paymentMethod: null
  })
})

const { state, asyncStatus, refetch } = useCustomQuery({
  key: ['getAllInvoices', specInvoice],
  query: () => getAllInvoices(specInvoice)
})
</script>
