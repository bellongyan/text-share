<script setup lang="ts">
import { useClipboard } from '@/composables/useClipboard'
import { useI18n } from '@/composables/useI18n'

const props = defineProps<{
  content: string
  device?: string
  createdAt?: string
}>()

const { copied, copyToClipboard } = useClipboard()
const { t } = useI18n()

const handleCopyAll = async () => {
  await copyToClipboard(props.content)
}
</script>

<template>
  <div class="animate-fade-in">
    <!-- Header info -->
    <div class="flex items-center gap-3 mb-4 p-4 bg-amber-50 dark:bg-amber-900/20 rounded-xl">
      <div class="w-10 h-10 flex items-center justify-center bg-amber-500 rounded-full">
        <svg class="w-5 h-5 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M8.684 13.342C8.886 12.938 9 12.482 9 12c0-.482-.114-.938-.316-1.342m0 2.684a3 3 0 110-2.684m0 2.684l6.632 3.316m-6.632-6l6.632-3.316m0 0a3 3 0 105.367-2.684 3 3 0 00-5.367 2.684zm0 9.316a3 3 0 105.368 2.684 3 3 0 00-5.368-2.684z" />
        </svg>
      </div>
      <div>
        <p class="font-medium text-[var(--text-primary)]">
          {{ t('receive.sharedFrom', { device: device || t('receive.unknownDevice') }) }}
        </p>
        <p v-if="createdAt" class="text-sm text-[var(--text-secondary)]">
          {{ createdAt }}
        </p>
      </div>
    </div>

    <!-- Content display -->
    <div class="bg-[var(--bg-card)] dark:bg-[var(--bg-card)] rounded-xl p-4 mb-4">
      <pre
        class="w-full whitespace-pre-wrap break-words text-[var(--text-primary)] dark:text-[var(--text-primary)]
               font-mono text-sm leading-relaxed"
      >{{ content }}</pre>
    </div>

    <!-- Copy button -->
    <button
      @click="handleCopyAll"
      class="w-full py-3.5 px-6 flex items-center justify-center gap-2
             bg-amber-500 hover:bg-amber-600
             text-white font-semibold text-base
             rounded-xl shadow-md
             transform transition-all duration-150
             hover:scale-[1.02] active:scale-[0.98]"
    >
      <svg v-if="!copied" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
        <path stroke-linecap="round" stroke-linejoin="round" d="M8 5H6a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2v-1M8 5a2 2 0 002 2h2a2 2 0 002-2M8 5a2 2 0 012-2h2a2 2 0 012 2m0 0h2a2 2 0 012 2v3m2 4H10m0 0l3-3m-3 3l3 3" />
      </svg>
      <svg v-else class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
        <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7" />
      </svg>
      <span>{{ copied ? t('receive.copied') : t('receive.copyAll') }}</span>
    </button>
  </div>
</template>