import request from '../../../network/request'

export function addType(data) {
	return request({
		method: 'put',
		url: 'http://localhost:8081/sort/addSort',
		data
	});
}

export function updateType(data) {
	return request({
		method: 'put',
		url: 'http://localhost:8081/sort/updateSort',
		data
	});
}

export function deleteType(params) {
	return request({
		method: 'delete',
		url: 'http://localhost:8081/sort/deleteSort',
		params
	});
}

export function getType(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sort/listSort',
		params
	});
}


