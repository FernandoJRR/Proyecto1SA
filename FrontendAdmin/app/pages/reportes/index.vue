<template>
    <!-- Filtros principales -->
    <div class="p-4 rounded-2xl bg-white shadow mb-5 border border-slate-200">
        <div class="grid grid-cols-3 gap-4 mb-4">
            <Dropdown v-model="reportType" :options="availableReports" optionLabel="label" optionValue="value"
                placeholder="Seleccionar reporte" class="w-full" />


            <!-- fechas-->
            <template class="flex flex-row gap-5">
                <DatePicker v-model="startDate" placeholder="Fecha de inicio" dateFormat="dd/mm/yy" class="w-full" />
                <DatePicker v-model="endDate" placeholder="Fecha de fin" dateFormat="dd/mm/yy" class="w-full" />
            </template>
        </div>


        <!-- botones -->
        <div class="flex flex-wrap gap-3 mt-4">
            <Button icon="pi pi-file-pdf" label="Exportar PDF" @click="exportReports" rounded outlined />
            <Button icon="pi pi-search" label="Filtrar" @click="filtrar" rounded outlined severity="info" />
            <Button icon="pi pi-refresh" label="Quitar Filtros" @click="recargarDatos" rounded outlined
                severity="help" />
        </div>
    </div>

    <template v-if="(reportType === 'RESERVATIONS_PROFIT') && globalFinancialSummary">
        <div class="p-4 mb-4 rounded-xl border border-slate-300 bg-slate-50 shadow-sm">
            <h3 class="text-slate-800 text-lg font-semibold mb-3">Resumen financiero global</h3>
            <div class="grid grid-cols-3 gap-4 text-sm text-slate-700">
                <div class="p-3 rounded-lg bg-white border border-slate-200 shadow-sm">
                    <span class="font-medium">Total de ingresos:</span>
                    <div class="text-green-700 font-semibold"> {{
                        globalFinancialSummary.totalSales ? "Q " + globalFinancialSummary.totalSales : "-" }}</div>
                </div>
            </div>
        </div>
    </template>


    <div class="text-slate-700 font-semibold mb-3" v-if="totalReservations !== null">
        Total de reservaciones: {{ totalReservations }}
    </div>

    <div class="text-slate-700 font-semibold mb-3" v-if="totalAverange !== null">
        Promedio General De Los Grupos: {{ totalAverange }}
    </div>


    <!-- tabla de resultados -->
    <DataTable :value="reportData.data" v-model:expandedRows="expandedRows" rowExpansionMode="single"
        :dataKey="tableConfig.dataKey" v-if="tableConfig" class="shadow-md border border-slate-200 rounded-xl ">
        <template #header>
            <div class=" px-4 py-2">
                <span class="text-lg font-bold text-slate-800">
                    {{ tableConfig.reportHeader }}
                </span>
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
import { ref, onMounted, render } from 'vue'
import { toast } from 'vue-sonner';
import { boolean } from 'zod';
import { getReservationReport, exportReservationReport, getPopularHoursBetweenDates, exportPopularHoursBetweenDates, exportReservationProfitReport, createReservationProfitReport, exportNotShowReport, getAveregangeTimeReport, exportAveregangeTimeReport, exportFrequentCustomersReport, createFrequentCustomersReport } from '~/lib/api/reportes/reporte';


//esto indica que las rows seran reactivas, se podran modificar, y por lo tanto se puede cambiar el valor y vue lo detectara
const expandedRows = ref({});
//esto indica que el tipo de reporte sera reactivo, y por lo tanto se puede cambiar el valor y vue lo detectara
const reportType = ref('RESERVATIONS');

//inputs que siven para el filtrado de los reportes
const startDate = ref<Date | null>(null)
const endDate = ref<Date | null>(null)


//este ref va a guardar los datos finanicieros totales de todos los reportes que lo poseean
//va a reaccionar con lo que mande el back entonces los nombres deben coincidir
const globalFinancialSummary = ref<{
    totalSales: number,
    totalProfit: number,
    totalCost: number
} | null>(null);





