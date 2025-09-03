<template>
  <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
    <!-- Page Header -->
    <header class="max-w-7xl mx-auto mb-8">
      <div class="flex flex-col sm:flex-row sm:items-end sm:justify-between gap-4">
        <div>
          <h1 class="text-2xl sm:text-3xl font-extrabold tracking-tight text-slate-900">Panel administrativo</h1>
          <p class="text-slate-600">Bienvenido, <span class="font-semibold">{{ user?.username }}</span>. Elige una sección para continuar.</p>
        </div>
        <div class="w-full sm:w-auto">
          <IconField class="w-full sm:w-80">
            <InputIcon class="pi pi-search" />
            <InputText v-model.trim="q" placeholder="Buscar módulos…" class="w-full" :pt="{ root: { class: 'pl-10' } }" />
          </IconField>
        </div>
      </div>
    </header>

    <!-- Content -->
    <main class="max-w-7xl mx-auto" role="main">
      <!-- Meta: count and quick-actions (optional) -->
      <div class="flex items-center justify-between mb-4 text-sm text-slate-600">
        <div>
          <span class="font-semibold">{{ filteredMenus.length }}</span>
          {{ filteredMenus.length === 1 ? 'módulo' : 'módulos' }} disponibles
        </div>
        <!-- Placeholder for future quick actions -->
        <div class="hidden sm:flex items-center gap-2">
          <!-- Example: add a quick report button later -->
          <!-- <Button label="Nuevo reporte" size="small" icon="pi pi-plus" /> -->
        </div>
      </div>

      <!-- Responsive Grid for many cards -->
      <div
        v-if="filteredMenus.length"
        class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-6 gap-4"
      >
        <template v-for="menu in filteredMenus" :key="menu.route">
          <MenuShortcutCard :menu="menu" />
        </template>
      </div>

      <!-- Empty state -->
      <div v-else class="flex flex-col items-center justify-center py-20 text-center">
        <i class="pi pi-inbox text-4xl mb-3 text-slate-400" aria-hidden="true"></i>
        <h2 class="text-lg font-semibold text-slate-800">No se encontraron módulos</h2>
        <p class="text-slate-600">Intenta con otro término de búsqueda.</p>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import MenuShortcutCard from "~/components/cards/MenuShortcutCard.vue";
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import InputText from 'primevue/inputtext';

const { user } = storeToRefs(useAuthStore());

// Search
const q = ref('');

// Base menus (can grow safely)
const menus = reactive([
  {
    title: "Administracion",
    description: "Administracion General",
    route: "/admin",
  },
  {
    title: "Reportes",
    description: "Generación de Repotes",
    route: "/reportes",
  },
  {
    title: "Reservaciones",
    description: "Administracion de Reservaciones",
    route: "/reservaciones",
  },
  {
    title: "Pagos",
    description: "Administracion de Facturas",
    route: "/facturacion",
  }
]);

const filteredMenus = computed(() => {
  const term = q.value.toLowerCase();
  if (!term) return menus;
  return menus.filter(m =>
    m.title.toLowerCase().includes(term) ||
    m.description.toLowerCase().includes(term) ||
    m.route.toLowerCase().includes(term)
  );
});

// Expose for debugging
defineExpose({ menus, filteredMenus });
</script>