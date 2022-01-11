import request from '../../../network/request'
import qs from "qs"

export function searchUsers(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sysUser/searchUsers',
		params
	});
}

export function deleteUser(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sysUser/deleteUser',
		params
	});
}

export function resetPassword(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sysUser/resetPassword',
		params
	});
}

export function assignRoles(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sysUser/assignRole',
		params,
		paramsSerializer: params => {
			return qs.stringify(params, {indices: false})
		}
	});
}

export function removeRole(params) {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sysUser/removeRole',
		params: {
			userId: this.userIdToAssignRole,
			rolesId: this.$refs.roleTree.getCheckedKeys()
		},
		paramsSerializer: params => {
			return qs.stringify(params, {indices: false})
		}
	});
}

export function updateUser(data) {
	return request({
		method: 'put',
		url: 'http://localhost:8081/sysUser/updateUser',
		data: this.editForm
	});
}

export function addUser(data) {
	return request({
		method: 'post',
		url: 'http://localhost:8081/sysUser/addUser',
		data
	});
}

export function getUsers() {
	return request({
		method: 'get',
		url: 'http://localhost:8081/sysUser/userList'
	});
}

