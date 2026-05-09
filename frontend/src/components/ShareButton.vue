<script setup lang="ts">
import { computed } from 'vue'
import { useI18n } from '@/composables/useI18n'

const props = defineProps<{
  loading?: boolean
  disabled?: boolean
}>()

const { t } = useI18n()

const isDisabled = computed(() => props.disabled || props.loading)
</script>

<template>
  <button
    :disabled="isDisabled"
    class="w-full py-3.5 px-6
           flex items-center justify-center gap-2
           bg-amber-500 hover:bg-amber-600
           disabled:bg-amber-300 disabled:cursor-not-allowed
           text-white font-semibold text-base
           rounded-xl shadow-md
           transform transition-all duration-150
           hover:scale-[1.02] active:scale-[0.98]
           disabled:hover:scale-100"
  >
    <!-- Loading spinner -->
    <svg
      v-if="loading"
      class="w-5 h-5 animate-spin"
      fill="none"
      viewBox="0 0 24 24"
    >
      <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
      <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
    </svg>

    <!-- Link icon -->
    <svg v-else class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
      <path stroke-linecap="round" stroke-linejoin="round" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
    </svg>

    <span>{{ loading ? t('send.submitting') : t('send.submit') }}</span>
  </button>
</template>