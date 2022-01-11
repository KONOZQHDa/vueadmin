import request from '../../../network/request'


export function getSysUserInfo() {
	return request({
		methods: 'get',
		url: 'http://localhost:8081/sysUser/userInfo',
	});
}