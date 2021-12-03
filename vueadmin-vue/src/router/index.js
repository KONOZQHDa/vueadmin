import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from "@/views/Home"
import Login from "@/views/Login"
import Index from "../components/Index"
import request from "../network/request"
import store from "../store"
import Register from "@/views/Register"
import UserCenter from "@/components/system/UserCenter";

Vue.use(VueRouter)
const routes = [
	{
		path: '/',
		name: 'Home',
		component: Home,
		children: [
			// {
			// 	path: 'index',
			// 	name: 'Index',
			// 	component: Index
			// }
		]
	},
	{
		path: '/book',
		name: 'BookHome',
		component: Home,
		children: [
			{
				path: 'index',
				name: 'Index',
				component: Index
			},
			{
				path: 'userCenter',
				name: 'UserCenter',
				component: UserCenter,
			}
		]
	},
	{
		path: '/login',
		name: 'Login',
		component: Login
	},
	{
		path: '/sysLogin',
		name: 'SysLogin',
		component: () => import('@/views/SysLogin')
	},
	{
		path: '/register',
		name: 'Register',
		component: Register
	}
]

const router = new VueRouter({
	routes
})
router.beforeEach((to, from, next) => {
	if (to.path != '/register' && to.path != '/login' && to.path != '/syslogin') {
		let isBind = sessionStorage.getItem('isBind') ? true : false
		//还未绑定菜单
		if (!isBind) {
			request({
				method: 'get',
				url: 'http://localhost:8081/sysMenu/getUserMenus'
			}).then(response => {
				let nav = response.data.data;
				let authorizations = response.data.data.authorizations;
				//将请求得到的菜单数据的属性名改为和前端中对应使用的一样
				formatMenu(nav)
				store.commit('menu/SET_MENULIST', nav)
				store.commit('menu/SET_AUTHORIZATIONS', authorizations)
				let newRoutes = router.options.routes;

				//生成动态路由
				nav.forEach(menu => {
					if (menu.children) {
						menu.children.forEach(menuitem => {
							newRoutes[0].children.push(menuToRoute(menuitem))
						})
					}
				})
				//这句不要漏，不然不生效
				router.addRoutes(newRoutes)
				sessionStorage.setItem('isBind', 'yes')
				window.onbeforeunload = function (event) {
					//将已经打开的标签页的内容存到sessionStorage
					//这样刷新页面时打开的标签不会消失
					let tabsJson = JSON.stringify(store.state.menu.editableTabs)
					let activeName = store.state.menu.editableTabsValue
					sessionStorage.setItem('tabs', tabsJson)
					sessionStorage.setItem('activeName', activeName)
					//修改isBind缓存为空，这样刷新时才能再次进入if语句，然后重新请求数据
					sessionStorage.setItem('isBind', '')
				}
			})
		}
	}
	next()
})

//将后台请求来的菜单转换为vue路由
const menuToRoute = (menu) => {
	if (!menu.component) {
		return null
	}
	const route = {
		path: menu.path,
		name: menu.name,
	}
	//webpack import里面的路径不能使用变量，需要通过es6的``来
	route.component = () => import(`@/components/${menu.component}`)//无法正常实现功能
	return route
}

//将请求得到的菜单变量属性名改为前端之前设定的对应变量名
const formatMenu = (nav) => {
	nav.map((item => {
		item.title = item.name

		if (item.children) {
			item.children.map((e => {
				e.title = e.name
				e.name = e.routeName
				return e
			}))
		}
		return item
	}))


}
export default router