/**
 * Ref que contiene la data del reporte, es reactivo y se va a modificar dependiendo del tipo de reporte seleccionado.
 * Por defecto la data es un array vacio.
 */
const reportData = ref<{
    data: Array<any>
}>({
    data: [
    ],
});

/**
 * Array que contiene los tipos de reportes disponibles, cada uno tiene un valor y una etiqueta 
 * el valor es el que se va a usar para filtrar la data y la etiqueta es la que se va a mostrar en el dropdown
 */
const availableReports = [
    { value: 'RESERVATIONS', label: 'Reporte de Reservaciones' },
    { value: 'POPULAR_HOURS', label: 'Reporte de Horarios con Mayor Demanda' },
    { value: 'RESERVATIONS_PROFIT', label: 'Reporte de Ingresos Por Reservaciones' },
    { value: 'NOT_SHOW', label: 'Reporte de No Shows' },
    { value: 'AVERANGE_TIME', label: 'Reporte de Tiempo Promedio por Grupo' },
    { value: 'FREQUENT', label: 'Reporte de Jugadores Frecuentes' },
]

const totalReservations = ref<number | null>(null);
const totalAverange = ref<number | null>(null);



const frequentTable =
{
    dataKey: 'customerId',
    reportHeader: 'Reporte de Tiempo Promedio por Grupo',
    columns: [
        { field: 'customerId', header: 'NIT' },
        { field: 'customerFullName', header: 'Cliente' },
        { field: 'totalVisits', header: 'Numero de visitas' }
    ]
}


const averangeTableCOnfig =
{
    dataKey: 'date',
    reportHeader: 'Reporte de Tiempo Promedio por Grupo',
    columns: [
        { field: 'date', header: 'Fecha' },
        {
            field: 'averageHours', header: 'Tiempo Promedio Por Grupo',
            render: (row: any) => row.averageHours ? row.averageHours + 'h' : '-'
        }
    ]
}



const reservationsTableCOnfig =
{
    dataKey: 'reservationId',
    reportHeader: 'Reporte de Reservas No Shows',
    columns: [
        { field: 'id', header: 'Id' },
        { field: 'date', header: 'Fecha' },
        { field: 'startTime', header: 'Hora inicio' },
        { field: 'endTime', header: 'Hora final' },
        { field: 'customerFullName', header: 'Cliente' },
        { field: 'customerNIT', header: 'NIT' },
        {
            field: 'notShow', header: 'Asistencia',
            render: (row: any) => row.notShow ? 'No se presentó' : 'Asistió'
        }
    ]
}

const reservationProfitTableConfig =
{
    dataKey: 'reservationId',
    reportHeader: 'Reporte de Ingresos Por Reservaciones',
    columns: [
        { field: 'reservationId', header: 'Id' },
        { field: 'date', header: 'Fecha' },
        { field: 'startTime', header: 'Hora inicio' },
        { field: 'endTime', header: 'Hora final' },
        { field: 'customerFullName', header: 'Cliente' },
        { field: 'customerNIT', header: 'NIT' },
        { field: 'paymentMethod', header: 'Metodo de pago' },
        {
            field: 'total', header: 'Total de la venta',
            render: (row: any) => row.total ? 'Q ' + row.total : '-'
        }
    ]
}

const popularHoursTableConfig =
{
    dataKey: 'startTime',
    reportHeader: 'Reporte de Horarios con Mayor Demanda ',
    columns: [

        { field: 'startTime', header: 'Hora inicio' },
        { field: 'endTime', header: 'Hora final' },
        { field: 'total', header: 'Total reservaciones' },
    ]
}


const notShowTableConfig =
{
    dataKey: 'reservationId',
    reportHeader: 'Reporte de Reservas No Shows',
    columns: [
        { field: 'reservationId', header: 'Id' },
        { field: 'date', header: 'Fecha' },
        { field: 'startTime', header: 'Hora inicio' },
        { field: 'endTime', header: 'Hora final' },
        { field: 'customerFullName', header: 'Cliente' },
        { field: 'customerNIT', header: 'NIT' },
        { field: 'notShow', header: 'No Se presento?' }
    ]
}


