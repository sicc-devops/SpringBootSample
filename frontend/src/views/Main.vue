<template>
<div>
<app-header ></app-header>
    <div>
        <h2 class="logo" style="font-size:32px; font-weight:bold;">Vue Sample</h2>
        <p>User Id: {{userInfo.userId}}</p>
        <p>User Name: {{userInfo.userName}}</p>
        <p>Password: {{userInfo.password}}</p>
        <div class="copy">
            COPYRIGHT© 2017 SSANGYONG&amp;COMMUNICATIONS. ALL RIGHTS RESERVED.
        </div>

        <loading :active.sync="isLoading" :can-cancel="true" color="#ff0000" :is-full-page="true"></loading>
    </div>
<app-footer ></app-footer>
</div>
</template>


<script>
import {AXIOS} from '../libs/http-common'
import Loading from 'vue-loading-overlay'

import appHeader from "../components/Header";
import appFooter from "../components/Footer";

export default {
    name: 'Main',
    components: {
        appHeader,
        appFooter,
        Loading
    },
    data() {
        return {
            token: window.sessionStorage.getItem('token'),
            msg:'',
            saveCheck:false,
            isLoading:false,
            userInfo: {
                userId:'userId',
                userName:'userName',
                password:'password'
            }
        }
    },
    userInfo(){

    },
    created(){
    },
    mounted() {
        this.getCurrentUser();
    },
    methods: {
        getCurrentUser(){
            console.log(this.token);
            AXIOS.defaults.headers.common['Authorization'] = `Bearer ${this.token}`;
            AXIOS.get('/user/detail')
            .then(response => {
                console.log(response.data);
                if(response.data.ResultCode!='200'){
                    alert(response.data.resultMsg);
                    this.isLoading = false;
                } else{
                    let self = this;
                    self.isLoading = false;
                    this.userInfo = response.data.UserVO;
                    console.log(this.userInfo);
                }
            })
            .catch(e => {
                console.log(e);
            });
        }
    }
}
</script>