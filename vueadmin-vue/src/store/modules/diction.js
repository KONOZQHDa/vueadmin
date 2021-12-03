import request from '../../network/request'
import {Message} from 'element-ui';

export default {
	namespaced: true,
	state: {
		dictypes: {}
	},
	actions: {
		getDicTypes(context, value) {
			request({
				methods: 'get',
				url: 'http://localhost:8081/diction/getDic',
				params: {}
			}).then(resp => {
				context.commit('SET_DICTYPES', resp.data.data)
			}, error => {
				Message.error(error)
			})
		}
	},
	mutations: {
		SET_DICTYPES(state, value) {
			state.dictypes = value;
		}
	}
}