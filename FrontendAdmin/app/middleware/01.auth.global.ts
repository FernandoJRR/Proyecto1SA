import { storeToRefs } from 'pinia'
import { toast } from 'vue-sonner'
import { canAccessAdmin, canAccessReservaciones, canAccessOrdenes, canAccessReportes, getRoleNameFromEmployee } from "~/lib/auth/roles";

export default defineNuxtRouteMiddleware((to, _from) => {
  if (to.path === '/example') return

  if(to.fullPath.includes('public')) return
  if(to.fullPath.includes('juegos')) return

  const token = useCookie('proyecto1sa-user-token')

  if (!token.value && !to?.fullPath.includes('login')) {
    toast.error("Debes loguearte para acceder al sitio")
    return navigateTo('/login')
  }

  if (token.value && to.fullPath.includes('login')) {
    return navigateTo('/')
  }

  if (import.meta.client) {
    const auth = useAuthStore()
    const { employee } = storeToRefs(auth)
    const role = getRoleNameFromEmployee(employee.value)

    // Guard: only Admins can access routes under /admin
    if (to.path.startsWith('/admin') && !canAccessAdmin(role)) {
      toast.error('No tienes permisos para acceder a Administración')
      return navigateTo('/')
    }

    // Guard: only Staff Hotel (or Admin) can access /reservaciones
    if (to.path.startsWith('/reservaciones') && !canAccessReservaciones(role)) {
      toast.error('No tienes permisos para acceder a Reservaciones')
      return navigateTo('/')
    }

    // Guard: only Staff Restaurante (or Admin) can access /ordenes
    if (to.path.startsWith('/ordenes') && !canAccessOrdenes(role)) {
      toast.error('No tienes permisos para acceder a Ordenes')
      return navigateTo('/')
    }

    // Guard: only Contador (or Admin) can access /reportes
    if (to.path.startsWith('/reportes') && !canAccessReportes(role)) {
      toast.error('No tienes permisos para acceder a Reportes')
      return navigateTo('/')
    }
  }
})
