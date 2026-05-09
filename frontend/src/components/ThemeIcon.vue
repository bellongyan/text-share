<script setup lang="ts">
import { useI18n } from '@/composables/useI18n'

defineProps<{
  isDark: boolean
}>()

const emit = defineEmits<{
  toggle: []
}>()

const { t } = useI18n()
</script>

<template>
  <button
    @click="emit('toggle')"
    class="relative inline-flex items-center justify-center w-10 h-10
           bg-[var(--bg-secondary)] dark:bg-[var(--bg-secondary)]
           hover:bg-[var(--bg-card)] dark:hover:bg-[var(--bg-card)]
           rounded-xl shadow-sm
           transition-all duration-200
           hover:scale-105 active:scale-95
           focus:outline-none focus:ring-2 focus:ring-amber-400/50"
    :title="isDark ? t('theme.toggleLight') : t('theme.toggleDark')"
  >
    <!-- Sun icon for light mode -->
    <svg
      v-if="!isDark"
      class="w-5 h-5 text-amber-500"
      fill="none"
      viewBox="0 0 24 24"
      stroke="currentColor"
      stroke-width="2"
    >
      <path
        stroke-linecap="round"
        stroke-linejoin="round"
        d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z"
      />
    </svg>
    <!-- Moon icon for dark mode -->
    <svg
      v-else
      class="w-5 h-5 text-amber-400"
      fill="none"
      viewBox="0 0 24 24"
      stroke="currentColor"
      stroke-width="2"
    >
      <path
        stroke-linecap="round"
        stroke-linejoin="round"
        d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z"
      />
    </svg>
  </button>
</template>