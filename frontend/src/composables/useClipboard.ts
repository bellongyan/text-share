import { ref } from 'vue'

export function useClipboard() {
  const copied = ref(false)
  let timeoutId: ReturnType<typeof setTimeout> | null = null

  const copyToClipboard = async (text: string): Promise<boolean> => {
    try {
      await navigator.clipboard.writeText(text)
      copied.value = true
      if (timeoutId) clearTimeout(timeoutId)
      timeoutId = setTimeout(() => {
        copied.value = false
      }, 2000)
      return true
    } catch (err) {
      console.error('Failed to copy:', err)
      return false
    }
  }

  return { copied, copyToClipboard }
}