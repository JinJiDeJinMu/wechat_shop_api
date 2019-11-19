import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui' // element
import 'element-ui/lib/theme-chalk/index.css' // element
import axios from 'axios' // axios
import qs from 'qs' // axios自带
import VueScroll from 'vuescroll' // 虚拟滚动条
import 'vuescroll/dist/vuescroll.css'
import VueParticles from 'vue-particles' // 粒子特效动画 安装npm install vue-particles --save-dev
import echarts from 'echarts' // 百度echarts插件

Vue.config.productionTip = false
Vue.use(VueParticles) // 粒子动画
Vue.use(ElementUI) // element
Vue.use(echarts) // echarts
Vue.use(VueScroll, {ops: {bar: {background: '#C0C4CC'}}})

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded'
axios.defaults.baseURL = 'http://127.0.0.1:8081/'
axios.defaults.withCredentials = true
Vue.prototype.$http = axios
Vue.prototype.$axios = axios
Vue.prototype.$qs = qs
Vue.prototype.$echarts = echarts

/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    axios,
    components: {App},
    template: '<App/>'
})
