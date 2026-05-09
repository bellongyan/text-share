import { createRouter, createWebHistory } from 'vue-router'
import SendView from '@/views/SendView.vue'
import ReceiveView from '@/views/ReceiveView.vue'
import ExpiredView from '@/views/ExpiredView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'send',
      component: SendView,
      meta: { title: 'TextShare - 企业局域网文本传输工具' }
    },
    {
      path: '/s/:id',
      name: 'receive',
      component: ReceiveView,
      meta: { title: '查看文本 - TextShare' }
    },
    {
      path: '/expired',
      name: 'expired',
      component: ExpiredView,
      meta: { title: '链接已过期 - TextShare' }
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/'
    }
  ]
})

router.beforeEach((to, _from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title as string
  }
  next()
})

export default router