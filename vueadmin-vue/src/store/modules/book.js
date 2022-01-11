import {getDiscountBooks, getSorts} from "@/api/moudles/system/book";

export default {
	namespaced: true,
	state: {
		bookTypes: [],
		//当前时间的特价书
		discountBooks: [],
		getDiscountBookInterval: null
	},
	actions: {
		GET_BOOKTYPES(context) {
			getSorts({usful: 1}).then(resp => {
				context.commit('SET_BOOKTYPES', resp.data.data)
			})
		},
		GET_DISCOUNTBOOKS(context) {
			getDiscountBooks().then(resp => {
				context.commit('SET_DISCOUNTBOOKS', resp.data.data)
			})
		}
	},
	mutations: {
		SET_BOOKTYPES(state, value) {
			state.bookTypes = value
		},
		SET_DISCOUNTBOOKS(state, value) {
			state.discountBooks = value
		}
	}
}