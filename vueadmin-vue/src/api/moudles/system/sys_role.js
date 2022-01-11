import request from "@/network/request";
import qs from "qs";

export function searchRole(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sysRole/searchRole',
		params
	});
}

export function deleteRole(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sysRole/deleteRole',
		params
	});
}

export function updateRole(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sysRole/updateRole',
		params
	});
}

export function addRole(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sysRole/addRole',
		params
	});
}

export function getRoles() {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sysRole/getRoles',
	});
}

export function getPermissions(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sysRole/getPermissions',
		params
	});
}

export function assignPermmission(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sysRole/assignPermission',
		params,
		paramsSerializer: params => {
			return qs.stringify(params, {indices: false})
		}
	});
}

export function deletePermmission(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sysRole/deletePermission',
		params,
		paramsSerializer: params => {
			return qs.stringify(params, {indices: false})
		}
	});
}