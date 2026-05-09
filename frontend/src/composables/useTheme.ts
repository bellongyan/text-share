import { ref, onMounted } from 'vue'

export function useTheme() {
  const isDark = ref(false)
  const STORAGE_KEY = 'textshare-theme'

  const applyTheme = (dark: boolean) => {
    isDark.value = dark
    if (dark) {
      document.documentElement.classList.add('dark')
    } else {
      document.documentElement.classList.remove('dark')
    }
    localStorage.setItem(STORAGE_KEY, dark ? 'dark' : 'light')
  }

  const toggleTheme = () => {
    applyTheme(!isDark.value)
  }

  onMounted(() => {
    const saved = localStorage.getItem(STORAGE_KEY)
    if (saved) {
      applyTheme(saved === 'dark')
    } else {
      applyTheme(window.matchMedia('(prefers-color-scheme: dark)').matches)
    }
  })

  return { isDark, toggleTheme }
}