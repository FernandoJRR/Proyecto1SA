<template>
  <div class="m-6 ml-12">
    <router-link to="/admin/facturas">
      <Button label="Ver Todas" icon="pi pi-arrow-left" text />
    </router-link>

    <div v-if="state.status === 'pending'" class="mt-6">
      Cargando...
    </div>

    <div v-else-if="state.status === 'error'" class="mt-6 text-red-600">
      Ocurrió un error al cargar la factura.
    </div>

    <div v-else class="grid grid-cols-2 mt-6 gap-8">
      <div class="flex flex-col gap-4">
        <h1 class="text-3xl font-bold">Detalle de Factura</h1>

        <div class="flex flex-col text-lg gap-2">
          <p>
            <span class="font-semibold">ID:</span> {{ state.data.id }}
          </p>
          <p>
            <span class="font-semibold">Documento Cliente:</span> {{ state.data.clientDocument }}
          </p>
          <p>
            <span class="font-semibold">Método de Pago:</span>
            <Tag :value="state.data.paymentMethod" :severity="methodSeverity(state.data.paymentMethod)" />
          </p>
          <p>
            <span class="font-semibold">Subtotal:</span> Q{{ state.data.subtotal.toFixed(2) }}
          </p>
          <p>
            <span class="font-semibold">Impuesto:</span> Q{{ state.data.tax.toFixed(2) }}
          </p>
          <p>
            <span class="font-semibold">Total:</span> Q{{ state.data.total.toFixed(2) }}
          </p>
        </div>
      </div>

      <div>
        <h2 class="text-2xl font-semibold mb-4">Detalle de Items</h2>
        <DataTable :value="state.data.details" dataKey="id" stripedRows tableStyle="min-width: 30rem">
          <Column header="Nombre">
            <template #body="{ data }">
              {{ data.itemName }}
            </template>
          </Column>
          <Column header="Tipo">
            <template #body="{ data }">
              <Tag :value="data.itemType" />
            </template>
          </Column>
          <Column header="Cantidad">
            <template #body="{ data }">
              {{ data.quantity }}
            </template>
          </Column>
          <Column header="Precio Unitario">
            <template #body="{ data }">
              Q{{ data.unitPrice.toFixed(2) }}
            </template>
          </Column>
          <Column header="Total">
            <template #body="{ data }">
              Q{{ data.total.toFixed(2) }}
            </template>
          </Column>
        </DataTable>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'
import { getInvoice } from '~/lib/api/invoices/invoice'
import type { InvoicePaymentMethod } from '~/lib/api/invoices/invoice'

const route = useRoute()

const { state } = useCustomQuery({
  key: ['factura', route.params.id as string],
  query: () => getInvoice(route.params.id as string),
})

function methodSeverity(method: InvoicePaymentMethod) {
  switch (method) {
    case 'CASH':
      return 'success'
    case 'CARD':
      return 'info'
    case 'ONLINE':
      return 'warning'
    default:
      return 'secondary'
  }
}
</script>
