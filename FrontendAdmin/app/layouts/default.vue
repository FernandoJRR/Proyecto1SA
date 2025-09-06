<template>
    <main class="min-h-screen bg-slate-50">
        <!-- Topbar with Menubar -->
        <header class="sticky top-0 z-40 w-full border-b border-slate-200 bg-white/90 backdrop-blur">
            <div class="mx-auto max-w-screen-2xl px-4 sm:px-6 lg:px-8">
                <Menubar :model="menuModel" class="border-0 bg-transparent">
                    <template #start>
                        <div class="flex items-center gap-3 py-3">
                            <Button icon="pi pi-bars" text class="lg:hidden" aria-label="Abrir menú"
                                @click="mobileOpen = true" />
                            <RouterLink to="/" class="flex items-center">
                                <span
                                    class="inline-flex h-8 w-8 items-center justify-center rounded-lg bg-slate-900 text-white font-bold">CD</span>
                                <span class="text-slate-900 font-extrabold tracking-tight">Comer y Dormir · Admin</span>
                            </RouterLink>
                        </div>
                    </template>
                    <template #end>
                        <div class="hidden lg:flex items-center">
                            <RouterLink to="/perfil">
                                <Button severity="secondary" variant="text"><i class="pi pi-user" /> Ver Perfil</Button>
                            </RouterLink>
                            <Button variant="text" @click="logout"><i class="pi pi-sign-out" /> Cerrar Sesión</Button>
                        </div>
                    </template>
                </Menubar>
            </div>
        </header>

        <!-- Content: expanded width, pages handle their own cards -->
        <div class="mx-auto max-w-screen-2xl px-4 sm:px-6 lg:px-8 py-6 lg:py-8">
            <!-- Breadcrumbs -->
            <div class="mb-4">
                <Breadcrumb :model="breadcrumbs" :home="homeCrumb" aria-label="Ruta actual" />
            </div>
            <!-- Page Outlet (no forced card wrapper) -->
            <NuxtPage />
        </div>

        <!-- Mobile Sidebar (only for mobile, not duplicated on desktop) -->
        <Sidebar v-model:visible="mobileOpen" class="lg:hidden" position="left" :modal="true">
            <template #header>
                <div class="flex items-center gap-2">
                    <span
                        class="inline-flex h-8 w-8 items-center justify-center rounded-lg bg-slate-900 text-white font-bold">CD</span>
                    <span class="text-slate-900 font-bold">Comer y Dormir · Admin</span>
                </div>
            </template>

            <nav aria-label="Principal (móvil)">
                <ul class="space-y-1">
                    <li>
                        <RouterLink to="/" @click="mobileOpen = false"
                            class="flex items-center gap-3 rounded-lg px-3 py-2 text-slate-700 hover:bg-slate-50 hover:text-slate-900">
                            <i class="pi pi-home" />
                            <span>Inicio</span>
                        </RouterLink>
                    </li>

                    <li class="mt-1">
                        <p class="px-3 py-2 text-xs uppercase tracking-wide text-slate-500">Establecimientos</p>
                        <ul class="ml-3 space-y-1 border-l border-slate-200 pl-3">
                            <li>
                                <RouterLink to="/admin/hoteles" @click="mobileOpen = false"
                                    class="flex items-center gap-2 rounded-lg px-3 py-2 text-slate-700 hover:bg-slate-50 hover:text-slate-900">
                                    <i class="pi pi-building" /><span>Hoteles</span>
                                </RouterLink>
                            </li>
                            <li>
                                <RouterLink to="/admin/restaurantes" @click="mobileOpen = false"
                                    class="flex items-center gap-2 rounded-lg px-3 py-2 text-slate-700 hover:bg-slate-50 hover:text-slate-900">
                                    <i class="pi pi-verified" /><span>Restaurantes</span>
                                </RouterLink>
                            </li>
                        </ul>
                    </li>

                    <li class="mt-1">
                        <p class="px-3 py-2 text-xs uppercase tracking-wide text-slate-500">Operación</p>
                        <ul class="ml-3 space-y-1 border-l border-slate-200 pl-3">
                            <li>
                                <RouterLink to="/reservaciones" @click="mobileOpen = false"
                                    class="flex items-center gap-2 rounded-lg px-3 py-2 text-slate-700 hover:bg-slate-50 hover:text-slate-900">
                                    <i class="pi pi-calendar" /><span>Reservaciones</span>
                                </RouterLink>
                            </li>
                            <li>
                                <RouterLink to="/ordenes" @click="mobileOpen = false"
                                    class="flex items-center gap-2 rounded-lg px-3 py-2 text-slate-700 hover:bg-slate-50 hover:text-slate-900">
                                    <i class="pi pi-shopping-cart" /><span>Órdenes</span>
                                </RouterLink>
                            </li>
                            <li>
                                <RouterLink to="/facturacion" @click="mobileOpen = false"
                                    class="flex items-center gap-2 rounded-lg px-3 py-2 text-slate-700 hover:bg-slate-50 hover:text-slate-900">
                                    <i class="pi pi-wallet" /><span>Pagos</span>
                                </RouterLink>
                            </li>
                        </ul>
                    </li>

                    <li class="mt-1">
                        <p class="px-3 py-2 text-xs uppercase tracking-wide text-slate-500">Administración</p>
                        <ul class="ml-3 space-y-1 border-l border-slate-200 pl-3">
                            <li>
                                <RouterLink to="/admin/personal" @click="mobileOpen = false"
                                    class="flex items-center gap-2 rounded-lg px-3 py-2 text-slate-700 hover:bg-slate-50 hover:text-slate-900">
                                    <i class="pi pi-id-card" /><span>Empleados</span>
                                </RouterLink>
                            </li>
                            <li>
                                <RouterLink to="/clientes" @click="mobileOpen = false"
                                    class="flex items-center gap-2 rounded-lg px-3 py-2 text-slate-700 hover:bg-slate-50 hover:text-slate-900">
                                    <i class="pi pi-users" /><span>Clientes</span>
                                </RouterLink>
                            </li>
                            <li>
                                <RouterLink to="/promociones" @click="mobileOpen = false"
                                    class="flex items-center gap-2 rounded-lg px-3 py-2 text-slate-700 hover:bg-slate-50 hover:text-slate-900">
                                    <i class="pi pi-percentage" /><span>Promociones</span>
                                </RouterLink>
                            </li>
                            <li>
                                <RouterLink to="/reportes" @click="mobileOpen = false"
                                    class="flex items-center gap-2 rounded-lg px-3 py-2 text-slate-700 hover:bg-slate-50 hover:text-slate-900">
                                    <i class="pi pi-chart-bar" /><span>Reportes</span>
                                </RouterLink>
                            </li>
                        </ul>
                    </li>
                </ul>
            </nav>
        </Sidebar>
    </main>
