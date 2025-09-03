import { storeToRefs } from 'pinia'
import { toast } from 'vue-sonner'

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
})
