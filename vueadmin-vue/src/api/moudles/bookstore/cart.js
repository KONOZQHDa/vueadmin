import request from "@/network/request";
import qs from "qs";


export function addCart(data, params) {
	return request({
		method: 'post',
		url: 'http://localhost:8081/saveCart',
		data,
		params
	})
}

export function getCart() {
	return request({
		method: 'get',
		url: 'http://localhost:8081/getCart'
	})
}

export function removeCartItems(params) {
	return request({
		method: 'delete',
		url: 'http://localhost:8081/removeCartItems',
		params,
		paramsSerializer: params => {
			return qs.stringify(params, {indices: false})
		}
	})
}

export function updateCart(params) {
	return request({
		method: 'put',
		url: 'http://localhost:8081/updateCart',
		params
	})
}

