import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '../components/LoginPage.vue'
import RegisterPage from '../components/RegisterPage.vue'
import HomePage from '../components/HomePage.vue'

const routes = [
  {
    path: '/',
    redirect: '/user/login'
  },
  {
    path: '/user/login',
    name: 'Login',
    component: LoginPage
  },
  {
    path: '/user/register',
    name: 'Register',
    component: RegisterPage
  },
  {
    path: '/home',
    name: 'Home',
    component: HomePage,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    next('/user/login')
  } else {
    next()
  }
})

export default router