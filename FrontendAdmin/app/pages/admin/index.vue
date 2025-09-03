<template>
    <div class="min-h-screen px-4 sm:px-6 lg:px-8 py-8 bg-slate-50">
        <!-- Page Header -->
        <header class="max-w-7xl mx-auto mb-8" role="banner">
            <div class="flex flex-col sm:flex-row sm:items-end sm:justify-between gap-4">
                <div>
                    <NuxtLink to="/" class="inline-block mb-2 sm:mb-0">
                        <Button size="small" icon="pi pi-arrow-left" label="Volver"
                            class="text-slate-700 hover:text-slate-900 mb-4" />
                    </NuxtLink>
                    <h1 class="text-2xl sm:text-3xl font-extrabold tracking-tight text-slate-900">Administración</h1>
                    <p class="text-slate-600">Configura catálogos, personal y opciones del sistema.</p>
                </div>
                <div class="w-full sm:w-auto">
                    <IconField class="w-full sm:w-80" iconPosition="left">
                        <InputIcon class="pi pi-search" />
                        <InputText v-model.trim="q" placeholder="Buscar opciones de administración…" class="w-full"
                            :pt="{ root: { class: 'pl-10' } }" />
                    </IconField>
                </div>
            </div>
        </header>

        <!-- Content -->
        <main class="max-w-7xl mx-auto" role="main">
            <!-- Meta: count and potential quick-actions -->
            <div class="flex items-center justify-between mb-4 text-sm text-slate-600">
                <div>
                    <span class="font-semibold">{{ filteredMenus.length }}</span>
                    {{ filteredMenus.length === 1 ? 'sección' : 'secciones' }} disponibles
                </div>
                <div class="hidden sm:flex items-center gap-2">
                    <!-- Placeholder para acciones rápidas futuras (e.g., crear usuario, importar catálogos) -->
                    <!-- <Button size="small" icon="pi pi-user-plus" label="Nuevo usuario" /> -->
                </div>
            </div>

            <!-- Responsive Grid (scales for many future cards) -->
            <div v-if="filteredMenus.length"
                class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
                <template v-for="menu in filteredMenus" :key="menu.route">
                    <MenuShortcutCard :menu="menu" />
                </template>
            </div>

            <!-- Empty state -->
            <div v-else class="flex flex-col items-center justify-center py-20 text-center">
                <i class="pi pi-inbox text-4xl mb-3 text-slate-400" aria-hidden="true"></i>
                <h2 class="text-lg font-semibold text-slate-800">No se encontraron secciones</h2>
                <p class="text-slate-600">Intenta con otro término o limpia el buscador.</p>
            </div>
        </main>
    </div>
</template>

<script setup lang="ts">
import MenuShortcutCard from '~/components/cards/MenuShortcutCard.vue'
import IconField from 'primevue/iconfield'
import InputIcon from 'primevue/inputicon'
import InputText from 'primevue/inputtext'

// Search
const q = ref('')

// Admin sections (feel free to add more later)
const menus = reactive([
    {
        title: 'Personal',
        description: 'Administración de Personal',
        route: '/admin/personal',
    },
    {
        title: 'Hoteles',
        description: 'Hoteles y Habitaciones del Sistema',
        route: '/admin/hoteles',
    },
    {
        title: 'Restaurantes',
        description: 'Restaurantes y Platillos del Sistema',
        route: '/admin/restaurantes',
    },
])

const filteredMenus = computed(() => {
    const term = q.value.toLowerCase()
    if (!term) return menus
    return menus.filter((m) =>
        m.title.toLowerCase().includes(term) ||
        m.description.toLowerCase().includes(term) ||
        m.route.toLowerCase().includes(term)
    )
})

// Expose for debugging if needed
defineExpose({ menus, filteredMenus })
</script>