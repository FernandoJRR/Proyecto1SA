// https://nuxt.com/docs/api/configuration/nuxt-config
import tailwindcss from "@tailwindcss/vite"
import Aura from "@primeuix/themes/material"
import { definePreset } from "@primeuix/themes"
// Switch to Aura and change the primary from green to Indigo (better contrast in light UIs)
const MyPreset = definePreset(Aura, {
  semantic: {
    primary: {
      50: '{indigo.50}',
      100: '{indigo.100}',
      200: '{indigo.200}',
      300: '{indigo.300}',
      400: '{indigo.400}',
      500: '{indigo.500}',
      600: '{indigo.600}',
      700: '{indigo.700}',
      800: '{indigo.800}',
      900: '{indigo.900}',
      950: '{indigo.950}'
    }
  }
})
export default defineNuxtConfig({
  compatibilityDate: '2024-11-01',
  devtools: { enabled: true },
  srcDir: 'app/',
  css: ['~/assets/css/main.css'],
  modules: ['@primevue/nuxt-module', '@pinia/nuxt', '@pinia/colada-nuxt', 'pinia-plugin-persistedstate/nuxt', '@nuxt/test-utils/module'],
  app: {
    baseURL: '/app/',
    buildAssetsDir: '/app/_nuxt/',
  },
  primevue: {
    options: {
      theme: {
        preset: MyPreset,
        cssLayer: {
          name: 'primevue',
          order: 'tailwind-base, primevue, tailwind-utilities'
        },
        options: {
          darkModeSelector: '.class-dark'
        }
      }
    }
  },
  vite: {
    plugins: [
      tailwindcss()
    ]
  }
})
