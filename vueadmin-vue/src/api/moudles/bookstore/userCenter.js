import request from "@/network/request";

export function getUserInfo() {
	return request({
		method: 'get',
		url: 'http://localhost:8081/user/userInfo',
	});
}

export function updateUserInfo(data) {
	return request({
		method: 'put',
		url: 'http://localhost:8081/user/updateInfo',
		data
	});
}

export function changePassword(data) {
	return request({
		method: 'put',
		url: 'http://localhost:8081/user/changePassword',
		data
	});
}

export function changeAvatar(data) {
	return request({
		method: 'put',
		url: 'http://localhost:8081/user/updateInfo',
		data
	});
}

export function upImg(data) {
	return request({
		method: 'post',
		url: 'http://localhost:8081/book/upImg',
		data,
		headers: {'Content-Type': 'multipart/form-data'}
	});
}