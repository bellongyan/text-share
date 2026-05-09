<script setup lang="ts">
import { useI18n } from '@/composables/useI18n'

const { locale, setLocale } = useI18n()
</script>

<template>
  <div class="relative inline-flex items-center">
    <select
      :value="locale"
      @change="setLocale(($event.target as HTMLSelectElement).value as 'zh' | 'en')"
      class="appearance-none bg-[var(--bg-secondary)] dark:bg-[var(--bg-secondary)]
             hover:bg-[var(--bg-card)] dark:hover:bg-[var(--bg-card)]
             rounded-lg px-3 py-2 pr-8 text-sm
             text-[var(--text-primary)] dark:text-[var(--text-primary)]
             cursor-pointer transition-all duration-200
             focus:outline-none focus:ring-2 focus:ring-amber-400/50"
    >
      <option value="zh">中文</option>
      <option value="en">English</option>
    </select>
    <svg
      class="absolute right-2 w-4 h-4 pointer-events-none text-[var(--text-secondary)]"
      fill="none"
      viewBox="0 0 24 24"
      stroke="currentColor"
      stroke-width="2"
    >
      <path stroke-linecap="round" stroke-linejoin="round" d="M19 9l-7 7-7-7" />
    </svg>
  </div>
</template>
