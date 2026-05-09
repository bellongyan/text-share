<script setup lang="ts">
import { useClipboard } from '@/composables/useClipboard'

const props = defineProps<{
  link: string
  viewCount?: number
}>()

const { copied, copyToClipboard } = useClipboard()

const handleCopy = async () => {
  await copyToClipboard(props.link)
}
</script>

<template>
  <div class="animate-fade-in" v-if="link">
    <div class="bg-[var(--bg-secondary)] dark:bg-[var(--bg-secondary)] rounded-xl p-4">
      <div class="flex items-center gap-3">
        <input
          type="text"
          :value="link"
          readonly
          class="flex-1 bg-transparent border-none text-[var(--text-primary)] dark:text-[var(--text-primary)] text-sm font-mono focus:outline-none select-all"
        />
        <button
          @click="handleCopy"
          class="flex items-center gap-2 px-4 py-2
                 bg-amber-500 hover:bg-amber-600
                 text-white text-sm font-medium
                 rounded-lg shadow-sm
                 transform transition-all duration-150
                 hover:scale-[1.02] active:scale-[0.98]"
          :title="copied ? '已复制到剪贴板' : '复制链接'"
        >
          <!-- Copy icon -->
          <svg v-if="!copied" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M8 5H6a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2v-1M8 5a2 2 0 002 2h2a2 2 0 002-2M8 5a2 2 0 012-2h2a2 2 0 012 2m0 0h2a2 2 0 012 2v3m2 4H10m0 0l3-3m-3 3l3 3" />
          </svg>
          <!-- Check icon -->
          <svg v-else class="w-4 h-4 text-emerald-300" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7" />
          </svg>
          <span>{{ copied ? '已复制' : '复制' }}</span>
        </button>
      </div>
    </div>

    <p class="mt-3 text-center text-sm text-[var(--text-secondary)]">
      链接有效期为 24 小时 | 已被查看 {{ viewCount || 0 }} 次
    </p>

    <p v-if="copied" class="mt-2 text-center text-sm text-emerald-500 font-medium animate-fade-in">
      已复制到剪贴板
    </p>
  </div>
</template>