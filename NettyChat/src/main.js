import Vue from 'vue'

import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import App from './App'
import axios from 'axios'

import router from './router'
Vue.prototype.$axios = axios;
// import VueSocketIO from 'vue-socket.io'

Vue.config.productionTip = false

Vue.use(ElementUI);
// Vue.use(new VueSocketIO({
//     debug: true,
//     connection: 'ws://127.0.0.1:7000/websocket',
 
// }))


/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App),

})
