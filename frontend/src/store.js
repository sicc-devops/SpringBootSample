import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)


export default new Vuex.Store({
  state: {
    token: null,
    empno:'',
    empnm:'',
    expire:'',
    isLoggedIn: !!sessionStorage.getItem('token')
  },
  getters: {
    
  },
  mutations: {
    LOGIN (state, {token}) {
      state.token = token
      state.isLoggedIn = true
     
    },
    LOGOUT (state) {
      state.token = null
      state.isLoggedIn = false
      delete sessionStorage.token
    }
  },
  actions: {
    LOGIN ({commit}, {username, password}) {
      return axios.post(`/auth/login`, {username, password})
        .then(({data}) => {
          
          commit('LOGIN', data)

          // 모든 HTTP 요청 헤더에 Authorization 을 추가한다.
          //axios.defaults.headers.common['Authorization'] = `Bearer ${data.token}`;
 
        })
    },
    LOGOUT ({commit}) {
      //axios.defaults.headers.common['Authorization'] = undefined
      commit('LOGOUT')
    },
  }
})