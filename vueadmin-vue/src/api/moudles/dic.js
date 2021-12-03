import request from "@/network/request";

export function searchDictype(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/dictype/search',
		params
	});
}

export function searchDiction(params) {
	request({
		method: 'get',
		url: 'http://localhost:8081/diction/search',
		params
	})

	export function getDictypes() {
		return request({
			method: 'get',
			url: 'http://localhost:8081/dictype/listDicType',
		});
	}

	export function deleteDictType(params) {
		return request({
			method: 'delete',
			url: 'http://localhost:8081/dictype/deleteDicType',
			params
		});
	}

	export function deleteDiction(params) {
		return request({
			method: 'delete',
			url: 'http://localhost:8081/diction/deleteDic',
			params
		});
	}

	export function updateDictype(data) {
		return request({
			method: 'put',
			url: 'http://localhost:8081/dictype/updateDicType',
			data
		});
	}

	export function addDictype(data) {
		return request({
			method: 'post',
			url: 'http://localhost:8081/dictype/addDicType',
			data: this.dictypeEditForm
		});
	}

	export function updateDiction(data) {
		request({
			method: 'put',
			url: 'http://localhost:8081/diction/updateDic',
			data
		})
	}

	export function addDiction(data) {
		request({
			method: 'post',
			url: 'http://localhost:8081/diction/addDic',
			data
		})
	}
}