</template>

<script setup lang="ts">
import Button from 'primevue/button'
import Sidebar from 'primevue/sidebar'
import Breadcrumb from 'primevue/breadcrumb'
import Menubar from 'primevue/menubar'

const authStore = useAuthStore()
const { logout } = authStore

const route = useRoute()
const mobileOpen = ref(false)

// Top Menubar model (supports nested menus)
const menuModel = [
    { label: 'Inicio', icon: 'pi pi-home', to: '/' },
    {
        label: 'Establecimientos', icon: 'pi pi-building',
        items: [
            { label: 'Hoteles', icon: 'pi pi-building', to: '/admin/hoteles' },
            { label: 'Restaurantes', icon: 'pi pi-verified', to: '/admin/restaurantes' },
        ]
    },
    {
        label: 'Operación', icon: 'pi pi-briefcase',
        items: [
            { label: 'Reservaciones', icon: 'pi pi-calendar', to: '/reservaciones' },
            { label: 'Órdenes', icon: 'pi pi-shopping-cart', to: '/ordenes' },
            { label: 'Pagos', icon: 'pi pi-wallet', to: '/facturacion' },
        ]
    },
    {
        label: 'Administración', icon: 'pi pi-cog',
        items: [
            { label: 'Empleados', icon: 'pi pi-id-card', to: '/admin/personal' },
            { label: 'Clientes', icon: 'pi pi-users', to: '/clientes' },
            { label: 'Promociones', icon: 'pi pi-percentage', to: '/promociones' },
            { label: 'Reportes', icon: 'pi pi-chart-bar', to: '/reportes' },
        ]
    },
]

// Breadcrumbs
const homeCrumb = { icon: 'pi pi-home', route: '/' }
const breadcrumbs = computed(() => {
    const items: any[] = []
    for (const rec of route.matched) {
        if (!rec.path || rec.path === '/') continue
        const label = (rec.meta as any)?.breadcrumb || rec.name || rec.path.split('/').filter(Boolean).slice(-1)[0]
        const to = rec.path.startsWith('/') ? rec.path : '/' + rec.path
        items.push({ label, route: to })
    }
    return items
})
</script>

<style scoped>
.p-menubar {
    padding: 0;
}

.p-sidebar {
    width: 18rem;
}
</style>