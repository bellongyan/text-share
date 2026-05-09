import { ref, watch } from 'vue'
import { useI18n as vueUseI18n } from 'vue-i18n'

const LOCALE_STORAGE_KEY = 'textshare-locale'

const locale = ref<'zh' | 'en'>('zh')

export function useI18n() {
  const { t, locale: i18nLocale } = vueUseI18n()

  watch(locale, (newLocale) => {
    i18nLocale.value = newLocale
    localStorage.setItem(LOCALE_STORAGE_KEY, newLocale)
  }, { immediate: true })

  const setLocale = (newLocale: 'zh' | 'en') => {
    locale.value = newLocale
  }

  const initLocale = () => {
    const saved = localStorage.getItem(LOCALE_STORAGE_KEY)
    if (saved && (saved === 'zh' || saved === 'en')) {
      locale.value = saved
    } else {
      const browserLang = navigator.language.toLowerCase()
      locale.value = browserLang.startsWith('en') ? 'en' : 'zh'
    }
    i18nLocale.value = locale.value
  }

  return {
    locale,
    setLocale,
    initLocale,
    t
  }
}

export function getLocale(): string {
  return localStorage.getItem(LOCALE_STORAGE_KEY) || 'zh'
}
