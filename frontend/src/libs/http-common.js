import axios from 'axios'

const AXIOS = axios.create({
  baseURL: '/',
  timeout: 40000,
  headers: {
    'Content-Type': 'application/json',
    Authorization: {
        toString () {
          if(sessionStorage.getItem('token')!=null){
            if(sessionStorage.getItem('token')!= 'undefined'){
              return 'Bearer ' + sessionStorage.getItem('token');
            }
          } else {
            return null;
          }
        }
      }
  }
})

export {AXIOS}
