import "babel-polyfill" // 새로추가

import Vue from 'vue'
require("expose-loader?$!jquery")
require("expose-loader?jQuery!jquery")

import App from './App.vue'
import router from './router'
import store from './store'

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')