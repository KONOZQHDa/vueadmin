import {getFavorites} from "@/api/moudles/bookstore/favorites";

export default {
	namespaced: true,
	state: {
		favorites: []
	},
	actions: {
		GET_FAVORITES(context) {
			getFavorites().then(resp => {
				context.commit('SET_FAVORITES', resp.data.data)
			}, error => {

			})
		}
	},
	mutations: {
		SET_FAVORITES(state, value) {
			state.favorites = value
		}
	}
}