import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Element from 'element-ui'
import "element-ui/lib/theme-chalk/index.css"
import './mixin/mixin'

require("./network/mock.js")

Vue.use(Element)
Vue.config.productionTip = false
new Vue({
	router,
	store,
	mounted() {
		this.$store.dispatch('diction/getDicTypes')
		this.$store.dispatch('book/GET_BOOKTYPES')
		this.$store.dispatch('favorites/GET_FAVORITES'),
				this.$store.dispatch('book/GET_DISCOUNTBOOKS')
		Vue.prototype.$bus = this
	},
	render: h => h(App)
}).$mount('#app')
