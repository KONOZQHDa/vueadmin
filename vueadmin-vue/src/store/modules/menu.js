export default {
	namespaced: true,
	state: {
		//导航栏中的数据
		menuList: [],
		//权限
		authorizations: [],
		// 当前在编辑的标签名
		editableTabsValue: 'Index',
		//所以标签
		editableTabs: [
			{
				title: '首页',
				name: 'Index',
				path:''
			}
		],
	},
	actions: {},
	mutations: {
		SET_MENULIST(state, value) {
			state.menuList = value
		},

		SET_AUTHORIZATIONS(state, value) {
			state.authorizations = value
		},
		ADD_TAB(state, tab) {
			let exist = false
			//判断点击导航栏待添加的标签是否已经存在，若存在则通过修改editableTabsValue跳转到对应标签
			state.editableTabs.forEach(t =>{
				if (t.name === tab.name) {
					exist = true
					state.editableTabsValue = tab.name
				}
			})
			//不存在则添加标签
			if (!exist) {
				state.editableTabs.push({
					title: tab.title,
					name: tab.name,
					path: tab.path
				});
				state.editableTabsValue = tab.name
			}
		},
		//删除标签，element中自带的代码搬过来并稍微修改
		REMOVE_TAB(state,targetName) {
			let tabs = state.editableTabs;

			let activeName = state.editableTabsValue;
			if (activeName === targetName) {
				tabs.forEach((tab, index) => {
					if (tab.name === targetName) {
						let nextTab = tabs[index + 1] || tabs[index - 1];
						if (nextTab) {
							activeName = nextTab.name;
						}
					}
				});
			}

			state.editableTabsValue = activeName;
			//自己修改的，如果只有一个标签了则不修改
			if(state.editableTabs.length > 1){
				state.editableTabs = tabs.filter(tab => tab.name !== targetName);
			}
		}
	}
}