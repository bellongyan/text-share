<script setup lang="ts">
import { ref, computed, onUnmounted, onMounted } from 'vue'
import TextInput from '@/components/TextInput.vue'
import ShareButton from '@/components/ShareButton.vue'
import LinkDisplay from '@/components/LinkDisplay.vue'
import ThemeIcon from '@/components/ThemeIcon.vue'
import LanguageSwitch from '@/components/LanguageSwitch.vue'
import { useTheme } from '@/composables/useTheme'
import { useApi } from '@/composables/useApi'
import { useI18n } from '@/composables/useI18n'

const { isDark, toggleTheme } = useTheme()
const { initLocale } = useI18n()
const { createText, getViewCount } = useApi()
const { t } = useI18n()

const textContent = ref('')
const generatedLink = ref('')
const createdId = ref('')
const viewCount = ref(0)
const loading = ref(false)
const error = ref('')

let pollInterval: ReturnType<typeof setInterval> | null = null

const canSubmit = computed(() => textContent.value.trim().length > 0 && !loading.value)

const handleTextUpdate = (value: string) => {
  textContent.value = value
  error.value = ''
}

const handleClear = () => {
  textContent.value = ''
}

const startPollingViewCount = (id: string) => {
  stopPollingViewCount()
  pollInterval = setInterval(async () => {
    try {
      viewCount.value = await getViewCount(id)
    } catch {
      // ignore polling errors
    }
  }, 2000)
}

const stopPollingViewCount = () => {
  if (pollInterval) {
    clearInterval(pollInterval)
    pollInterval = null
  }
}

const generateLink = async () => {
  if (!canSubmit.value) return

  loading.value = true
  error.value = ''

  try {
    const device = navigator.userAgent.includes('Chrome') ? 'Chrome' : '浏览器'
    const result = await createText(textContent.value, device)
    generatedLink.value = `${window.location.origin}/s/${result.id}`
    createdId.value = result.id
    viewCount.value = result.viewCount || 0
    startPollingViewCount(result.id)
  } catch (err) {
    error.value = err instanceof Error ? err.message : t('send.error')
  } finally {
    loading.value = false
  }
}

const handleKeydown = (event: KeyboardEvent) => {
  if ((event.ctrlKey || event.metaKey) && event.key === 'Enter') {
    generateLink()
  }
}

onMounted(() => {
  initLocale()
})

onUnmounted(() => {
  stopPollingViewCount()
})
</script>

<template>
  <div
    class="min-h-screen bg-[var(--bg-primary)] dark:bg-[var(--bg-primary)]
           transition-colors duration-300"
    @keydown="handleKeydown"
  >
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

      <!-- Main content -->
      <main class="space-y-6 animate-slide-up">
        <!-- Text input section -->
        <div>
          <TextInput
            :model-value="textContent"
            @update:model-value="handleTextUpdate"
            @clear="handleClear"
          />
        </div>

        <!-- Error message -->
        <p v-if="error" class="text-red-500 text-sm text-center">{{ error }}</p>

        <!-- Submit button -->
        <ShareButton
          :loading="loading"
          :disabled="!canSubmit"
          @click="generateLink"
        />

        <!-- Keyboard hint -->
        <p class="text-xs text-center text-[var(--text-placeholder)]">
          {{ t('send.shortcut') }}
        </p>

        <!-- Link display -->
        <LinkDisplay
          v-if="generatedLink"
          :link="generatedLink"
          :view-count="viewCount"
        />
      </main>

      <!-- Footer -->
      <footer class="mt-12 text-center text-sm text-[var(--text-placeholder)]">
        <p>{{ t('app.tagline') }}</p>
      </footer>
    </div>
  </div>
</template>