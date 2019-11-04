import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/views/Main' //메인
import Login from '@/views/Login' //로그인
import Register from '@/views/Register' //로그인

Vue.use(Router)

const requireAuth = (to, from, next) => {
  let isLoggedIn = !!sessionStorage.getItem('token')

  if (isLoggedIn) return next()
  next({
    path: '/login',
    query: { redirect: to.fullPath }
  })
}


export default new Router({
  //mode: 'history',  //# 없애는 부분
  routes: [
    { path: '/',             name: 'Main',   component: Main, beforeEnter: requireAuth  },
    { path: '/login',        name: 'Login',        component: Login },
    { path: '/Register',     name: 'Register',     component: Register }
  ],
    scrollBehavior (to, from, savedPosition) {
      if (savedPosition) {
        return savedPosition
      } else {
        return { x: 0, y: 0 }
      }
    }
})

