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
                                <span class="text-slate-900 font-extrabold tracking-tight">Comer y Dormir</span>
                            </RouterLink>
                        </div>
                    </template>
                </Menubar>
            </div>
        </header>

        <!-- Content: expanded width, pages handle their own cards -->
        <div class="mx-auto max-w-screen-2xl px-4 sm:px-6 lg:px-8 py-6 lg:py-8">
            <!-- Page Outlet (no forced card wrapper) -->
            <NuxtPage />
        </div>

        <!-- Mobile Sidebar (only for mobile, not duplicated on desktop) -->
        <Sidebar v-model:visible="mobileOpen" class="lg:hidden" position="left" :modal="true">
            <template #header>
                <div class="flex items-center gap-2">
                    <span
                        class="inline-flex h-8 w-8 items-center justify-center rounded-lg bg-slate-900 text-white font-bold">CD</span>
                    <span class="text-slate-900 font-bold">Comer y Dormir</span>
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
                        <p class="px-3 py-2 text-xs uppercase tracking-wide text-slate-500">Explorar</p>
                        <ul class="ml-3 space-y-1 border-l border-slate-200 pl-3">
                            <li>
                                <RouterLink to="/hoteles" @click="mobileOpen = false"
                                    class="flex items-center gap-2 rounded-lg px-3 py-2 text-slate-700 hover:bg-slate-50 hover:text-slate-900">
                                    <i class="pi pi-building" /><span>Hoteles</span>
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
const router = useRouter()
const mobileOpen = ref(false)

// Top Menubar model (supports nested menus)
const menuModel = [
    { label: 'Inicio', icon: 'pi pi-home', command: () => router.push('/') },
    { label: 'Hoteles', icon: 'pi pi-building', command: () => router.push('/hoteles') },
    { label: 'Mis reservaciones', icon: 'pi pi-calendar', command: () => router.push('/mis-reservaciones') },
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