/**
 * Ref que define la configuracion de la tabla, es reactivo y se va a modificar dependiendo del tipo de reporte seleccionado
 */
const tableConfig = ref<{
    dataKey: string,
    reportHeader: string;//esto va indicar el header del listado general
    columns: {
        field: string;
        header: string;
        render?: (row: any) => string | VNode;
    }[];
}>(reservationsTableCOnfig);

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
        //cargamos el reporte actual dependiendo del tipo de reporte seleccionado
        switch (reportType.value) {
            case 'RESERVATIONS':
                tableConfig.value = reservationsTableCOnfig;
                const reservationReport = await getReservationReport(
                    startDate.value,
                    endDate.value
                );
                reportData.value.data = reservationReport.reservations;
                totalReservations.value = reservationReport.totalReservations;
                break;

            case 'POPULAR_HOURS':
                tableConfig.value = popularHoursTableConfig;
                const popularHours = await getPopularHoursBetweenDates(
                    startDate.value,
                    endDate.value
                );
                reportData.value.data = popularHours.popularHours;
                totalReservations.value = popularHours.totalReservations;
                break;
            case 'RESERVATIONS_PROFIT':
                tableConfig.value = reservationProfitTableConfig;
                const reservationsProfit = await createReservationProfitReport(
                    startDate.value,
                    endDate.value
                );
                reportData.value.data = reservationsProfit.reservations;
                globalFinancialSummary.value = {
                    totalSales: reservationsProfit.totalProfit,
                    totalProfit: 0,
                    totalCost: 0,
                }
                break;

            case 'NOT_SHOW':
                tableConfig.value = notShowTableConfig;
                const notShow = await createReservationProfitReport(
                    startDate.value,
                    endDate.value
                );
                reportData.value.data = notShow.popularHours;
                totalReservations.value = notShow.totalReservations;
                break;

            case 'AVERANGE_TIME':
                tableConfig.value = averangeTableCOnfig;
                const averange = await getAveregangeTimeReport(
                    startDate.value,
                    endDate.value
                );
                reportData.value.data = averange.averangePerDates;
                totalAverange.value = averange.globalHoursAverange;
                break;

            case 'FREQUENT':
                tableConfig.value = frequentTable;
                const frequent = await createFrequentCustomersReport(
                    startDate.value,
                    endDate.value
                );
                reportData.value.data = frequent;
                break;
            default:
                reportData.value.data = []
        }
    } catch (error: any) {
        toast.error('Error', { description: `${(error.message)}` })
    }

}


const exportReports = async () => {
    try {
        switch (reportType.value) {
            case 'RESERVATIONS':
                await exportReservationReport(
                    startDate.value,
                    endDate.value
                );
                break;
            case 'POPULAR_HOURS':
                await exportPopularHoursBetweenDates(
                    startDate.value,
                    endDate.value
                );
                break;

            case 'RESERVATIONS_PROFIT':
                await exportReservationProfitReport(
                    startDate.value,
                    endDate.value
                );
                break;
            case 'NOT_SHOW':
                await exportNotShowReport(
                    startDate.value,
                    endDate.value
                );
                break;

            case 'AVERANGE_TIME':
                await exportAveregangeTimeReport(
                    startDate.value,
                    endDate.value
                );
                break;

            case 'FREQUENT':
                await exportFrequentCustomersReport(
                    startDate.value,
                    endDate.value
                );
                break;

            default:
        }
    } catch (error: any) {
        toast.error('Error', { description: `${(error.message)}` })
    }
}

watch(reportType, async () => {
    totalReservations.value = null;
    totalAverange.value = null;
    await cargarReporteActual();
});

const recargarDatos = async () => {
    //elimina cualquier filtro y manda a cargar el reporte seleccionado de nuevo
    startDate.value = null;
    endDate.value = null;
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