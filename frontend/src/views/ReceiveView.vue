<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import TextDisplay from '@/components/TextDisplay.vue'
import ThemeIcon from '@/components/ThemeIcon.vue'
import { useTheme } from '@/composables/useTheme'
import { useApi } from '@/composables/useApi'
import type { GetTextResponse } from '@/types'

const { isDark, toggleTheme } = useTheme()
const { getText, incrementView } = useApi()
const route = useRoute()
const router = useRouter()

const loading = ref(true)
const textData = ref<GetTextResponse | null>(null)
const error = ref('')

const formatDate = (dateStr: string) => {
  const date = new Date(dateStr.replace(' ', 'T'))
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(async () => {
  const id = route.params.id as string

  try {
    const data = await getText(id)
    textData.value = data

    if (data.expired) {
      router.push('/expired')
      return
    }

    // 异步增加浏览次数，不阻塞页面加载
    incrementView(id).catch(() => {})
  } catch (err) {
    const message = err instanceof Error ? err.message : '加载失败，请返回首页重试'
    if (message.includes('不存在') || message.includes('过期')) {
      router.push('/expired')
    } else {
      error.value = message
    }
  } finally {
    loading.value = false
  }
})
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
      </header>

      <!-- Loading state -->
      <div v-if="loading" class="flex items-center justify-center py-20">
        <svg class="w-8 h-8 text-amber-500 animate-spin" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
        </svg>
        <span class="ml-3 text-[var(--text-secondary)]">加载中...</span>
      </div>

      <!-- Error state -->
      <div v-else-if="error" class="text-center py-20">
        <p class="text-red-500">{{ error }}</p>
        <router-link
          to="/"
          class="inline-block mt-4 px-6 py-2 bg-amber-500 hover:bg-amber-600 text-white rounded-lg transition-colors"
        >
          返回首页
        </router-link>
      </div>

      <!-- Content -->
      <main v-else-if="textData" class="animate-slide-up">
        <TextDisplay
          :content="textData.content"
          :device="textData.device"
          :created-at="formatDate(textData.createdAt)"
        />
      </main>
    </div>
  </div>
</template>