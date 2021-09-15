
import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from "@/components/Home"
import Login from "@/components/Login"
import Index from "../components/Index"
import Menu from "../components/system/Menu";
import Role from "../components/system/Role";
import User from "@/components/system/User.vue";
import UserCenter from "../components/UserCenter";
import request from "../network/request"
import store from "../store"

Vue.use(VueRouter)
const routes = [
	{
		path: '/',
		name: 'Home',
		component: Home,
		children:[
		  {
		    path:'/index',
		    name:'Index',
		    component:Index
		  },
		  {
		    path:'/sys/users',
		    name:'Users',
		    component:User
		  },
		  {
		    path:'/sys/roles',
		    name:'Roles',
		    component:Role
		  },
		  {
		    path:'/sys/menus',
		    name:'Menus',
		    component:Menu
		  },
		  {
		    path:'/userCenter',
		    name:'UserCenter',
		    component:UserCenter
		  },
		]
	},
	{
		path: '/login',
		name: 'Login',
		component: Login
	}
]

const router = new VueRouter({
	routes
})
//全局路由守卫实现动态导航栏渲染和对应路由绑定，动态路由绑定出bug了未完成
router.beforeEach((to, from, next) => {
	let isBind = sessionStorage.getItem('isBind') ? true : false
	if(!isBind){
		request({
			method: 'get',
			url: 'sys/menuList'
		}).then(response => {
			let nav = response.data.data.nav;
			let authorizations = response.data.data.authorizations;
			store.commit('menu/SET_MENULIST', nav)
			store.commit('menu/SET_AUTHORIZATIONS', authorizations)
			/*let newRoutes = router.options.routes;
			nav.forEach(menu => {
				if (menu.childrenMenus) {
					menu.childrenMenus.forEach(menuitem => {
						newRoutes[0].children.push(menuToRoute(menuitem))
					})
				}
			})*/
			sessionStorage.setItem('isBind','yes')
			window.onbeforeunload = function(event){
				//将已经打开的标签页的内容存到sessionStorage
				//这样刷新页面时打开的标签不会消失
				let tabsJson = JSON.stringify(store.state.menu.editableTabs)
				let activeName = store.state.menu.editableTabsValue
				sessionStorage.setItem('tabs',tabsJson)
				sessionStorage.setItem('activeName',activeName)
				//修改isBind缓存为空，这样刷新时才能再次进入if语句，然后重新请求数据
				sessionStorage.setItem('isBind','')
			}
		})
	}
	next()
})

/*const menuToRoute = (menu) => {
	if (!menu.component) {
		return null
	}
	const route = {
		path: menu.path,
		name: menu.name,
	}
  route.component = () => import(menu.component)//无法正常实现功能
  return route
}*/

export default router
