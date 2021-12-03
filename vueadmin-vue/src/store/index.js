import Vue from 'vue'
import Vuex from 'vuex'
import menu from "./modules/menu";
import diction from "@/store/modules/diction";

Vue.use(Vuex)

export default new Vuex.Store({
	state: {
		tokenName: '',
		tokenValue: '',
		isSysUser: 0
	},
	mutations: {
		SET_TOKEN(state, token) {
			state.tokenName = token.tokenName
			state.tokenValue = token.tokenValue
			sessionStorage.setItem('tokenName', token.tokenName)
			sessionStorage.setItem('tokenValue', token.tokenValue)
		},
		CLEAR_TOKEN(state) {
			state.tokenName = ''
			state.tokenValue = ''
			sessionStorage.setItem('tokenName', '')
			sessionStorage.setItem('tokenValue', '')
		},
		SET_ISSYSUSER(state, p) {
			state.isSysUser = p
		}
	},
	actions: {},
	modules: {
		menu,
		diction
	}
})
