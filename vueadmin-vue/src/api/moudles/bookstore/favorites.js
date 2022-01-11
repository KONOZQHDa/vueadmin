import request from "@/network/request";

export function addFavorite(data, params) {
	return request({
		method: 'post',
		url: 'http://localhost:8081/saveFavorites',
		data,
		params
	});
}

export function getFavorites() {
	return request({
		method: 'get',
		url: 'http://localhost:8081/getFavorites'
	});
}

export function removeFavorite(params) {
	return request({
		method: 'delete',
		url: 'http://localhost:8081/removeFavorite',
		params
	})
}