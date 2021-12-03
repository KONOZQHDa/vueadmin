import request from "@/network/request";

export function getCaptcha() {
	return request({
		method: "get",
		url: "http://localhost:8081/captcha"
	})
}

export function login(params, data) {
	return request({
		method: 'post',
		url: "http://localhost:8081/login",
		params,
		data,
	})
}

export function sysLogin(params, data) {
	return request({
		method: 'post',
		url: "http://localhost:8081/sysLogin",
		params,
		data,
	})
}


export function getSysUserAvatarSrc(params) {
	return request({
		method: 'get',
		url: "http://localhost:8081/sysUser/getAvatar",
		params,
	})
}

export function getUserAvatarSrc(params) {
	return request({
		method: 'get',
		url: "http://localhost:8081/user/getAvatar",
		params,
	})
}

export function register(data) {
	return request({
		method: 'post',
		url: 'http://localhost:8081/user/register',
		data
	})
}