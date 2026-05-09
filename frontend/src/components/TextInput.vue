<script setup lang="ts">
import { ref, computed } from 'vue'
import { useI18n } from '@/composables/useI18n'

const props = defineProps<{
  modelValue: string
  maxLength?: number
}>()

const emit = defineEmits<{
  'update:modelValue': [value: string]
  'clear': []
}>()

const { t } = useI18n()
const maxLength = props.maxLength || 50000
const textareaRef = ref<HTMLTextAreaElement | null>(null)

const charCount = computed(() => props.modelValue.length)
const isOverLimit = computed(() => charCount.value > maxLength)

const handleInput = (event: Event) => {
  const target = event.target as HTMLTextAreaElement
  emit('update:modelValue', target.value)
}

const handleClear = () => {
  if (charCount.value > 100) {
    if (confirm(t('send.clearConfirm'))) {
      emit('clear')
    }
  } else {
    emit('clear')
  }
}

const focus = () => {
  textareaRef.value?.focus()
}

defineExpose({ focus })
</script>

<template>
  <div class="relative group">
    <textarea
      ref="textareaRef"
      :value="modelValue"
      @input="handleInput"
      :placeholder="t('send.placeholder')"
      :maxlength="maxLength"
      class="w-full min-h-[200px] md:min-h-[300px] lg:min-h-[400px] p-4 pr-12
             bg-[var(--bg-card)] dark:bg-[var(--bg-card)]
             border-2 border-transparent dark:border-transparent
             rounded-xl shadow-sm resize-y
             text-base font-mono text-[var(--text-primary)] dark:text-[var(--text-primary)]
             placeholder:text-[var(--text-placeholder)]
             focus:outline-none focus:border-amber-400 dark:focus:border-amber-400
             focus:ring-4 focus:ring-amber-400/20
             transition-all duration-200"
      :class="{ 'border-red-500': isOverLimit }"
      autofocus
    ></textarea>

    <!-- Clear button -->
    <button
      v-if="modelValue.length > 0"
      @click="handleClear"
      class="absolute top-4 right-4 w-8 h-8
             flex items-center justify-center
             bg-gray-200/80 dark:bg-gray-700/80
             hover:bg-gray-300 dark:hover:bg-gray-600
             rounded-lg opacity-0 group-hover:opacity-100
             transition-all duration-150"
      :title="t('send.clearTitle')"
    >
      <svg class="w-4 h-4 text-gray-600 dark:text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
      </svg>
    </button>
  </div>

  <!-- Character count -->
  <div class="mt-2 flex justify-between items-center text-sm">
    <span :class="isOverLimit ? 'text-red-500' : 'text-[var(--text-secondary)]'">
      {{ t('send.charCount', { count: charCount.toLocaleString(), max: maxLength.toLocaleString() }) }}
    </span>
    <span v-if="isOverLimit" class="text-red-500 font-medium">{{ t('send.overLimit') }}</span>
  </div>
</template>