import Vue from 'vue'
import App from '@/App.vue'
import router from '@/router'
import store from '@/store'

// import axios
import axios from "axios";
import cssMain from "@/assets/main.css";

Vue.use(cssMain)
Vue.prototype.$axios = axios
axios.defaults.withCredentials = true
axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8'

// import iView
import 'view-design/dist/styles/iview.css'
import ViewUI from 'view-design'

Vue.use(ViewUI)

// import Bootstrap
// import 'jquery'
// import 'popper.js'
// import 'bootstrap/dist/js/bootstrap.min.js'
// import 'bootstrap/dist/css/bootstrap.min.css'

// import ElementUI
import 'element-ui/lib/theme-chalk/index.css'
import ElementUI from 'element-ui'

Vue.use(ElementUI)

Vue.config.productionTip = false
// root Vue instance
new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')

