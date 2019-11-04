<template>
    <div class="login">
        <h2 class="logo" style="font-size:32px; font-weight:bold;">Vue Sample</h2>
        <form name="loginForm" id="loginForm">
            <input type="text" v-model="username" class="txt_input" placeholder="아이디" @keyup.enter="loginProcess()" maxlength="16" value='' />
            <input type="password" v-model="password" class="txt_input" placeholder="비밀번호" @keyup.enter="loginProcess()"/>
            <input type="button" class="btn btn_login" @click="loginProcess()"  value="로그인"/>
            <label for="idSaveCheck" class="check_login">
                <input type="checkbox" id="idSaveCheck" v-model="saveCheck" />사원번호저장
            </label>
            <input type="button" class="btn btn_login" @click="goRegister()"  value="가입"/>
        </form>
        <div class="btnbox">

        </div>
        <div class="copy">
            COPYRIGHT© 2017 SSANGYONG&amp;COMMUNICATIONS. ALL RIGHTS RESERVED.
        </div>

        <loading :active.sync="isLoading" :can-cancel="true" color="#ff0000" :is-full-page="true"></loading>
    </div>
</template>

<script>
import {AXIOS} from '../libs/http-common'
import Loading from 'vue-loading-overlay'

export default {
    name: 'Login',
    components: {
        Loading
    },
    data() {
        return {
            username: '',
            password: '',
            msg:'',
            saveCheck:false,
            isLoading:false
        }
    },
    created(){
        if(window.sessionStorage.getItem('savecheck')!=''){
            this.username =window.sessionStorage.getItem('savecheck') ;
            this.saveCheck = true;
        } else {
            this.username = '';
            this.saveCheck = false;
        }
    },
    mounted() {


    },
    methods: {
      goRegister (){
         let self = this;
         self.$router.push('/Register');
      },
      loginProcess () {
         this.isLoading = true
         if(this.saveCheck){
             window.sessionStorage.setItem('savecheck', this.username);
         } else {
             window.sessionStorage.setItem('savecheck','');
         }

        let data = JSON.stringify({
            userId: this.username,
            password: this.password
        })

        AXIOS.post('/auth/login', data)
        .then(response => {
            if(response.data.token==undefined){
                alert('아이디 및 비밀번호가 틀립니다.');
                this.isLoading = false
            } else{
                let self = this;
                setTimeout(function(){
                    self.isLoading = false
                    if(response.data.code != '200'){
                        alert(response.data.message);
                        return;
                    }

                    window.sessionStorage.setItem('token', response.data.token);
                    AXIOS.defaults.headers.common['Authorization'] = `Bearer ${response.data.token}`;
                    self.$router.push('/');

                },1000);
            }
        })
        .catch(e => {
            console.log(e);
        });
      }
    }
}
</script>



<style>

.vld-overlay {
  bottom: 0;
  left: 0;
  position: absolute;
  right: 0;
  top: 0;
  align-items: center;
  display: none;
  justify-content: center;
  overflow: hidden;
  z-index: 1
}

.vld-overlay.is-active {
  display: flex
}

.vld-overlay.is-full-page {
  z-index: 999;
  position: fixed
}

.vld-overlay .vld-background {
  bottom: 0;
  left: 0;
  position: absolute;
  right: 0;
  top: 0;
  background: #fff;
  opacity: 0.5
}

.vld-overlay .vld-icon, .vld-parent {
  position: relative
}
</style>
