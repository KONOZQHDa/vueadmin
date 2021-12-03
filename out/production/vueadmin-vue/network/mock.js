import Mock from "mockjs"

const Random = Mock.Random

const Result = {
	code: 200,
	msg: '操作成功',
	data: null
}

/*Mock.mock('http://localhost:8080/captcha', 'get', () => {
	Result.data = {
		token: Random.string(32),
		captchaImg: Random.dataImage('100x40', 'p7n5w')
	}
	return Result
})*/


// Mock.mock("/sys/userInfo", 'get', () => {
// 	Result.data = {
// 		userInfo: {
// 			id: 1,
// 			name: 'KONO曾淇杭哒！',
// 			avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
// 		},
// 		authorizations: ['sys:user:add','sys:role:add','sys:user:delete']
//
// 	}
// 	return Result
// })

// Mock.mock("/logout", 'get', () => {
// 	return Result
// })

// Mock.mock('http://localhost:8080/sys/menuList', 'get', () => {
// 	Result.data = {
// 		nav: [
// 			{
// 				name: 'backgroundManage',
// 				title: '后台管理',
// 				icon: 'el-icon-star-on',
// 				children: [
// 					{
// 						name: 'bookManage',
// 						title: '书籍管理',
// 						icon: 'el-icon-s-management',
// 						path: '/sys/bookManage',
// 						component: 'system/BookManage.vue'
// 					}
// 				]
// 			},
// 			{
// 				name: 'systemManager',
// 				title: '系统管理',
// 				icon: 'el-icon-menu',
// 				children: [
// 					{
// 						name: 'sysUsers',
// 						title: '用户管理',
// 						icon: 'el-icon-s-custom',
// 						path: '/sys/users',
// 						component: 'system/User.vue'
// 					},
// 					{
// 						name: 'sysRoles',
// 						title: '角色管理',
// 						icon: 'el-icon-s-marketing',
// 						path: '/sys/roles',
// 						component: 'system/Role.vue'
// 					},
// 					{
// 						name: 'sysMenus',
// 						title: '菜单管理',
// 						icon: 'el-icon-s-tools',
// 						path: '/sys/menus',
// 						component: 'system/Menu.vue'
// 					}
// 				]
// 			},
// 			{
// 				name: 'systemTools',
// 				title: '系统工具',
// 				icon: 'el-icon-s-tools',
// 				children: [
// 					{
// 						name: 'dictionary',
// 						title: '数字字典',
// 						icon: 'el-icon-document',
// 						path: '/sys/dictionary',
// 						component: 'system/Dictionary.vue'
// 					}
// 				]
// 			}
// 		],
// 		authorizations: [],
// 	}
//
// 	return Result
// })

// Mock.mock('http://localhost:8080/getMenusTableData', 'get', () => {
// 	Result.data = {
// 		tableData: [
// 			{
// 				id: 1,
// 				name: '系统管理',
// 				state: 1,
// 				type: 0,
// 				permit:'sys:management',
// 				children: [
// 					{
// 						id: 11,
// 						name: '用户管理',
// 						state: 1,
// 						type: 1,
// 						url:'sys/users',
// 						component:'system/user.vue',
// 						children:[
// 							{
// 								id: 111,
// 								name: '添加用户',
// 								state: 1,
// 								type: 2
// 							}
// 						]
// 					},
// 					{
// 						id: 12,
// 						name: '角色管理',
// 						state: 1,
// 						type: 1,
// 						url:'sys/roles',
// 						component:'system/role.vue',
// 					},
// 					{
// 						id: 13,
// 						name: '菜单管理',
// 						state: 1,
// 						type: 1,
// 						url:'sys/menus',
// 						component:'system/menu.vue',
// 					}
// 				]
// 			},
// 			{
// 				id: 2,
// 				name: '系统工具',
// 				state: 0,
// 				type: 0,
// 				children: [
// 					{
// 						id: 21,
// 						name: '数字字典',
// 						state: 0,
// 						type: 1
// 					}
// 				]
// 			}
// 		],
// 		idsArray: [1,11,111,2]
//
// 	}
// 	return Result
// })

// Mock.mock('http://localhost:8080/getRolesTableData', 'get', () => {
// 	Result.data = {
// 		tableData: [
// 			{
// 				roleName: '普通用户',
// 				roleCode: 'normal',
// 				description: '',
// 				state: 1,
// 			}, {
// 				roleName: '超级管理员',
// 				roleCode: 'admin',
// 				description: '',
// 				state: 1,
// 			}
// 		],
// 		total: 2
// 	}
// 	return Result
// })
// Mock.mock('http://localhost:8080/getUsersTableData', 'get', () => {
// 	Result.data = {
// 		tableData: [
// 			{
// 				id: 1,
// 				avatar: '',
// 				username: 'admin',
// 				roles: ['normal','admin'],
// 				email: '123456@dio.com',
// 				createTime: '2021.08.26',
// 				state: 1,
// 			}, {
// 				id: 2,
// 				avatar: '',
// 				username: 'user',
// 				roles: ['normal'],
// 				email: '123456@jojo.com',
// 				createTime: '2021.08.26',
// 				state: 1,
// 			}
// 		],
// 		total: 2
// 	}
// 	return Result
// })


