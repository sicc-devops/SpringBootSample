<template>
    <div class="register">
        <h2 class="logo" style="font-size:32px; font-weight:bold;">Register</h2>
        <form name="loginForm" id="loginForm">
            <input type="text" id="userId" v-model="userid" class="txt_input" placeholder="아이디"  maxlength="16" value='' /><br>
            <input type="password" id="password" v-model="password" class="txt_input" placeholder="비밀번호" /><br>
            <input type="Password" id="confirmPassword" v-model="confirmPassword" class="txt_input" placeholder="비밀번호 확인" /><br>
            <input type="text" id="username" v-model="username" class="txt_input" placeholder="성명" /><br>
            <input type="button" class="btn btn_reg" @click="regProcess()"  value="가입"/>
            <input type="button" class="btn btn_cancel" @click="cancel()"  value="취소"/>
        </form>
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
    name: 'Register',
    components: {
        Loading
    },
    data() {
        return {
            userid: '',
            username: '',
            password: '',
            msg:'',
            saveCheck:false,
            isLoading:false
        }
    },
    created(){
    },
    mounted() {


    },
    methods: {
      regProcess () {
         this.isLoading = true
         if(this.userid == ''){
             alert('아이디를 입력 해 주세요');
             document.getElementById('userid').focus();
             this.isLoading = false;
             return;
         } else if (this.password == '') {
             alert('비밀번호를 입력 해 주세요');
             document.getElementById('password').focus();
             this.isLoading = false;
             return;
         } else if (this.password != this.confirmPassword) {
             alert('비밀번호를 확인 해 주세요');
             document.getElementById('confirmPassword').focus();
             this.isLoading = false;
             return;           
         } else if (this.username == '') {
             alert('사용자명를 입력 해 주세요');
             document.getElementById('username').focus();
             this.isLoading = false;
             return;
         }

        let data = JSON.stringify({
            userId: this.userid,
            userName: this.username,
            password: this.password
        })

        AXIOS.post('/auth/Register', data)
        .then(response => {
            if(response.data.ResultCode!=200){
                alert(response.data.ResultMsg);
                this.isLoading = false
            } else{
                let self = this;
                setTimeout(function(){
                    self.isLoading = false
                    window.sessionStorage.setItem('savecheck', this.userid);
                    self.$router.push('/Login');
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