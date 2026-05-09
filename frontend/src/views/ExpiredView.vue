<script setup lang="ts">
import { useRouter } from 'vue-router'
import ThemeIcon from '@/components/ThemeIcon.vue'
import LanguageSwitch from '@/components/LanguageSwitch.vue'
import { useTheme } from '@/composables/useTheme'
import { useI18n } from '@/composables/useI18n'

const { isDark, toggleTheme } = useTheme()
const { initLocale, t } = useI18n()
const router = useRouter()

initLocale()

const goHome = () => {
  router.push('/')
}
</script>

<template>
  <div class="min-h-screen bg-[var(--bg-primary)] dark:bg-[var(--bg-primary)] transition-colors duration-300">
    <div class="max-w-2xl mx-auto px-4 py-8 lg:py-12">
      <!-- Header -->
      <header class="flex items-center justify-between mb-8">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 bg-amber-500 rounded-xl flex items-center justify-center shadow-md">
            <svg class="w-6 h-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M8 5H6a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2v-1M8 5a2 2 0 002 2h2a2 2 0 002-2M8 5a2 2 0 012-2h2a2 2 0 012 2m0 0h2a2 2 0 012 2v3m2 4H10m0 0l3-3m-3 3l3 3" />
            </svg>
          </div>
          <h1 class="text-2xl font-bold text-[var(--text-primary)]">TextShare</h1>
        </div>
        <ThemeIcon :is-dark="isDark" @toggle="toggleTheme" />
        <LanguageSwitch />
      </header>

      <!-- Expired content -->
      <main class="flex flex-col items-center justify-center py-20 animate-fade-in">
        <div class="w-20 h-20 bg-amber-100 dark:bg-amber-900/30 rounded-full flex items-center justify-center mb-6">
          <svg class="w-10 h-10 text-amber-500" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </div>

        <h2 class="text-2xl font-bold text-[var(--text-primary)] mb-3">{{ t('expired.heading') }}</h2>

        <p class="text-[var(--text-secondary)] text-center mb-8 max-w-sm">
          {{ t('expired.description1') }}<br />
          {{ t('expired.description2') }}
        </p>

        <button
          @click="goHome"
          class="flex items-center gap-2 px-6 py-3
                 bg-amber-500 hover:bg-amber-600
                 text-white font-semibold
                 rounded-xl shadow-md
                 transform transition-all duration-150
                 hover:scale-[1.02] active:scale-[0.98]"
        >
          <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
          </svg>
          <span>{{ t('expired.backHome') }}</span>
        </button>
      </main>
    </div>
  </div>
</template>
