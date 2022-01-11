import request from "../../../network/request"
import qs from "qs";

export function getMunus() {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sysMenu/list',
	})
}

export function updateMenu(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sysMenu/updateMenu',
		params,
		paramsSerializer: params => {
			return qs.stringify(params, {indices: false})
		}
	})
}

export function addMenu(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sysMenu/addMenu',
		params
	});
}

export function deleteMenu(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sysMenu/deleteMenu',
		params
	});
}
